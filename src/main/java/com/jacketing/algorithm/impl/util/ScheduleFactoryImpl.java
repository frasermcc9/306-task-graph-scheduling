package com.jacketing.algorithm.impl.util;

import com.jacketing.algorithm.impl.structures.ScheduleImpl;
import com.jacketing.algorithm.interfaces.structures.Schedule;
import com.jacketing.algorithm.interfaces.util.ScheduleFactory;
import com.jacketing.io.cli.ProgramContext;
import java.util.HashMap;

public class ScheduleFactoryImpl implements ScheduleFactory {

  public Schedule newSchedule(ProgramContext ctx) {
    return new ScheduleImpl(ctx, new HashMap<>(), new HashMap<>());
  }

  @Override
  public Schedule copy(Schedule schedule) {
    return new ScheduleImpl((ScheduleImpl) schedule);
  }
}
