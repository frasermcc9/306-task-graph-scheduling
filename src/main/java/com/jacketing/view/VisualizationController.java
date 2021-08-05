package com.jacketing.view;

import com.jacketing.util.CpuReader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VisualizationController {

  @FXML
  private LineChart<String, Double> thread1Graph;

  @FXML
  private LineChart<String, Double> thread2Graph;

  @FXML
  private LineChart<String, Double> thread3Graph;

  @FXML
  private LineChart<String, Double> thread4Graph;

  public VisualizationController() {}

  @FXML
  public void initialize() {
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    List<double[]> model = new ArrayList<>();
    CpuReader reader = new CpuReader(model);
    reader.setSyntheticLoad();

    XYChart.Series<String, Double> series = new XYChart.Series<>();
    thread1Graph.getData().add(series);
    Axis xAxis = thread1Graph.getXAxis();
    xAxis.setTickLabelsVisible(false);
    xAxis.setAnimated(false);

    Axis yAxis = thread1Graph.getYAxis();
    yAxis.setAnimated(false);

    thread1Graph.setCreateSymbols(false);
    thread1Graph.setAnimated(false);

    new Thread(() -> {
      while(true) {
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        double[] util = model.get(model.size()-1);
        System.out.println(Arrays.toString(util));

        Platform.runLater(() -> {
          Date now = new Date();

          if (series.getData().size() > 20) {
            series.getData().remove(0);
          }

          series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), util[0]*100));
        });
      }
    }).start();
  }

}
