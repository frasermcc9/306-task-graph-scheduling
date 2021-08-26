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

package com.jacketing.common.analysis;

public enum AlgorithmEvent {
  /**
   * Fired when the algorithm finds a new best-schedule. This means the best
   * schedule will be updated, as well as the total number of successful
   * schedules found.
   */
  BEST_UPDATE,
  /**
   * Fired when the algorithm checks a new schedule (partial).
   */
  SCHEDULE_CHECK,
  /**
   * Fired when the algorithm checks a new complete schedule.
   */
  FULL_SCHEDULE_CHECK,
  /**
   * Fired whenever the algorithm determines that it will stop going down a path
   * due to it being impossible to be shorter than the current best.
   */
  CULLED_SCHEDULE,
  /**
   * Fired when the algorithm finds a schedule that would be equivalent to one
   * already searched.
   */
  DUPLICATE_SCHEDULE,
  /**
   * Fired when the graph is first loaded in memory
   */
  GRAPH_LOADED,
}
