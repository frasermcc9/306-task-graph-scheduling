package com.jacketing.io.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.validators.PositiveInteger;
import java.util.ArrayList;
import java.util.List;

public class ProgramContext {

  @Parameter(description = "Input")
  private List<String> input = new ArrayList<>();

  @Parameter(
    names = { "-p" },
    description = "use N cores for execution in parallel",
    validateWith = PositiveInteger.class
  )
  private int coresToCalculateWith;

  @Parameter(names = { "-v" }, description = "visualize the search")
  private boolean visualize;

  @Parameter(
    names = { "-o" },
    description = "output file is named OUTPUT (default is INPUT-output.dot"
  )
  private String outputName;

  public String getInputFile() {
    return input.get(0);
  }

  public int getProcessorsToScheduleOn() {
    return Integer.parseInt(input.get(1));
  }

  public int getCoresToCalculateWith() {
    return coresToCalculateWith;
  }

  public boolean isVisualized() {
    return visualize;
  }

  public String getOutputName() {
    if (outputName != null) {
      return outputName;
    } else {
      String input = getInputFile();
      input = input.substring(0, input.length() - 4);
      return input + "-output.dot";
    }
  }

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

  public String helpText() {
    String output = "";
    output += "java -jar scheduler.jar INPUT.dot P [OPTIONS]\n";
    output += "INPUT.dot  a task graph with integer weights in dot format\n";
    output += "P          number of processors to schedule the INPUT graph on\n";
    output += "\n";
    output += "Optional:\n";
    output += "-p N       use N cores for execution (default sequential)\n";
    output += "-v         visualize the search\n";
    output += "-o OUTPUT  output file is named OUTPUT (default INPUT-output.dot)\n";

    return output;
  }
}
