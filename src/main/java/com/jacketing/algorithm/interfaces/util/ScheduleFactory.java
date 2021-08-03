package com.jacketing.algorithm.interfaces.util;

import com.jacketing.algorithm.impl.util.ScheduleFactoryImpl;
import com.jacketing.algorithm.interfaces.structures.Schedule;
import com.jacketing.io.cli.ProgramContext;

public interface ScheduleFactory {
  static ScheduleFactory create() {
    return new ScheduleFactoryImpl();
  }
  Schedule newSchedule(ProgramContext ctx);
  Schedule copy(Schedule schedule);
}
