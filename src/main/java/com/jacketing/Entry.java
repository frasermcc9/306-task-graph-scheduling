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
import com.jacketing.algorithm.algorithms.DepthFirstScheduler;
import com.jacketing.algorithm.algorithms.astar.ParallelAStar;
import com.jacketing.algorithm.algorithms.common.AlgorithmSchedule;
import com.jacketing.algorithm.algorithms.deprecated.ParallelDepthFirstScheduler;
import com.jacketing.algorithm.algorithms.smart.SmartAlgorithm;
import com.jacketing.common.Loader;
import com.jacketing.common.analysis.AlgorithmObserver;
import com.jacketing.common.log.Log;
import com.jacketing.common.log.LogLevel;
import com.jacketing.io.cli.ApplicationContext;
import com.jacketing.io.cli.ProgramContext;
import com.jacketing.io.output.OutputLoader;
import com.jacketing.io.output.commandOut.CommandLineOutput;
import com.jacketing.io.output.commandOut.CommandOutput;
import com.jacketing.io.output.commandOut.CommandOutputFactory;
import com.jacketing.io.output.format.DotFileFormatter;
import com.jacketing.io.output.saver.StandardFileSaver;
import com.jacketing.parsing.ParserLoader;
import com.jacketing.parsing.impl.structures.Graph;
import com.jacketing.view.ApplicationEntry;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import org.fusesource.jansi.AnsiConsole;

public class Entry {

  public static AlgorithmObserver observer;

  public static void main(String... argv) {
    ApplicationContext programContext = new ProgramContext();

    try {
      JCommander.newBuilder().addObject(programContext).build().parse(argv);
      programContext.validate();

      AnsiConsole.systemInstall();
      Log.loggingLevel(LogLevel.DEBUG);

      if (programContext.isVisualized()) {
        observer = new AlgorithmObserver();
        programContext.giveObserver(observer);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(os));

        Thread algorithmThread = new Thread(
          () -> {
            handledSearch(programContext, CommandLineOutput::new);
          }
        );

        try {
          ApplicationEntry.launch(observer, os, programContext, algorithmThread);
        } catch (Error e) {
          System.setOut(originalOut);
          Log.error(
            "JavaFX Not found, please use Oracle JDK 1.8 when using the visualization."
          );
        }

      } else {
        handledSearch(programContext, CommandLineOutput::new);
      }
    } catch (ParameterException e) {
      System.out.println(e.getMessage());
      System.out.println(programContext.helpText());
    }
  }

  public static void handledSearch(
    ApplicationContext context,
    CommandOutputFactory commandOutputFactory
  ) {
    try {
      beginSearch(context, commandOutputFactory);
    } catch (Exception e) {
      Log.error(
        "Jacketing Studio Encountered an unrecoverable error. " +
        "Please check the inputs and try again."
      );
      e.printStackTrace();
    }
  }

  public static void beginSearch(
    ApplicationContext context,
    CommandOutputFactory commandOutputFactory
  ) {
    //print general context info
    CommandOutput out = commandOutputFactory.create();
    out.printCmdOutput(context);

    Loader<Graph> graphLoader = ParserLoader.create(
      context.getInputFile(),
      HashMap::new
    );
    Graph graph = graphLoader.load();

    if (observer != null) {
      observer.setGraph(graph);
    }

    Loader<AlgorithmSchedule> scheduleLoader;

    if (context.isVisualized()) {
      if (context.getCoresToCalculateWith() <= 1) {
        scheduleLoader =
          AlgorithmLoader.create(graph, context, DepthFirstScheduler::new);
      } else {
        scheduleLoader =
          AlgorithmLoader.create(
            graph,
            context,
            ParallelDepthFirstScheduler::new
          );
      }
    } else if (context.getCoresToCalculateWith() <= 1) {
      scheduleLoader =
        AlgorithmLoader.create(graph, context, SmartAlgorithm::new);
    } else scheduleLoader =
      AlgorithmLoader.create(graph, context, ParallelAStar::new);

    //print start of search
    out.printStartOfSearch();
    double startTime = System.nanoTime();

    //Start search
    AlgorithmSchedule schedule = scheduleLoader.load();
    if (observer != null) {
      observer.setFinished();
    }

    int duration = schedule.getDuration();
    int optimalLength = graph.getOptimalLength();

    if (context.failSuboptimal() && optimalLength != -1) {
      if (duration != optimalLength) {
        Log.error("Expected length " + optimalLength + ", got " + duration);
        System.exit(1);
      }
    }

    double endTime = System.nanoTime();
    double timeElapsed = endTime - startTime;

    //print end of search
    out.printEndOfSearch(timeElapsed);

    if (!context.outputIsDisabled()) {
      Loader<Void> outputLoader = OutputLoader.create(
        schedule,
        context,
        graph,
        StandardFileSaver::new,
        DotFileFormatter::new
      );
      outputLoader.load();
    }

    out.printScheduleTime(schedule);
  }
}
