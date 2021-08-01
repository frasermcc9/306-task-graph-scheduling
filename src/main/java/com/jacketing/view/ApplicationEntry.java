package com.jacketing.view;

import com.jacketing.Entry;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ApplicationEntry extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(
      getClass().getClassLoader().getResource("mockup.fxml")
    );
    Parent root;

    root = loader.load();
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
    new Thread(Entry::beginSearch).start();
  }
}
