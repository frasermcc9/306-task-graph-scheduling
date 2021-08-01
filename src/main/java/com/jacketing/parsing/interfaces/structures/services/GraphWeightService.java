package com.jacketing.parsing.interfaces.structures.services;

public interface GraphWeightService {
  void formWeights();

  int nodeWeight(int enumeratedNode);

  EdgeWeightFrom edgeWeight();

  interface EdgeWeightFrom {
    EdgeWeightTo from(int source);
  }

  interface EdgeWeightTo {
    int to(int target);
  }
}
