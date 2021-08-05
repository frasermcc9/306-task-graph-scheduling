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

package com.jacketing.parsing;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.jacketing.common.Loader;
import com.jacketing.parsing.impl.Parser;
import com.jacketing.parsing.impl.services.EnumerationService;
import com.jacketing.parsing.impl.services.WeightService;
import com.jacketing.parsing.impl.structures.EnumeratedAdjacencyList;
import com.jacketing.parsing.impl.structures.Graph;
import com.jacketing.parsing.interfaces.structures.services.EnumeratedNodeMap;
import com.jacketing.util.MapFactory;
import com.paypal.digraph.parser.GraphParser;

public class ParserLoader implements Loader<Graph> {

  private final MapFactory mapFactory;
  private final String graphPath;

  private ParserLoader(String graphPath, MapFactory factory) {
    this.mapFactory = factory;
    this.graphPath = graphPath;
  }

  public static Loader<Graph> create(String graphPath, MapFactory factory) {
    return new ParserLoader(graphPath, factory);
  }

  public Graph load() {
    try {
      Parser parser = Parser.fromFile(graphPath);
      GraphParser graph = parser.parse();
      return preprocess(graph);
    } catch (Exception e) {
      return null;
    }
  }

  private Graph preprocess(GraphParser graph) {
    EnumerationService enumerationService = new EnumerationService(
      Maps.synchronizedBiMap(HashBiMap.create())
    );
    EnumeratedNodeMap enumeratedNodeMap = enumerationService.enumerateFromGraph(
      graph
    );
    return new Graph(
      new EnumeratedAdjacencyList(
        graph,
        enumeratedNodeMap,
        mapFactory.createMap(),
        mapFactory.createMap()
      ),
      new WeightService(
        graph,
        enumeratedNodeMap,
        mapFactory.createMap(),
        mapFactory.createMap()
      ),
      graph.getGraphId()
    );
  }
}
