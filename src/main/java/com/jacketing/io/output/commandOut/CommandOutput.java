package com.jacketing.io.output.commandOut;

import com.jacketing.algorithm.interfaces.structures.Schedule;
import com.jacketing.io.cli.ApplicationContext;

public interface CommandOutput {
  void printCmdOutput(ApplicationContext context);

  void printStartOfSearch();

  void printEndOfSearch(Double time);

  void printScheduleTime(Schedule schedule);
}
