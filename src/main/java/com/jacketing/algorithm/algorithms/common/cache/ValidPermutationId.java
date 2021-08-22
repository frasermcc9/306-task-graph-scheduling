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

package com.jacketing.algorithm.algorithms.common.cache;

import java.util.Arrays;
import org.jetbrains.annotations.Contract;

public class ValidPermutationId implements PermutationId {

  private final String permutationId;
  private final int[] processorIdMap;

  public ValidPermutationId(String permutationId, int procCount) {
    this.permutationId = permutationId;
    this.processorIdMap = new int[procCount];
    Arrays.fill(processorIdMap, -1);
  }

  public ValidPermutationId(StringBuilder permutationId, int procCount) {
    this.permutationId = permutationId.toString();
    this.processorIdMap = new int[procCount];
    Arrays.fill(processorIdMap, -1);
  }

  public ValidPermutationId(StringBuilder permutationId, int[] procMap) {
    this.permutationId = permutationId.toString();
    this.processorIdMap = procMap;
  }

  @Override
  @Contract(pure = true)
  public PermutationId updatePermutation(
    int node,
    int startPosition,
    int processor
  ) {
    int normalized = processorIdMap[processor];
    if (normalized == -1) {
      int[] copy = new int[processorIdMap.length];
      System.arraycopy(processorIdMap, 0, copy, 0, processorIdMap.length);
      copy[processor] = node;

      StringBuilder builder = new StringBuilder(permutationId);
      builder.setCharAt(node, (char) startPosition);
      builder.setCharAt(node + 1, (char) copy[processor]);

      return new ValidPermutationId(builder, copy);
    }

    StringBuilder builder = new StringBuilder(permutationId);
    builder.setCharAt(node, (char) startPosition);
    builder.setCharAt(node + 1, (char) normalized);

    return new ValidPermutationId(builder, processorIdMap);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ValidPermutationId that = (ValidPermutationId) o;
    return permutationId.equals(that.permutationId);
  }

  @Override
  public int hashCode() {
    return permutationId.hashCode();
  }
}
