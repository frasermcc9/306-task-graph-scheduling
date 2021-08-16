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

package com.jacketing.algorithm.interfaces;

import com.jacketing.algorithm.AlgorithmFactory;
import com.jacketing.algorithm.impl.X.AlgorithmSchedule;
import com.jacketing.algorithm.impl.util.SchedulingAlgorithmContextImpl;
import com.jacketing.common.analysis.UpdatesFromAlgorithm;

public interface SchedulingAlgorithmStrategy {
  static SchedulingAlgorithmStrategy create(
    SchedulingAlgorithmStrategy algorithm
  ) {
    return new SchedulingAlgorithmContextImpl(algorithm);
  }

  AlgorithmSchedule schedule();

  SchedulingAlgorithmStrategy withObservable(UpdatesFromAlgorithm updater);

  SchedulingAlgorithmStrategy withEstimateAlgorithm(AlgorithmFactory factory);
}
