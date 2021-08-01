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
