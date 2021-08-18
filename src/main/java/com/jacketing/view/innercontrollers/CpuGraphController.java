package com.jacketing.view.innercontrollers;

import com.jacketing.util.CPU.CpuReader;
import com.jacketing.util.CPU.CpuStatModel;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class CpuGraphController extends GraphController {

  public CpuGraphController(
    LineChart<String, Double> chart,
    NumberAxis threadAxis
  ) {
    super(chart);
    CpuReader reader = new CpuReader();
    for (int i = 0; i < 4; i++) {
      XYChart.Series<String, Double> series = new XYChart.Series<>();
      CpuStatModel model = new CpuStatModel(series, i);
      //reader.setSyntheticLoad();
      reader.addModel(model);
      chart.getData().add(series);
    }

    chart.setTitle("Thread Utilization (%)");
    chart.setLegendVisible(false);
    threadAxis.setAutoRanging(false);
    threadAxis.setUpperBound(100);
    threadAxis.setLowerBound(0);
    threadAxis.setTickUnit(5);
  }
}
