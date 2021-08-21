/*
 * Copyright 2021 Team Jacketing
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property of Team
 * Jacketing (the author) and its affiliates, if any. The intellectual and
 * technical concepts contained herein are proprietary to Team Jacketing, and
 * are protected by copyright law. Dissemination of this information or
 * reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from the author.
 *
 */

const fs = require("fs");
const path = require("path");
const os = require("os");
const exec = require("child_process").exec;
const { red } = require("colors");

const graphDirectory = path.join(__dirname, "output");

const getGraphFileNames = () => {
  return fs.readdirSync(graphDirectory);
};

const jarPath = "scheduler.jar";

const verify = async () => {
  const graphFileNames = getGraphFileNames();
  for (const file of graphFileNames) {
    if (!file.includes("Homogeneous")) continue;
    if (file.includes("_30_")) continue;
    if (file.includes("_21_")) continue;
    if (file.includes("_16_")) continue;
    //if (!file.includes("Random_Nodes")) continue;
    if (!file.includes("Independent")) continue;

    const graphPath = `output/${file}`;
    const regex = /(\d+)*\.dot$/g;
    const processorCount = regex.exec(graphPath)[1];

    console.log(`Testing ${graphPath}`);

    const command = `java -jar -Xmx4g "${jarPath}" "${graphPath}" ${processorCount} --no-output --fail-suboptimal`;

    await new Promise((resolve, reject) => {
      let processTimeout;
      const proc = exec(command, (err, out) => {
        clearTimeout(processTimeout);
        if (err) {
          console.error(
            red(`Error with ${graphPath} @ ${processorCount} processors!`)
          );
          console.error(out);
          resolve();
        } else {
          resolve();
        }
      });

      processTimeout = setTimeout(() => {
        console.log(red("PROCESS TIMED OUT"));
        if (os.platform() === "win32") {
          exec("taskkill /pid " + proc.pid + " /T /F");
        } else {
          proc.kill();
        }
      }, 5000);
    });
  }
};
(async () => await verify())();
