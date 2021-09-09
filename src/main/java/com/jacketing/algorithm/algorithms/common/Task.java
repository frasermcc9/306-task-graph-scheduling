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

package com.jacketing.algorithm.algorithms.common;

import com.jacketing.common.FormattableTask;

public class Task implements FormattableTask {

  private final int processor;
  private final int duration;
  private final int id;
  private int time;

  public Task(int processor, int startTime, int duration, int id) {
    this.processor = processor;
    this.time = startTime;
    this.duration = duration;
    this.id = id;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public int getStartTime() {
    return getTime();
  }

  @Override
  public void setStartTime(int startTime) {
    this.time = startTime;
  }

  public int getProcessor() {
    return processor;
  }

  public int getTime() {
    return time;
  }

  public int getDuration() {
    return duration;
  }
}
