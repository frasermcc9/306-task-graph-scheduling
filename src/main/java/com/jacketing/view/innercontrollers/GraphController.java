package com.jacketing.view.innercontrollers;

import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;

public class GraphController {

  protected LineChart<String, ?> chart;

  protected GraphController(LineChart<String, ?> chart) {
    this.chart = chart;
    removeAnimationsAndTicks(chart);
  }
  private void removeAnimationsAndTicks(LineChart<String, ?> chart) {
      Axis xAxis = chart.getXAxis();
      xAxis.setTickLabelsVisible(false);
      xAxis.setAnimated(false);

      Axis yAxis = chart.getYAxis();
      yAxis.setAnimated(false);

      chart.setCreateSymbols(false);
      chart.setAnimated(false);
  }

}
