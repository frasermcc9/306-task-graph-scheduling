package com.jacketing.algorithm.impl.structures;

import java.util.ArrayList;
import java.util.List;

public class TaskList extends ArrayList<Task> {

  public TaskList() {
    super();
  }

  public TaskList(List<Task> tasks) {
    super(tasks);
  }

  public int getLastScheduledEndTime() {
    if (this.isEmpty()) return 0;
    return this.get(this.size() - 1).getEndTime();
  }

  public Task getLastScheduledTask() {
    return this.get(this.size() - 1);
  }
}
