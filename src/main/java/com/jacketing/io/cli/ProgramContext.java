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

package com.jacketing.io.cli;

import static org.fusesource.jansi.Ansi.ansi;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.validators.PositiveInteger;
import com.jacketing.common.analysis.Observer;
import com.jacketing.common.analysis.UpdatesFromAlgorithm;
import java.util.ArrayList;
import java.util.List;

public class ProgramContext implements ApplicationContext {

  @Parameter
  private List<String> input = new ArrayList<>();

  @Parameter(names = { "-p" }, validateWith = PositiveInteger.class)
  private int coresToCalculateWith;

  @Parameter(names = { "-v" })
  private boolean visualize;

  @Parameter(names = { "-o" })
  private String outputName;

  @Parameter(names = { "--no-output" })
  private boolean noOutput;

  @Parameter(names = { "--fail-suboptimal" })
  private boolean failSuboptimal;

  @Parameter(names = { "--Ofast-off" })
  private boolean disablePog;

  private Observer algorithmObserver;

  public ProgramContext(List<String> input) {
    this.input = input;
  }

  public ProgramContext() {}

  @Override
  public boolean failSuboptimal() {
    return failSuboptimal;
  }

  @Override
  public String getInputFile() {
    return input.get(0);
  }

  @Override
  public int getProcessorsToScheduleOn() {
    return Integer.parseInt(input.get(1));
  }

  @Override
  public boolean isDisablePog() {
    return disablePog;
  }

  @Override
  public int getCoresToCalculateWith() {
    return coresToCalculateWith;
  }

  @Override
  public UpdatesFromAlgorithm getObserver() {
    return this.algorithmObserver;
  }

  @Override
  public boolean isVisualized() {
    return visualize;
  }

  @Override
  public String getOutputName() {
    if (outputName != null) {
      return outputName;
    } else {
      String input = getInputFile();
      input = input.substring(0, input.length() - 4);
      return input + "-output.dot";
    }
  }

  @Override
  public boolean outputIsDisabled() {
    return this.noOutput;
  }

  @Override
  public void validate() throws IllegalArgumentException {
    if (input.size() != 2) {
      throw new ParameterException(
        "2 arguments expected, received " + input.size()
      );
    }

    if (!input.get(0).endsWith(".dot")) {
      throw new ParameterException(
        ".dot file expected as first parameter, received " + input.get(0)
      );
    }

    try {
      Integer.parseInt(input.get(1));
    } catch (NumberFormatException e) {
      throw new ParameterException(
        "Integer expected as second parameter, received " + input.get(1)
      );
    }
  }

  @Override
  public String helpText() {
    String output = "";
    output += "java -jar scheduler.jar INPUT.dot P [OPTIONS]\n";
    output += "INPUT.dot  a task graph with integer weights in dot format\n";
    output +=
      "P          number of processors to schedule the INPUT graph on\n";
    output += "\n";
    output += "Optional:\n";
    output += "-p N       use N cores for execution (default sequential)\n";
    output += "-v         visualize the search\n";
    output +=
      "-o OUTPUT  output file is named OUTPUT (default INPUT-output.dot)\n";

    return output;
  }

  @Override
  public void giveObserver(Observer observer) {
    this.algorithmObserver = observer;
  }

  public String toString() {
    if (input.size() != 2) {
      return "";
    }

    int useCores = coresToCalculateWith == 0 ? 1 : coresToCalculateWith;

    String vizColor = visualize ? "green " : "red ";

    String outName = outputName == null
      ? getInputFile() + "-output"
      : outputName;
    String outText = outputIsDisabled()
      ? "@|red Output will not be saved|@"
      : "Output will be saved to: @|yellow " + outName + "|@";

    return (
      ansi()
        .render(
          "------------------------------------ \n" +
          "Input file: " +
          "@|cyan " +
          getInputFile() +
          "|@" +
          "\n" +
          "Processors to schedule on: " +
          "@|cyan " +
          getProcessorsToScheduleOn() +
          "|@" +
          "\n" +
          "Threads to calculate with: " +
          "@|cyan " +
          useCores +
          "|@ " +
          "\n" +
          "Visualize: " +
          "@|" +
          vizColor +
          visualize +
          "|@" +
          "\n" +
          outText +
          "\n" +
          "------------------------------------"
        )
    ).toString();
  }
}
