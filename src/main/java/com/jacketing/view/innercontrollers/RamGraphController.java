package com.jacketing.view.innercontrollers;

import com.jacketing.util.RAM.RamReader;
import com.jacketing.util.RAM.RamStatModel;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class RamGraphController extends GraphController {

  public RamGraphController(LineChart<String, Long> chart) {
    super(chart);
    XYChart.Series<String, Long> ramSeries = new XYChart.Series<>();
    chart.getData().add(ramSeries);
    chart.setLegendVisible(false);
    chart.setTitle("RAM Usage (MB)");
    RamStatModel model = new RamStatModel(ramSeries);
    new RamReader(model);
  }
}
