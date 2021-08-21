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

import com.jacketing.algorithm.structures.ScheduleV1;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AlgorithmObserver implements Observer {

  private final Map<AlgorithmEvent, List<AlgorithmUpdateHandler>> observers = new HashMap<>();
  private final AtomicInteger totalSchedulesChecked = new AtomicInteger();
  private final AtomicInteger totalFullSchedulesChecked = new AtomicInteger();
  private final AtomicInteger culledScheduleCount = new AtomicInteger();
  private final AtomicInteger improvementsFoundCount = new AtomicInteger();
  private final AtomicInteger duplicateSchedules = new AtomicInteger();
  private ScheduleV1 currentBestSchedule;
  private Graph graph;

  @Override
  public void on(AlgorithmEvent event, AlgorithmUpdateHandler updateHandler) {
    this.observers.putIfAbsent(event, new ArrayList<>());
    this.observers.get(event).add(updateHandler);
  }

  private void update(AlgorithmEvent event) {
    this.observers.putIfAbsent(event, new ArrayList<>());
    this.observers.get(event).forEach(e -> e.update(this));
  }

  @Override
  public synchronized void updateBestSchedule(ScheduleV1 schedule) {
    this.currentBestSchedule = schedule;
    improvementsFoundCount.getAndIncrement();
    update(AlgorithmEvent.BEST_UPDATE);
  }

  @Override
  public void incrementCheckedSchedules() {
    totalSchedulesChecked.getAndIncrement();
    update(AlgorithmEvent.SCHEDULE_CHECK);
  }

  @Override
  public void incrementFullSchedulesChecked() {
    totalFullSchedulesChecked.getAndIncrement();
    update(AlgorithmEvent.FULL_SCHEDULE_CHECK);
  }

  @Override
  public void incrementCulledSchedules() {
    culledScheduleCount.getAndIncrement();
    update(AlgorithmEvent.CULLED_SCHEDULE);
  }

  @Override
  public void incrementDuplicateSchedules() {
    duplicateSchedules.getAndIncrement();
    update(AlgorithmEvent.DUPLICATE_SCHEDULE);
  }

  @Override
  public int getDuplicateSchedules() {
    return duplicateSchedules.get();
  }

  @Override
  public ScheduleV1 getCurrentBestSchedule() {
    return currentBestSchedule;
  }

  @Override
  public int getCheckedSchedules() {
    return totalSchedulesChecked.get();
  }

  @Override
  public int getFullSchedulesChecked() {
    return totalFullSchedulesChecked.get();
  }

  @Override
  public int getCulledSchedules() {
    return culledScheduleCount.get();
  }

  @Override
  public int getImprovementsFound() {
    return improvementsFoundCount.get();
  }

  public void setGraph(Graph graph) {
    this.graph = graph;
  }

  public Graph getGraph() {
    return graph;
  }

  public boolean hasGraph() {
    return this.graph != null;
  }
}
