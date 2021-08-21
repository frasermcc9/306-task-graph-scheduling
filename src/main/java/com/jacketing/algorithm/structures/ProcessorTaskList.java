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

package com.jacketing.algorithm.structures;

import java.util.ArrayList;
import java.util.List;

public class ProcessorTaskList extends ArrayList<Task> {

  public ProcessorTaskList() {
    super();
  }

  public ProcessorTaskList(List<Task> tasks) {
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
