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

package com.jacketing.algorithm.util;

import com.jacketing.algorithm.structures.ProcessorTaskList;
import com.jacketing.algorithm.structures.ScheduleFactory;
import com.jacketing.algorithm.structures.ScheduleV1;
import com.jacketing.algorithm.structures.ScheduleV1Impl;
import com.jacketing.io.cli.AlgorithmContext;
import java.util.HashMap;
import java.util.Map;

public class ScheduleFactoryImpl implements ScheduleFactory {

  public ScheduleV1 newSchedule(AlgorithmContext ctx) {
    Map<Integer, ProcessorTaskList> map = new HashMap<>();
    for (int i = 0; i < ctx.getProcessorsToScheduleOn(); i++) {
      map.put(i, new ProcessorTaskList());
    }

    return new ScheduleV1Impl(ctx, map, new HashMap<>());
  }

  @Override
  public ScheduleV1 copy(ScheduleV1 schedule) {
    return new ScheduleV1Impl((ScheduleV1Impl) schedule);
  }
}
