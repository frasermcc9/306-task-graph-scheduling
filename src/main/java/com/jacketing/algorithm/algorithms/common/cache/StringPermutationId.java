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

import org.jetbrains.annotations.Contract;

public class StringPermutationId implements PermutationId {

  private final String permutationId;

  public StringPermutationId(String permutationId) {
    this.permutationId = permutationId;
  }

  public StringPermutationId(StringBuilder permutationId) {
    this.permutationId = permutationId.toString();
  }

  @Override
  @Contract(pure = true)
  public PermutationId updatePermutation(
    int node,
    int startPosition,
    int processor
  ) {
    StringBuilder builder = new StringBuilder(permutationId);
    builder.setCharAt(node, (char) startPosition);
    return new StringPermutationId(builder);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StringPermutationId that = (StringPermutationId) o;
    return permutationId.equals(that.permutationId);
  }

  @Override
  public int hashCode() {
    return permutationId.hashCode();
  }
}
