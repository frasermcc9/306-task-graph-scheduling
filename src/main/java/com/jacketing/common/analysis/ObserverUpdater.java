package com.jacketing.common.analysis;

@FunctionalInterface
public interface ObserverUpdater {
  void updateObserver(UpdatesFromAlgorithm update);
}
