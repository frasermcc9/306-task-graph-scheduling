package com.jacketing.view.innercontrollers;

import com.jacketing.io.cli.ApplicationContext;
import com.jacketing.view.ApplicationEntry;
import java.io.IOException;
import java.io.OutputStream;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class LogsController extends OutputStream {

  private TextArea logs;

  public LogsController(TextArea logs) {
    this.logs = logs;
  }

  public void appendText(String valueOf) {
    Platform.runLater(() -> {
      logs.appendText(valueOf);
      String text = logs.getText();
      logs.clear();
      text = text.replaceAll("\\[\\d\\dm", "");
      text = text.replaceAll("\\[m", "");
      logs.appendText(text);
    });
  }

  @Override
  public void write(int b) throws IOException {
    appendText(String.valueOf((char) b));
  }
}
