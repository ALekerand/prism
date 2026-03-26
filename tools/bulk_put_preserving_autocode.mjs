import fs from "fs";
import path from "path";
import { fileURLToPath } from "url";

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const ROOT = path.join(__dirname, "../src/main/java/com/dcspa/prism/controller");

/** Method body lines use two tabs (common in this project). */
const PATTERN_DOUBLE_TAB_BODY =
  /@PutMapping\("\/\{id\}"\)\s*\r?\n\tpublic ResponseEntity<(\w+)> update\(@PathVariable Integer id, @RequestBody \1 (\w+)\) \{\r?\n\t\t\2\.setId\(id\);\r?\n\t\t\1 saved = (\w+)\.save\(\2\);\r?\n\t\treturn ResponseEntity\.ok\(saved\);\r?\n\t\}/g;

/** Method body lines use one tab (some controllers). */
const PATTERN_SINGLE_TAB_BODY =
  /@PutMapping\("\/\{id\}"\)\s*\r?\n\tpublic ResponseEntity<(\w+)> update\(@PathVariable Integer id, @RequestBody \1 (\w+)\) \{\r?\n\t\2\.setId\(id\);\r?\n\t\1 saved = (\w+)\.save\(\2\);\r?\n\treturn ResponseEntity\.ok\(saved\);\r?\n\t\}/g;

/** Inline ok(service.save(param)); method body lines indented with two tabs. */
const PATTERN_INLINE_OK_DOUBLE_TAB =
  /@PutMapping\("\/\{id\}"\)\s*\r?\n\tpublic ResponseEntity<(\w+)> update\(@PathVariable Integer id, @RequestBody \1 (\w+)\) \{\r?\n\t\t\2\.setId\(id\);\r?\n\t\treturn ResponseEntity\.ok\((\w+)\.save\(\2\)\);\r?\n\t\}/g;

/** Same as above but single-tab method body (rare). */
const PATTERN_INLINE_OK_SINGLE_TAB =
  /@PutMapping\("\/\{id\}"\)\s*\r?\n\tpublic ResponseEntity<(\w+)> update\(@PathVariable Integer id, @RequestBody \1 (\w+)\) \{\r?\n\t\2\.setId\(id\);\r?\n\treturn ResponseEntity\.ok\((\w+)\.save\(\2\)\);\r?\n\t\}/g;

function block(entity, param, service, inner, eol) {
  return (
    `@PutMapping("/{id}")${eol}` +
    `\tpublic ResponseEntity<${entity}> update(@PathVariable Integer id, @RequestBody ${entity} ${param}) {${eol}` +
    `${inner}return ReferentialPutHelper.putPreservingAutoCode(id, ${param}, ${service}::findById, ${service}::save);${eol}` +
    `\t}`
  );
}

function main() {
  const files = fs.readdirSync(ROOT).filter((f) => f.endsWith(".java"));
  for (const name of files.sort()) {
    const fp = path.join(ROOT, name);
    let text = fs.readFileSync(fp, "utf8");
    if (text.includes("ReferentialPutHelper.putPreservingAutoCode")) continue;

    const eol = text.includes("\r\n") ? "\r\n" : "\n";

    let newText = text.replace(PATTERN_DOUBLE_TAB_BODY, (_, entity, param, service) =>
      block(entity, param, service, "\t\t", eol),
    );
    if (newText === text) {
      newText = text.replace(PATTERN_SINGLE_TAB_BODY, (_, entity, param, service) =>
        block(entity, param, service, "\t", eol),
      );
    }
    if (newText === text) {
      newText = text.replace(PATTERN_INLINE_OK_DOUBLE_TAB, (_, entity, param, service) =>
        block(entity, param, service, "\t\t", eol),
      );
    }
    if (newText === text) {
      newText = text.replace(PATTERN_INLINE_OK_SINGLE_TAB, (_, entity, param, service) =>
        block(entity, param, service, "\t", eol),
      );
    }
    if (newText === text) continue;

    const IMP = "import com.dcspa.prism.controller.support.ReferentialPutHelper;";
    const withImport = newText.includes(IMP)
      ? newText
      : newText.replace(/^package com\.dcspa\.prism\.controller;\r?\n/m, (m) => m + IMP + (m.endsWith("\r\n") ? "\r\n" : "\n"));
    fs.writeFileSync(fp, withImport, "utf8");
    console.log("updated", name);
  }
}

main();
