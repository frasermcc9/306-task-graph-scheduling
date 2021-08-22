package com.jacketing.view.innercontrollers;

import com.jacketing.algorithm.structures.ScheduleV1;
import com.jacketing.common.analysis.AlgorithmObserver;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import javafx.scene.text.Text;

public class StatsTextController {

  private Text duration, schedulesChecked, improvements, schedulesCulled, duplicatesRemoved, currentBestTime, numberCores, numberProcessors, algorithm, time, inputFile;
  private AlgorithmObserver observer;
  private Instant then = Instant.now();
  private Thread pollingThread;

  public StatsTextController(
    AlgorithmObserver observer,
    Text duration,
    Text schedulesChecked,
    Text improvements,
    Text schedulesCulled,
    Text duplicatesRemoved,
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
    this.duplicatesRemoved = duplicatesRemoved;
    this.schedulesCulled = schedulesCulled;
    this.currentBestTime = currentBestTime;
    this.numberCores = numberCores;
    this.numberProcessors = numberProcessors;
    this.algorithm = algorithm;
    this.time = time;
    this.inputFile = inputFile;
    Thread pollingThread = new Thread(
      () -> {
        while (!observer.isFinished()) {
          pollStats();
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        pollStats();
      }
    );
    pollingThread.start();
    this.pollingThread = pollingThread;
  }

  public void stop() {
    pollingThread.suspend();
  }

  public void resume() {
    pollingThread.resume();
  }

  private void pollStats() {
    Duration durationObj = Duration.between(then, Instant.now());
    duration.setText("Duration: " + durationObj.getSeconds() + "s");
    time.setText(new Date().toString());
    int improvementsMade = observer.getImprovementsFound();
    improvementsMade = improvementsMade == 0 ? 0 : improvementsMade - 1;

    schedulesChecked.setText(
      "Schedules Checked: " + observer.getCheckedSchedules()
    );
    improvements.setText(
      "Improvements Made: " + improvementsMade
    );
    schedulesCulled.setText(
      "Schedules Culled: " + observer.getCulledSchedules()
    );
    duplicatesRemoved.setText(
      "Duplicates Removed: " + observer.getDuplicateSchedules()
    );

    ScheduleV1 current = observer.getCurrentBestSchedule();
    if (current != null) {
      currentBestTime.setText(
        "Current Best Time: " + observer.getCurrentBestSchedule().getDuration()
      );
    }
  }
}
