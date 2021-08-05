package com.jacketing.util.RAM;

import javafx.scene.chart.XYChart;
import org.junit.Test;

public class RamTest {

  @Test
  public void testRam() throws InterruptedException {
    XYChart.Series series = new XYChart.Series();
    RamStatModel model = new RamStatModel(series);
    RamReader reader = new RamReader(model);
    Thread.sleep(500);
  }

}
