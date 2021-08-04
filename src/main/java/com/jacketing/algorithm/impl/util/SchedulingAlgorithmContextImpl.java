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

package com.jacketing.algorithm.impl.util;

import com.jacketing.algorithm.interfaces.SchedulingAlgorithmStrategy;
import com.jacketing.algorithm.interfaces.structures.Schedule;

public class SchedulingAlgorithmContextImpl
  implements SchedulingAlgorithmStrategy {

  SchedulingAlgorithmStrategy algorithm;

  public SchedulingAlgorithmContextImpl(SchedulingAlgorithmStrategy algorithm) {
    this.algorithm = algorithm;
  }

  @Override
  public Schedule schedule() {
    return algorithm.schedule();
  }
}
