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

package com.jacketing.io.output.format;

import com.jacketing.algorithm.impl.structures.Task;
import com.jacketing.algorithm.interfaces.structures.Schedule;
import com.jacketing.parsing.impl.structures.Graph;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public class DotFileFormatter implements Formatter {

  @Override
  public String formatSchedule(Schedule schedule, Graph graph) {
    StringBuilder builder = new StringBuilder();
    builder
      .append("digraph ")
      .append(graph.getName())
      .append(" {")
      .append("\n");

    List<Task> allTasks = schedule.getAllTasks();

    for (Task task : allTasks) {
      int id = task.getId();

      int start = task.getStartTime();
      int processor = schedule.getProcessor(id);
      int weight = graph.getNodeWeight(id);
      String original = graph.translate(id);

      String attributes = new NodeGen(original, weight, start, processor)
        .toString();

      builder.append("\t").append(attributes);
    }

    Map<Integer, List<Integer>> outAdjacencyList = graph
      .getAdjacencyList()
      .getOutAdjacencyList();

    outAdjacencyList.forEach(
      (parent, children) ->
        children.forEach(
          child ->
            builder
              .append("\t")
              .append(
                new EdgeGen(
                  graph.getEdgeWeight().from(parent).to(child),
                  graph.translate(parent),
                  graph.translate(child)
                )
                  .toString()
              )
        )
    );

    builder.append("}");

    return builder.toString();
  }

  public static class EdgeGen {

    private final int weight;
    private final String src;
    private final String target;

    public EdgeGen(int weight, String src, String target) {
      this.weight = weight;
      this.src = src;
      this.target = target;
    }

    @Override
    public String toString() {
      return MessageFormat.format(
        "{0} -> {1} [Weight={2}];\n",
        src,
        target,
        weight
      );
    }
  }

  public static class NodeGen {

    private final int weight;
    private final int start;
    private final int proc;

    private final String id;

    public NodeGen(String id, int weight, int start, int proc) {
      this.weight = weight;
      this.start = start;
      this.proc = proc;
      this.id = id;
    }

    @Override
    public String toString() {
      return MessageFormat.format(
        "{0} [Weight={1}, Start={2}, Processor={3}];\n",
        id,
        weight,
        start,
        proc
      );
    }
  }
}
