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

package com.jacketing.common.analysis;

import com.jacketing.algorithm.interfaces.structures.Schedule;

public interface UpdatesFromAlgorithm {
  void updateBestSchedule(Schedule schedule);

  void incrementCheckedSchedules();

  void incrementFullSchedulesChecked();

  void incrementCulledSchedules();

  void incrementDuplicateSchedules();
}
