"""Auto-fill empty test bodies in the Postman collection.

For every Postman request whose body is `{}` we try to:
  * locate the controller responsible for the URL path under
    `src/main/java/com/dcspa/prism/controller`
  * inspect the `@RequestBody` type used by POST/PUT methods
  * generate a JSON skeleton from the corresponding entity / DTO file
    in `src/main/java/com/dcspa/prism/{dto,entity}`.

The script favours stability: it preserves bodies that already contain data,
only overwrites `{}`/empty raw payloads.
"""

from __future__ import annotations

import json
import re
from collections import OrderedDict
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
COLLECTION_PATH = ROOT / "postman" / "PRISM.postman_collection.json"
JAVA_SRC = ROOT / "src" / "main" / "java" / "com" / "dcspa" / "prism"
CONTROLLER_DIR = JAVA_SRC / "controller"
DTO_DIR = JAVA_SRC / "dto"
ENTITY_DIR = JAVA_SRC / "entity"

INTEGER_LIKE = {"Integer", "Long", "BigInteger", "Short"}
DECIMAL_LIKE = {"Float", "Double", "BigDecimal"}
DATE_LIKE = {"LocalDate"}
DATETIME_LIKE = {"LocalDateTime", "OffsetDateTime", "Instant", "Date"}


def load_controllers() -> dict[str, Path]:
	"""Map URL prefix (e.g. `api/appui-partenaire`) to controller path."""
	mapping: dict[str, Path] = {}
	pattern = re.compile(r'@RequestMapping\("/?([^"]+)"\)')
	for file in CONTROLLER_DIR.glob("*.java"):
		text = file.read_text(encoding="utf-8", errors="ignore")
		match = pattern.search(text)
		if not match:
			continue
		prefix = match.group(1).strip("/").lower()
		mapping[prefix] = file
	return mapping


FIELD_RE = re.compile(
	r"private\s+(?:final\s+)?([A-Za-z0-9_<>,\s]+?)\s+([a-zA-Z_][a-zA-Z0-9_]*)\s*;"
)


def parse_fields(java_file: Path) -> list[tuple[str, str]]:
	text = java_file.read_text(encoding="utf-8", errors="ignore")
	fields: list[tuple[str, str]] = []
	for type_, name in FIELD_RE.findall(text):
		t = type_.strip()
		if name in {"id", "serialVersionUID"}:
			continue
		fields.append((t, name))
	return fields


def find_request_body_type(controller_file: Path, method: str) -> str | None:
	text = controller_file.read_text(encoding="utf-8", errors="ignore")
	method = method.upper()
	candidates: list[str] = []
	for mapping_anno in ("PostMapping", "PutMapping"):
		if (method == "POST" and mapping_anno != "PostMapping") or (
			method == "PUT" and mapping_anno != "PutMapping"
		):
			continue
		for match in re.finditer(
			rf"@{mapping_anno}[^\n]*\n[^@\n]*?@RequestBody[^\n]*?(\w+)\s+\w+", text
		):
			candidates.append(match.group(1))
	return candidates[0] if candidates else None


def find_type_file(simple_name: str) -> Path | None:
	for directory in (DTO_DIR, ENTITY_DIR):
		candidate = directory / f"{simple_name}.java"
		if candidate.exists():
			return candidate
	return None


PRIMITIVE_TYPES = {
	"Boolean", "boolean", "int", "long", "Integer", "Long", "Short", "BigInteger",
	"Float", "Double", "BigDecimal", "String", "LocalDate", "LocalDateTime",
	"OffsetDateTime", "Instant", "Date", "Character", "char", "byte", "Byte",
}


def placeholder_for_field(type_: str, name: str) -> object:
	t = type_.strip()
	lower = name.lower()
	is_list = t.startswith("List<") or t.startswith("Set<")
	if is_list:
		return []
	if t == "Boolean" or t == "boolean":
		return True
	if t in INTEGER_LIKE or t in {"int", "long"}:
		if lower.startswith("id"):
			return 1
		return 0
	if t in DECIMAL_LIKE:
		return 0
	if t in DATE_LIKE:
		return "2026-01-01"
	if t in DATETIME_LIKE:
		return "2026-01-01T00:00:00"
	if t == "String":
		if lower.startswith("code"):
			return f"{name.upper()}-1"
		if lower.startswith("libelle"):
			return f"Libelle {name}"
		if lower.startswith("nom"):
			return "Nom test"
		if lower.startswith("contact") or lower.endswith("phone") or lower.endswith("tel"):
			return "0102030405"
		if lower.endswith("mail") or lower.startswith("email"):
			return "test@example.com"
		return f"{name} test"
	# JPA entity reference (e.g. CategorieAppui idCategorieAppui).
	if t and t[0].isupper() and t not in PRIMITIVE_TYPES:
		return {"id": 1}
	return None


def build_payload(java_file: Path) -> "OrderedDict[str, object]":
	payload: OrderedDict[str, object] = OrderedDict()
	for type_, name in parse_fields(java_file):
		value = placeholder_for_field(type_, name)
		payload[name] = value
	return payload


def url_to_path_prefix(url: str) -> str | None:
	match = re.search(r"\{\{baseUrl\}\}/([^?]+)", url)
	if not match:
		return None
	parts = match.group(1).split("/")
	if not parts or parts[0] != "api":
		return None
	# Strip trailing variable / id segment.
	clean = []
	for part in parts:
		if part.startswith("{{"):
			break
		clean.append(part)
	return "/".join(clean).lower()


def find_controller_for_url(url: str, controllers: dict[str, Path]) -> Path | None:
	prefix = url_to_path_prefix(url)
	if not prefix:
		return None
	if prefix in controllers:
		return controllers[prefix]
	# fallback: try matching any registered prefix as URL prefix (case-insensitive)
	for known, path in controllers.items():
		if prefix == known.lower():
			return path
	return None


def fill_collection() -> int:
	with COLLECTION_PATH.open("r", encoding="utf-8") as f:
		data = json.load(f)

	controllers = load_controllers()
	updated = 0

	def visit(items):
		nonlocal updated
		for it in items:
			if "item" in it:
				visit(it["item"])
				continue
			req = it.get("request", {})
			body = req.get("body") or {}
			if body.get("mode") != "raw":
				continue
			raw = body.get("raw") or ""
			stripped = raw.strip()
			# Re-fill empty bodies, plus auto-generated payloads that contain
			# null FK references (these were produced by a previous, less
			# accurate run of this script).
			needs_fill = stripped in ("", "{}") or '": null' in raw
			if not needs_fill:
				continue
			method = req.get("method", "")
			url_obj = req.get("url", {})
			url_raw = url_obj.get("raw") if isinstance(url_obj, dict) else url_obj
			if not url_raw:
				continue
			controller = find_controller_for_url(url_raw, controllers)
			if controller is None:
				continue
			body_type = find_request_body_type(controller, method)
			if body_type is None:
				# default to entity inferred from controller file name
				stem = controller.stem
				if stem.endswith("Controller"):
					body_type = stem[: -len("Controller")]
				else:
					continue
			type_file = find_type_file(body_type)
			if type_file is None:
				continue
			payload = build_payload(type_file)
			if not payload:
				continue
			body["raw"] = json.dumps(payload, indent=2, ensure_ascii=False)
			updated += 1

	visit(data["item"])

	with COLLECTION_PATH.open("w", encoding="utf-8") as f:
		json.dump(data, f, indent=2, ensure_ascii=False)
	return updated


if __name__ == "__main__":
	count = fill_collection()
	print(f"FILLED_BODIES={count}")
