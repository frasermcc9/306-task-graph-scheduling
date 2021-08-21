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

package com.jacketing.algorithm.structures;

import com.jacketing.algorithm.util.ScheduleFactoryImpl;
import com.jacketing.io.cli.AlgorithmContext;

public interface ScheduleFactory {
  static ScheduleFactory create() {
    return new ScheduleFactoryImpl();
  }

  ScheduleV1 newSchedule(AlgorithmContext ctx);

  ScheduleV1 copy(ScheduleV1 schedule);
}
