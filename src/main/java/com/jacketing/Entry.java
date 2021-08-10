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

package com.jacketing;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.jacketing.algorithm.AlgorithmLoader;
import com.jacketing.algorithm.impl.algorithms.DepthFirstScheduler;
import com.jacketing.algorithm.impl.algorithms.ParallelDepthFirstScheduler;
import com.jacketing.algorithm.interfaces.structures.Schedule;
import com.jacketing.common.Loader;
import com.jacketing.common.analysis.AlgorithmObserver;
import com.jacketing.io.cli.ApplicationContext;
import com.jacketing.io.cli.ProgramContext;
import com.jacketing.io.output.OutputLoader;
import com.jacketing.io.output.commandOut.CommandLineOutput;
import com.jacketing.io.output.format.DotFileFormatter;
import com.jacketing.io.output.saver.StandardFileSaver;
import com.jacketing.parsing.ParserLoader;
import com.jacketing.parsing.impl.structures.Graph;
import com.jacketing.view.ApplicationEntry;
import java.util.HashMap;
import javafx.application.Application;

public class Entry {

  public static void main(String... argv) {
    ApplicationContext programContext = new ProgramContext();

    try {
      JCommander.newBuilder().addObject(programContext).build().parse(argv);
      programContext.validate();

      if (programContext.isVisualized()) {
        programContext.giveObserver(new AlgorithmObserver());

        Application.launch(ApplicationEntry.class);
      }
      beginSearch(programContext);
    } catch (ParameterException e) {
      System.out.println(e.getMessage());
      System.out.println(programContext.helpText());
    }
  }

  public static void beginSearch(ApplicationContext context) {
    CommandLineOutput out = new CommandLineOutput();
    out.printCmdOutput(context);
    Loader<Graph> graphLoader = ParserLoader.create(
      context.getInputFile(),
      HashMap::new
    );
    Graph graph = graphLoader.load();

    Loader<Schedule> scheduleLoader = AlgorithmLoader.create(
      graph,
      context,
      (data, ctx, scheduleFactory) -> {
        if (ctx.getCoresToCalculateWith() <= 1) return new DepthFirstScheduler(
          data,
          ctx,
          scheduleFactory
        );
        return new ParallelDepthFirstScheduler(data, ctx, scheduleFactory);
      }
    );
    Schedule schedule = scheduleLoader.load();

    Loader<Void> outputLoader = OutputLoader.create(
      schedule,
      context,
      graph,
      StandardFileSaver::new,
      DotFileFormatter::new
    );
    outputLoader.load();
  }
}
