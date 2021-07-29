const process = require("process");
const fs = require("fs");

const args = process.argv;
const message = args[2];

const commitMsg = fs.readFileSync(message, "utf-8");
const allowed =
  /Merge|:rocket:|:sparkles:|:bug:|:boom:|:card_index:|:books:|:bulb:|:racehorse:|:lipstick:|:rotating_light:|:zap:|:art:|:hammer:|:fire:|:green_heart:|:toolbox:|:lock:|:arrow_up:|:arrow_down:|:shirt:|:ambulance:|:wrench:|:package:|:truck:|:apple:|:penguin:|:checkered_flag:|:construction:/g;

if (allowed.test(commitMsg)) {
  process.exit(0);
}
process.exit(1);
