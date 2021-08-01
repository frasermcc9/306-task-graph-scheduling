/**
 * Reads the input GXL file and returns a DOT file inside output.
 * Files are taken from the input directory, and output to the output directory.
 *
 * Requires the GXL2DOT executable installed (i.e. apt get graphviz).
 **/

const { resolve } = require("path");
const { readdir } = require("fs").promises;
const { execSync } = require("child_process");

async function* getFiles(dir) {
  const dirents = await readdir(dir, { withFileTypes: true });
  for (const dirent of dirents) {
    const res = resolve(dir, dirent.name);
    if (dirent.isDirectory()) {
      yield* getFiles(res);
    } else {
      yield res;
    }
  }
}

(async () => {
  for await (const f of getFiles(__dirname + "/input")) {
    const output = replaceLast(f.replace(".gxl", ".dot"), "input", "output");
    const cmd = `gxl2dot -o ${output} ${f}`;
    execSync(cmd);
  }
})();

/**
 *
 * @param {string} string
 * @param {string} toReplace
 * @param {string} replacement
 * @returns {string}
 */
function replaceLast(string, toReplace, replacement) {
  const a = string.split("");
  const index = string.lastIndexOf(toReplace);
  a.splice(index, toReplace.length, replacement);
  return a.join("");
}
