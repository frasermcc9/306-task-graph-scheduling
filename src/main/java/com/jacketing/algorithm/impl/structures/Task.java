package com.jacketing.algorithm.impl.structures;

public class Task {

  private final int start;
  private final int duration;
  private final int id;

  public Task(int start, int duration, int id) {
    this.start = start;
    this.duration = duration;
    this.id = id;
  }

  public int getEndTime() {
    return start + duration;
  }

  public int getStart() {
    return start;
  }

  public int getDuration() {
    return duration;
  }

  public int getId() {
    return id;
  }
}
