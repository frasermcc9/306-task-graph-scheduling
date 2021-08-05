package com.jacketing.util.CPU;

import com.jacketing.util.RAM.RamReader;
import com.jacketing.util.RAM.RamStatModel;
import javafx.scene.chart.XYChart;
import org.junit.Test;

public class CpuTest {

  @Test
  public void testCpu() throws InterruptedException {
    XYChart.Series series = new XYChart.Series();
    CpuStatModel model = new CpuStatModel(series, 0);
    CpuReader reader = new CpuReader();
    reader.setSyntheticLoad();
    reader.addModel(model);
    Thread.sleep(500);
  }

}
