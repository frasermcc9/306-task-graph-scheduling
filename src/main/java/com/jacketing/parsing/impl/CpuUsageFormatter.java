package com.jacketing.parsing.impl;

import java.util.*;
import java.util.stream.DoubleStream;

public class CpuUsageFormatter {

  /**
   * Formats a thread utilization map into an array of the 4 highest (unique) threads
   * @param utilization Map from CpuReader polling
   * @return Array of the most active threads in descending order
   */
  public static double[] format(Map<Long, Float> utilization) {
    int threads = 4;
    double[] output = new double[threads];

    Float[] values = utilization.values().toArray(new Float[0]);
    Arrays.sort(values, Collections.reverseOrder());

    int i = 0;
    for (double value : values) {
      if (i == threads) {
        break;
      }

      if (DoubleStream.of(output).noneMatch(x -> x == value)) {
        output[i] = value;
        i++;
      }
    }
    return output;
  }
}
