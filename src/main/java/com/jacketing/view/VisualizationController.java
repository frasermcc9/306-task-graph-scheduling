package com.jacketing.view;

import com.jacketing.common.analysis.AlgorithmObserver;
import com.jacketing.view.innercontrollers.*;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class VisualizationController {

  @FXML
  private LineChart<String, Double> threadGraph;

  @FXML
  private NumberAxis threadAxis;

  @FXML
  private LineChart<String, Long> ramGraph;

  @FXML
  private Text duration, schedulesChecked, improvements, peakRam, peakCpu, currentBestTime, numberCores, numberProcessors, algorithm, time, inputFile;

  @FXML
  private Button stop;

  @FXML
  private VBox scheduleList;

  @FXML
  private StackedBarChart<String, Double> bestScheduleGraph;

  @FXML
  private TextArea logs;

  @FXML
  private StackPane searchSpaceStackPane;

  private AlgorithmObserver observer;

  public void setAlgorithmObserver(AlgorithmObserver observer) {
    this.observer = observer;
    new SearchSpaceController(observer, searchSpaceStackPane);
  }

  @FXML
  public void initialize() {
    new CpuGraphController(threadGraph, threadAxis);
    new RamGraphController(ramGraph);
    new StatsTextController(duration, schedulesChecked, improvements, peakRam, peakCpu, currentBestTime, numberCores, numberProcessors, algorithm, time, inputFile);
    new LogsController(logs);
    new ScheduleController(bestScheduleGraph, scheduleList);
  }


}
