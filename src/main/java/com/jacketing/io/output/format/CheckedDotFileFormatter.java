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

package com.jacketing.io.output.format;

import com.jacketing.algorithm.interfaces.structures.Schedule;
import com.jacketing.algorithm.validation.ScheduleValidationService;
import com.jacketing.common.errors.ValidationException;
import com.jacketing.parsing.impl.structures.Graph;

public class CheckedDotFileFormatter implements Formatter {

  @Override
  public String formatSchedule(Schedule schedule, Graph graph) {
    boolean isValid = new ScheduleValidationService()
      .validateSchedule(schedule, graph, schedule.getNumberOfProcessors());

    if (!isValid) {
      throw new ValidationException("Attempted to format invalid schedule");
    }

    return new DotFileFormatter().formatSchedule(schedule, graph);
  }
}
