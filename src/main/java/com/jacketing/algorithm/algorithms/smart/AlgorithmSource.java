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

package com.jacketing.algorithm.algorithms.smart;

import com.jacketing.algorithm.AlgorithmFactory;
import com.jacketing.algorithm.algorithms.astar.AStar;
import com.jacketing.algorithm.algorithms.dfs.IterativeDfs;
import com.jacketing.algorithm.algorithms.suboptimal.IndependentScheduler;
import com.jacketing.algorithm.algorithms.suboptimal.ListScheduler;

public interface AlgorithmSource {
  default AlgorithmFactory getDfs() {
    return IterativeDfs::new;
  }

  default AlgorithmFactory getAStar() {
    return AStar::new;
  }

  default AlgorithmFactory getIndependent() {
    return IndependentScheduler::new;
  }

  default AlgorithmFactory getList() {
    return ListScheduler::new;
  }
}
