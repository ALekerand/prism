"""Liste les requêtes POST/PUT/PATCH dont le body est manquant ou inutilisable (vide / null / {})."""
import json

p = r"postman\PRISM.postman_collection.json"
data = json.load(open(p, encoding="utf-8"))

issues = []


def walk(items, path):
	for it in items:
		if "item" in it:
			walk(it["item"], path + [it.get("name", "?")])
		else:
			req = it.get("request", {})
			method = (req.get("method") or "").upper()
			if method not in {"POST", "PUT", "PATCH"}:
				continue
			body = req.get("body")
			raw = (body or {}).get("raw") if body else None
			if not body or (raw is not None and raw.strip() in {"", "{}"}):
				url = req.get("url", {})
				url_raw = url.get("raw", "?") if isinstance(url, dict) else (url or "?")
				issues.append((" / ".join(path), it.get("name"), method, url_raw))


walk(data["item"], [])

current_parent = None
for parent, name, method, url in issues:
	if parent != current_parent:
		print(f"\n== {parent} ==")
		current_parent = parent
	print(f"  {method:6s} {url}  ({name})")
print(f"\nTOTAL: {len(issues)}")
