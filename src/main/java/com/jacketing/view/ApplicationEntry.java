/*
 * Copyright 2021 Team Jacketing
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property of Team
 * Jacketing (the author) and its affiliates, if any. The intellectual and
 * technical concepts contained herein are proprietary to Team Jacketing, and
 * are protected by copyright law. Dissemination of this information or
 * reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from the author.
 *
 */

package com.jacketing.view;

import com.jacketing.common.analysis.AlgorithmObserver;
import com.jacketing.io.cli.ApplicationContext;
import java.io.ByteArrayOutputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ApplicationEntry extends Application {

  private static AlgorithmObserver observer;
  private static ByteArrayOutputStream outputStream;

  public static void launch(
    AlgorithmObserver algorithmObserver,
    ByteArrayOutputStream os
  ) {
    observer = algorithmObserver;
    outputStream = os;
    ApplicationEntry.launch(ApplicationEntry.class);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(
      getClass().getClassLoader().getResource("final.fxml")
    );

    Parent root;
    root = loader.load();

    VisualizationController controller = loader.getController();
    controller.setAlgorithmObserver(observer);
    System.out.println(outputStream);
    Scene scene = new Scene(root);
    primaryStage.setTitle("Jacketing Studio");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
