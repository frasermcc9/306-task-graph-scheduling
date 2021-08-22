package com.jacketing.view;

import com.jacketing.common.analysis.AlgorithmObserver;
import com.jacketing.io.cli.AlgorithmContext;
import com.jacketing.io.cli.ApplicationContext;
import com.jacketing.view.innercontrollers.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class VisualizationController {

  @FXML
  private StackedBarChart<String, Integer> bestScheduleGraph;

  @FXML
  private LineChart<String, Double> threadGraph;

  @FXML
  private NumberAxis threadAxis, scheduleAxis;

  @FXML
  private LineChart<String, Long> ramGraph;

  @FXML
  private Text duration, schedulesChecked, schedulesCulled, duplicatesRemoved, improvements, currentBestTime, numberCores, numberProcessors, algorithm, time, inputFile;

  @FXML
  private Button stop;

  @FXML
  private VBox scheduleList;

  @FXML
  private TextArea logs;

  @FXML
  private StackPane searchSpaceStackPane;

  private PrintStream ps;
  private ApplicationContext context;
  private Thread algorithmThread;
  private AlgorithmObserver observer;
  private LogsController logsController;
  private SearchSpaceController searchSpaceController;
  private StatsTextController statsTextController;
  private boolean running = true;

  public void setAlgorithmFields(AlgorithmObserver observer, Thread thread, ApplicationContext context) {
    this.observer = observer;
    this.context = context;
    this.algorithmThread = thread;
    start();
  }

  @FXML
  public void initialize() {
    logsController = new LogsController(logs);
    ps = new PrintStream(logsController);
    System.setOut(ps);
    new CpuGraphController(threadGraph, threadAxis);
    new RamGraphController(ramGraph);
  }

  private void start() {
    searchSpaceController = new SearchSpaceController(observer, searchSpaceStackPane);
    new ScheduleController(observer, bestScheduleGraph, scheduleList, scheduleAxis);
    statsTextController = new StatsTextController(
      observer,
      duration,
      schedulesChecked,
      improvements,
      schedulesCulled,
      duplicatesRemoved,
      currentBestTime,
      numberCores,
      numberProcessors,
      algorithm,
      time,
      inputFile
    );

    inputFile.setText(context.getInputFile());
    numberCores.setText("Number of Cores: " + context.getCoresToCalculateWith());
    numberProcessors.setText("Number of Processors: " + context.getProcessorsToScheduleOn());
    algorithm.setText("Algorithm: DFS");

    String resumeColour = "-fx-background-color: #00aeef;";
    String stopColour = "-fx-background-color: #e84855;";

    stop.setOnAction((event) -> {
      if (running) {
        algorithmThread.suspend();
        statsTextController.stop();
        searchSpaceController.stop();
        stop.setStyle(resumeColour);
        stop.setText("Resume");
      } else {
        algorithmThread.resume();
        statsTextController.resume();
        searchSpaceController.resume();
        stop.setStyle(stopColour);
        stop.setText("Stop");
      }

      running = !running;
    });

    algorithmThread.start();
  }
}
