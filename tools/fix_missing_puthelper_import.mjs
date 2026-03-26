import fs from "fs";
import path from "path";
import { fileURLToPath } from "url";

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const ROOT = path.join(__dirname, "../src/main/java/com/dcspa/prism/controller");
const IMP = "import com.dcspa.prism.controller.support.ReferentialPutHelper;";

for (const name of fs.readdirSync(ROOT)) {
  if (!name.endsWith(".java")) continue;
  const fp = path.join(ROOT, name);
  let text = fs.readFileSync(fp, "utf8");
  if (!text.includes("ReferentialPutHelper.putPreservingAutoCode") || text.includes(IMP)) continue;
  const next = text.replace(
    /^package com\.dcspa\.prism\.controller;\r?\n/m,
    (m) => m + IMP + (m.endsWith("\r\n") ? "\r\n" : "\n"),
  );
  if (next === text) {
    console.warn("skip (no package match)", name);
    continue;
  }
  fs.writeFileSync(fp, next, "utf8");
  console.log("import added", name);
}
