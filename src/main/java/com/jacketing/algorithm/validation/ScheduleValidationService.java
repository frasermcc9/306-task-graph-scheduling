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

package com.jacketing.algorithm.validation;

import com.jacketing.algorithm.structures.ScheduleV1;
import com.jacketing.algorithm.structures.Task;
import com.jacketing.common.FormattableTask;
import com.jacketing.parsing.impl.structures.EnumeratedAdjacencyList;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScheduleValidationService implements ScheduleValidator {

  private boolean validateTaskDuration(ScheduleV1 schedule, Graph graph) {
    return true;
  }

  private boolean validateTasksInOrder(ScheduleV1 schedule, Graph graph) {
    EnumeratedAdjacencyList enumeratedAdjacencyList = graph.getAdjacencyList();
    Map<Integer, List<Integer>> childToParentsMap = enumeratedAdjacencyList.getInAdjacencyList();

    for (FormattableTask childTask : schedule.getAllTasks()) {
      int childId = childTask.getId();
      List<Integer> parentNodeIds = childToParentsMap.get(childId);

      for (Integer parentNodeId : parentNodeIds) {
        Task parentTask = schedule.getTaskForNode(parentNodeId);

        if (parentTask == null) continue;

        int difference = childTask.getStartTime() - parentTask.getEndTime();

        if (difference < 0) {
          return false;
        }

        int parentProcessor = schedule.getProcessor(parentTask.getId());
        int childProcessor = schedule.getProcessor(childTask.getId());

        if (parentProcessor != childProcessor) {
          int weightOfEdge = graph
            .getEdgeWeight()
            .from(parentTask.getId())
            .to(childTask.getId());

          if (weightOfEdge > difference) {
            return false;
          }
        }
      }
    }

    return true;
  }

  public boolean validateTasksUnique(ScheduleV1 schedule) {
    ArrayList<Integer> seenTasks = new ArrayList<>();
    for (FormattableTask task : schedule.getAllTasks()) {
      if (seenTasks.contains(task.getId())) {
        return false;
      } else {
        seenTasks.add(task.getId());
      }
    }
    return true;
  }

  @Override
  public boolean validateSchedule(
    ScheduleV1 schedule,
    Graph graph,
    int numberOfProcessors
  ) {
    if (schedule.getNumberOfProcessors() != numberOfProcessors) {
      return false;
    }

    if (
      schedule.getAllTasks().size() != graph.getAdjacencyList().getNodeCount()
    ) {
      return false;
    }

    if (!validateTasksUnique(schedule)) {
      return false;
    }

    if (!validateTaskDuration(schedule, graph)) {
      return false;
    }

    return validateTasksInOrder(schedule, graph);
  }
}
