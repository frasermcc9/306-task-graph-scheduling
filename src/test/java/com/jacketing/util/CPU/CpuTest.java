package com.jacketing.util.CPU;

import javafx.scene.chart.XYChart;
import org.junit.Test;

public class CpuTest {

  @Test
  public void testCpu() throws InterruptedException {
    XYChart.Series<String, Double> series = new XYChart.Series();

    for (int i = 0; i <= 100; i++) {
      series.getData().add(new XYChart.Data<>("", 1D));
    }

    CpuStatModel model = new CpuStatModel(series, 0);
    CpuReader reader = new CpuReader();
    reader.setSyntheticLoad();
    reader.addModel(model);
    Thread.sleep(500);
  }
}
