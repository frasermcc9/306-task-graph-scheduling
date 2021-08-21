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

import com.jacketing.common.factories.SetFactory;
import com.jacketing.io.cli.AlgorithmContext;
import com.jacketing.parsing.impl.structures.Graph;

public class ArrayDuplicationStaticCache extends AbstractStaticCache {

  public ArrayDuplicationStaticCache(
    Graph g,
    AlgorithmContext ctx,
    SetFactory<PermutationId> factory
  ) {
    super(g, ctx, factory);
  }

  @Override
  public PermutationId defaultPermutationId() {
    int nodeCount = getGraph().getAdjacencyList().getNodeCount();
    int[] defaultIdentifier = new int[nodeCount];
    for (int i = 0; i < nodeCount; i++) {
      defaultIdentifier[i] = -1;
    }
    return new ArrayPermutationId(defaultIdentifier);
  }
}
