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

package com.jacketing.algorithm.impl.structures;

public class Task {

  private final int id;
  private final int start;
  private final int duration;
  private final int processorNumber;

  public Task(int start, int duration, int id, int processorNumber) {
    this.start = start;
    this.duration = duration;
    this.id = id;
    this.processorNumber = processorNumber;
  }

  public int getEndTime() {
    return start + duration;
  }

  public int getStartTime() {
    return start;
  }

  public int getDuration() {
    return duration;
  }

  public int getId() {
    return id;
  }

  public int getProcessorNumber() {
    return processorNumber;
  }
}
