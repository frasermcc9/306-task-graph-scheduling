package com.jacketing.util.CPU;

import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.application.Platform;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

public class CpuStatModel {

  private Series<String, Double> series;
  private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
    "HH:mm:ss:SS"
  );
  private final int index;

  public CpuStatModel(Series<String, Double> series, int index) {
    this.series = series;
    this.index = index;
  }

  public void change(double[] data, Date now) {
    Platform.runLater(
      () -> {
        if (series.getData().size() > 100) {
          series.getData().remove(0);
        }

        series
          .getData()
          .add(new Data<>(simpleDateFormat.format(now), data[index] * 100));
      }
    );
  }
}
