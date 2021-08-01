package com.jacketing.algorithm.impl.structures;

import java.util.ArrayList;

public class TaskList extends ArrayList<Task> {

  public int getLastScheduledEndTime() {
    if (this.isEmpty()) return 0;
    return this.get(this.size() - 1).getEndTime();
  }
}
