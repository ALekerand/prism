import json
from collections import defaultdict

p = r"postman\PRISM.postman_collection.json"
data = json.load(open(p, encoding="utf-8"))

empties = []


def walk(items, path):
	for it in items:
		if "item" in it:
			walk(it["item"], path + [it.get("name", "?")])
		else:
			req = it.get("request", {})
			body = req.get("body") or {}
			if body.get("mode") == "raw" and (body.get("raw") in ("{}", "")):
				method = req.get("method", "?")
				url = req.get("url", {})
				if isinstance(url, dict):
					raw = url.get("raw", "?")
				else:
					raw = url or "?"
				empties.append(("/".join(path), it.get("name"), method, raw))


walk(data["item"], [])

groups = defaultdict(list)
for parent, name, m, u in empties:
	groups[parent].append((name, m, u))

for parent in sorted(groups.keys()):
	print(f"== {parent} ==")
	for name, m, u in groups[parent]:
		print(f"  {m:6s} {u}  ({name})")
print(f"\nTOTAL EMPTY: {len(empties)}")
