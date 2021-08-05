package com.jacketing.view;

import javafx.scene.chart.LineChart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ViewTest {

  @Mock
  private LineChart<String, Double> thread1Graph;

  @Mock
  private LineChart<String, Double> thread2Graph;

  @Mock
  private LineChart<String, Double> thread3Graph;

  @Mock
  private LineChart<String, Double> thread4Graph;

  @Mock
  private LineChart<String, Double> ramGraph;

  @InjectMocks
  private VisualizationController visualizationController;

  @Test
  public void testVisualizationController() throws InterruptedException {
    Thread.sleep(1000);
  }

}
