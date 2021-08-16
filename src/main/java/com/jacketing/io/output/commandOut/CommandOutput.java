package com.jacketing.io.output.commandOut;

import com.jacketing.algorithm.impl.X.AlgorithmSchedule;
import com.jacketing.io.cli.ApplicationContext;

public interface CommandOutput {
  void printCmdOutput(ApplicationContext context);

  void printStartOfSearch();

  void printEndOfSearch(Double time);

  void printScheduleTime(AlgorithmSchedule schedule);
}
