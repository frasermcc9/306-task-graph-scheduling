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

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.validators.PositiveInteger;
import com.jacketing.common.analysis.Observer;
import com.jacketing.common.analysis.UpdatesFromAlgorithm;
import java.util.ArrayList;
import java.util.List;

public class ProgramContext implements ApplicationContext {

  public ProgramContext(List<String> input) {
    this.input = input;
  }

  public ProgramContext() {}

  @Parameter
  private List<String> input = new ArrayList<>();

  @Parameter(names = { "-p" }, validateWith = PositiveInteger.class)
  private int coresToCalculateWith;

  @Parameter(names = { "-v" })
  private boolean visualize;

  @Parameter(names = { "-o" })
  private String outputName;

  private Observer algorithmObserver;

  @Override
  public String getInputFile() {
    return input.get(0);
  }

  @Override
  public int getProcessorsToScheduleOn() {
    return Integer.parseInt(input.get(1));
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

  @Override
  public String toString() {
    if (input.size() != 2) {
      return "";
    }
    if (coresToCalculateWith == 0 && outputName == null) {
      return (
        "------------------------------------ \n" +
        "Input file: " +
        getInputFile() +
        "\n" +
        "Processors to schedule on: " +
        getProcessorsToScheduleOn() +
        "\n" +
        "Threads to calculate with: 1\n" +
        "Visualize: " +
        visualize +
        "\n" +
        "Output will be saved to: " +
        getInputFile() +
        "-output\n" +
        "------------------------------------"
      );
    } else if (outputName == null) {
      return (
        "------------------------------------ \n" +
        "Input file: " +
        getInputFile() +
        "\n" +
        "Processors to schedule on: " +
        getProcessorsToScheduleOn() +
        "\n" +
        "Threads to calculate with: 1\n" +
        "Visualize: " +
        visualize +
        "\n" +
        "Output will be saved to: " +
        getInputFile() +
        "-output\n" +
        "------------------------------------"
      );
    } else if (coresToCalculateWith == 0) {
      return (
        "------------------------------------ \n" +
        "Input file: " +
        getInputFile() +
        "\n" +
        "Processors to schedule on: " +
        getProcessorsToScheduleOn() +
        "\n" +
        "Threads to calculate with: 1\n" +
        "Visualize: " +
        visualize +
        "\n" +
        "Output will be saved to: " +
        outputName +
        "\n" +
        "------------------------------------"
      );
    } else {
      return (
        "------------------------------------ \n" +
        "Input file: " +
        getInputFile() +
        "\n" +
        "Processors to schedule on: " +
        getProcessorsToScheduleOn() +
        "\n" +
        "Threads to calculate with: " +
        coresToCalculateWith +
        "\n" +
        "Visualize: " +
        visualize +
        "\n" +
        "Output will be saved to: " +
        outputName +
        "\n" +
        "------------------------------------"
      );
    }
  }
}
