"""Compte les FKs résiduelles au format imbriqué vs format aplati dans les bodies raw Postman."""
import json
import re

with open(r"postman/PRISM.postman_collection.json", encoding="utf-8") as f:
	data = json.load(f)

nested = 0
flat = 0
samples_flat = []


def walk(items):
	global nested, flat
	for it in items:
		if isinstance(it, dict):
			if "item" in it:
				walk(it["item"])
				continue
			body = (it.get("request") or {}).get("body") or {}
			raw = body.get("raw")
			if not raw:
				continue
			try:
				parsed = json.loads(raw)
			except Exception:
				continue
			scan(parsed, it.get("name"))


def scan(node, ctx):
	global nested, flat
	if isinstance(node, dict):
		for k, v in node.items():
			if re.match(r"^id[A-Z]", k or ""):
				if isinstance(v, dict) and "id" in v:
					nested += 1
				elif isinstance(v, (int, str)):
					flat += 1
					if len(samples_flat) < 5:
						samples_flat.append((ctx, k, v))
			scan(v, ctx)
	elif isinstance(node, list):
		for x in node:
			scan(x, ctx)


walk(data["item"])
print("FK_NESTED_LEFT:", nested)
print("FK_FLAT_FOUND :", flat)
print("Samples flat  :")
for c, k, v in samples_flat:
	print(f"  - {c}: {k} = {v}")
