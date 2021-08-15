package com.jacketing.view.innercontrollers;

import javafx.scene.text.Text;

public class StatsTextController {

  private Text duration, schedulesChecked, improvements, peakRam, peakCpu, currentBestTime, numberCores, numberProcessors, algorithm, time, inputFile;

  public StatsTextController(
    Text duration,
    Text schedulesChecked,
    Text improvements,
    Text peakRam,
    Text peakCpu,
    Text currentBestTime,
    Text numberCores,
    Text numberProcessors,
    Text algorithm,
    Text time,
    Text inputFile
  ) {
    this.duration = duration;
    this.schedulesChecked = schedulesChecked;
    this.improvements = improvements;
    this.peakRam = peakRam;
    this.peakCpu = peakCpu;
    this.currentBestTime = currentBestTime;
    this.numberCores = numberCores;
    this.numberProcessors = numberProcessors;
    this.algorithm = algorithm;
    this.time = time;
    this.inputFile = inputFile;
  }
}
