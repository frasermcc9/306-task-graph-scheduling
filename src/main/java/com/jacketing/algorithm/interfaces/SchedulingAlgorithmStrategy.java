package com.jacketing.algorithm.interfaces;

import com.jacketing.algorithm.impl.util.SchedulingAlgorithmContextImpl;
import com.jacketing.algorithm.interfaces.structures.Schedule;

public interface SchedulingAlgorithmStrategy {
  static SchedulingAlgorithmStrategy create(
    SchedulingAlgorithmStrategy algorithm
  ) {
    return new SchedulingAlgorithmContextImpl(algorithm);
  }

  Schedule schedule();
}
