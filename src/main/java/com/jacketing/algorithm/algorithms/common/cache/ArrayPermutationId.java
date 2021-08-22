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

public class ArrayPermutationId implements PermutationId {

  private final int[] permutationId;

  public ArrayPermutationId(int[] permutationId) {
    this.permutationId = permutationId;
  }

  @Override
  public PermutationId updatePermutation(
    int node,
    int startPosition,
    int processor
  ) {
    int len = permutationId.length;
    int[] copy = new int[len];
    System.arraycopy(permutationId, 0, copy, 0, len);
    copy[node] = startPosition;
    return new ArrayPermutationId(copy);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ArrayPermutationId that = (ArrayPermutationId) o;
    return Arrays.equals(permutationId, that.permutationId);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(permutationId);
  }
}
