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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Entry extends Application {

  private static Args args;

  public static void main(String... argv) {
    args = new Args();

    try {
      JCommander.newBuilder().addObject(args).build().parse(argv);
      args.validate();

      if (args.isVisualized()) {
        launch();
      } else {
        beginSearch();
      }
    } catch (ParameterException e) {
      System.out.println(e.getMessage());
      System.out.println(args.helpText());
    }
  }

  public static void beginSearch() {
    System.out.println("Starting search...");
  }

  @Override
  public void start(Stage primaryStage) {
    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mockup.fxml"));
    Parent root;

    try {
      root = loader.load();
      Scene scene = new Scene(root);
      primaryStage.setScene(scene);
      primaryStage.show();
      new Thread(Entry::beginSearch).start();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
