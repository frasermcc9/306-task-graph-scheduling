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

package com.jacketing.io.cli;

import com.jacketing.common.analysis.UpdatesFromAlgorithm;

public interface AlgorithmContext {
  int getProcessorsToScheduleOn();

  boolean isDisablePog();

  int getCoresToCalculateWith();

  UpdatesFromAlgorithm getObserver();

  String toString();
}
