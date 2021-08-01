// ********************************************************************************
// Copyright 2021 Team Jacketing All Rights Reserved.
//
// NOTICE: All information contained herein is, and remains the property of Team
// Jacketing (the author) and their affiliates, if any. The intellectual and
// technical concepts contained herein are proprietary to Team Jacketing , and
// are protected by copyright law. Dissemination of this information or
// reproduction of this material is strictly forbidden unless prior written
// permission is obtained from the author.
// *******************************************************************************

package com.jacketing;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Entry extends Application {

  public static void main(String... argv) {
    Args args = new Args();

    try {
      JCommander.newBuilder().addObject(args).build().parse(argv);
      args.validate();

      if (args.isVisualized()) {
        launch();
      }

      System.out.println("Starting search...");
    } catch (ParameterException e) {
      System.out.println(e.getMessage());
      System.out.println(args.helpText());
    }
  }

  @Override
  public void start(Stage primaryStage) {
    StackPane root = new StackPane(new Label("Hello World"));
    Scene scene = new Scene(root, 800, 800);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
