package com.jacketing.view.innercontrollers;

import javafx.scene.chart.StackedBarChart;
import javafx.scene.layout.VBox;

public class ScheduleController {

  private StackedBarChart<String, Double> bestScheduleGraph;
  private VBox scheduleList;

  public ScheduleController(StackedBarChart<String, Double> bestScheduleGraph, VBox scheduleList) {
    this.bestScheduleGraph = bestScheduleGraph;
    this.scheduleList = scheduleList;
  }

}
