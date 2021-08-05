package com.jacketing.util.RAM;

import javafx.application.Platform;
import javafx.scene.chart.XYChart;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RamStatModel {

  private XYChart.Series<String, Long> series;
  private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss:SS");

  public RamStatModel(XYChart.Series<String, Long> series) {
    this.series = series;
  }

  public void change(long used) {
    Platform.runLater(() -> {
      Date now = new Date();

      if (series.getData().size() > 200) {
        series.getData().remove(0);
      }

      long MB = used / 1000 / 1000;

      series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), MB));
    });
  }

}
