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
