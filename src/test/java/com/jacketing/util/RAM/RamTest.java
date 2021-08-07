package com.jacketing.util.RAM;

import javafx.scene.chart.XYChart;
import org.junit.Test;

public class RamTest {

  @Test
  public void testRam() throws InterruptedException {
    XYChart.Series<String, Long> series = new XYChart.Series();

    for (int i = 0; i <= 100; i++) {
      series.getData().add(new XYChart.Data<>("", 1L));
    }

    RamStatModel model = new RamStatModel(series);
    RamReader reader = new RamReader(model);
    Thread.sleep(500);
  }
}
