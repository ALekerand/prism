"""Convertit les FKs Postman { "idXxx": { "id": N } } en { "idXxx": N }.

- Lit la collection PRISM.postman_collection.json
- Pour chaque request body raw qui parse en JSON, parcourt récursivement
  et remplace tout objet { "id": <int|str> } affecté à une clé qui commence par "id"
  (lettre majuscule ensuite), par la valeur scalaire de "id".
- Sauvegarde la collection (formatage 2 espaces, comme l'existant).
"""
import json
import re

PATH = r"postman\PRISM.postman_collection.json"


def is_pure_id_ref(node):
	return (
		isinstance(node, dict)
		and "id" in node
		and len(node) == 1
		and isinstance(node["id"], (int, str))
	)


def flatten_id_refs(obj, parent_key=None):
	if isinstance(obj, dict):
		for k, v in list(obj.items()):
			# clé ressemblant à un FK : "idXxx" ou "id_xxx"
			fk_like = re.match(r"^id[_A-Z]", k or "")
			if fk_like and is_pure_id_ref(v):
				obj[k] = v["id"]
			else:
				flatten_id_refs(v, k)
	elif isinstance(obj, list):
		for it in obj:
			flatten_id_refs(it, parent_key)
	return obj


def walk_collection(items, on_request):
	for it in items:
		if isinstance(it, dict):
			if "item" in it:
				walk_collection(it["item"], on_request)
			elif "request" in it:
				on_request(it["request"])


def main():
	with open(PATH, encoding="utf-8") as f:
		data = json.load(f)

	changed_count = 0

	def process(req):
		nonlocal changed_count
		body = req.get("body") or {}
		if body.get("mode") != "raw":
			return
		raw = body.get("raw")
		if not raw or not raw.strip():
			return
		try:
			parsed = json.loads(raw)
		except json.JSONDecodeError:
			return
		before = json.dumps(parsed, ensure_ascii=False)
		flatten_id_refs(parsed)
		after = json.dumps(parsed, ensure_ascii=False)
		if before != after:
			body["raw"] = json.dumps(parsed, ensure_ascii=False, indent=2)
			changed_count += 1

	walk_collection(data["item"], process)

	with open(PATH, "w", encoding="utf-8") as f:
		json.dump(data, f, ensure_ascii=False, indent=2)
		f.write("\n")

	print(f"OK: {changed_count} body(ies) updated")


if __name__ == "__main__":
	main()
