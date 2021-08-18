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

  private AlgorithmObserver observer;

  private PrintStream ps;
  private ApplicationContext context;

  private LogsController logsController;

  public void setAlgorithmObserver(AlgorithmObserver observer) {
    this.observer = observer;
    new SearchSpaceController(observer, searchSpaceStackPane);
    new ScheduleController(observer, bestScheduleGraph, scheduleList, scheduleAxis);
    new StatsTextController(
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
  }

  public void setAlgorithmContext(ApplicationContext context) {
    inputFile.setText(context.getInputFile());
    numberCores.setText("Number of Cores: " + context.getCoresToCalculateWith());
    numberProcessors.setText("Number of Processors: " + context.getProcessorsToScheduleOn());
    algorithm.setText("Algorithm: DFS");
  }

  @FXML
  public void initialize() {
    logsController = new LogsController(logs);
    ps = new PrintStream(logsController);
    System.setOut(ps);
    new CpuGraphController(threadGraph, threadAxis);
    new RamGraphController(ramGraph);
  }
}
