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

package com.jacketing.algorithm.interfaces.util;

import com.jacketing.algorithm.impl.util.ScheduleFactoryImpl;
import com.jacketing.algorithm.interfaces.structures.Schedule;
import com.jacketing.io.cli.ProgramContext;

public interface ScheduleFactory {
  static ScheduleFactory create() {
    return new ScheduleFactoryImpl();
  }

  Schedule newSchedule(ProgramContext ctx);
}
