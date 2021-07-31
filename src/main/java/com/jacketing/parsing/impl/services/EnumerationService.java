package com.jacketing.parsing.impl.services;

import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import com.google.common.collect.BiMap;
import com.jacketing.parsing.interfaces.structures.services.EnumeratedNodeMap;
import com.jacketing.parsing.interfaces.structures.services.GraphEnumerationService;
import java.util.ArrayList;

public class EnumerationService implements GraphEnumerationService {

  private final BiMap<Integer, String> numeralToIdBiMap;
  private final BiMap<String, Integer> idToNumeralBiMap;

  public EnumerationService(BiMap<Integer, String> numeralToIdBiMap) {
    this.numeralToIdBiMap = numeralToIdBiMap;
    this.idToNumeralBiMap = numeralToIdBiMap.inverse();
  }

  public EnumeratedNodeMap enumerateFromGraph(Graph graph) {
    ArrayList<Node> nodes = graph.getNodes(true);
    int incrementer = 0;
    for (Node node : nodes) {
      numeralToIdBiMap.put(incrementer++, node.getId().getId());
    }

    return new EnumeratedNodeMap() {
      @Override
      public String getIdFromNumeral(int numeral) {
        return numeralToIdBiMap.get(numeral);
      }

      @Override
      public int getEnumerated(String id) {
        return idToNumeralBiMap.get(id);
      }
    };
  }
}
