package com.jacketing.view.innercontrollers;

import com.jacketing.common.analysis.AlgorithmObserver;
import javafx.scene.text.Text;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class StatsTextController {

  private Text duration, schedulesChecked, improvements, peakRam, peakCpu, currentBestTime, numberCores, numberProcessors, algorithm, time, inputFile;
  private AlgorithmObserver observer;
  private Instant then = Instant.now();

  public StatsTextController(
    AlgorithmObserver observer,
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
    this.observer = observer;
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



    new Thread(() -> {
      while (true) {
        pollStats();
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

  private void pollStats() {
    Duration durationObj = Duration.between(then, Instant.now());
    duration.setText("Duration: " + durationObj.getSeconds() + "s");

    schedulesChecked.setText(observer.getCheckedSchedules() + "");
    improvements.setText(observer.getDuplicateSchedules() + "");
  }


}
