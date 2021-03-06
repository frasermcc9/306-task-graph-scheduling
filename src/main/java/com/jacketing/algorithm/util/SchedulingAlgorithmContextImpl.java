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

import com.jacketing.algorithm.AlgorithmFactory;
import com.jacketing.algorithm.algorithms.SchedulingAlgorithmStrategy;
import com.jacketing.algorithm.algorithms.common.AlgorithmSchedule;
import com.jacketing.common.analysis.UpdatesFromAlgorithm;

public class SchedulingAlgorithmContextImpl
  implements SchedulingAlgorithmStrategy {

  SchedulingAlgorithmStrategy algorithm;

  public SchedulingAlgorithmContextImpl(SchedulingAlgorithmStrategy algorithm) {
    this.algorithm = algorithm;
  }

  @Override
  public AlgorithmSchedule schedule() {
    return algorithm.schedule();
  }

  @Override
  public SchedulingAlgorithmStrategy withObservable(
    UpdatesFromAlgorithm updater
  ) {
    return algorithm.withObservable(updater);
  }

  @Override
  public SchedulingAlgorithmStrategy withEstimateAlgorithm(
    AlgorithmFactory factory
  ) {
    return algorithm.withEstimateAlgorithm(factory);
  }
}
