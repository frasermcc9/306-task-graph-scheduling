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

package com.jacketing.io.output;

import com.jacketing.algorithm.interfaces.structures.Schedule;
import com.jacketing.common.Loader;
import com.jacketing.io.cli.IOContext;
import com.jacketing.io.output.format.FormatterFactory;
import com.jacketing.io.output.saver.FileSaverFactory;
import com.jacketing.parsing.impl.structures.Graph;
import java.io.IOException;

public class OutputLoader implements Loader<Void> {

  private final Schedule schedule;
  private final IOContext ioContext;
  private final FileSaverFactory fileSaverFactory;
  private final FormatterFactory formatterFactory;
  private final Graph graph;

  private OutputLoader(
    Schedule schedule,
    IOContext ioContext,
    Graph graph,
    FileSaverFactory fileSaverFactory,
    FormatterFactory formatterFactory
  ) {
    this.schedule = schedule;
    this.ioContext = ioContext;
    this.fileSaverFactory = fileSaverFactory;
    this.formatterFactory = formatterFactory;
    this.graph = graph;
  }

  public static Loader<Void> create(
    final Schedule schedule,
    final IOContext ioContext,
    final Graph graph,
    final FileSaverFactory fileSaverFactory,
    final FormatterFactory formatterFactory
  ) {
    return new OutputLoader(
      schedule,
      ioContext,
      graph,
      fileSaverFactory,
      formatterFactory
    );
  }

  @Override
  public Void load() {
    String output = formatterFactory
      .getFormatter()
      .formatSchedule(schedule, graph);
    try {
      fileSaverFactory
        .getFileSaver()
        .saveFile(ioContext.getOutputName(), output);
    } catch (IOException e) {
      System.err.println("Error when writing file");
      e.printStackTrace();
    }

    return null;
  }
}
