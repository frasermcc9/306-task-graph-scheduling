package com.jacketing.io.output.commandOut;

import com.jacketing.algorithm.algorithms.common.AlgorithmSchedule;
import com.jacketing.io.cli.ApplicationContext;

public class CommandLineOutput implements CommandOutput {

  @Override
  public void printCmdOutput(ApplicationContext context) {
    System.out.println(context.toString());
  }

  public void printStartOfSearch() {
    System.out.println("Starting search...");
  }

  public void printEndOfSearch(Double time) {
    String outTime = time.toString() + "ns";
    if (time > 1000000000) {
      outTime = String.format("%,f seconds", time / 1000000000);
    } else if (time > 1000000) {
      outTime = String.format("%,f milliseconds", time / 1000000);
    }
    System.out.println("Finished\n" + "Time taken: " + outTime);
  }

  public void printScheduleTime(AlgorithmSchedule schedule) {
    System.out.println("Schedule Cost: " + schedule.getDuration());
  }
}
