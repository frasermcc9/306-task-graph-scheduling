package com.jacketing.util.CPU;

import javafx.application.Platform;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.chart.XYChart.Data;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class CpuStatModel {

  private Series<String, Double> series;
  private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss:SS");
  private final int index;

  public CpuStatModel(Series<String, Double> series, int index) {
    this.series = series;
    this.index = index;
  }

  public void change(double[] data) {
    Platform.runLater(() -> {
      Date now = new Date();

      if (series.getData().size() > 100) {
        series.getData().remove(0);
      }

      series.getData().add(new Data<>(simpleDateFormat.format(now), data[index]*100));
    });
  }

}
