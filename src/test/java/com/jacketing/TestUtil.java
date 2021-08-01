package com.jacketing;

import com.google.common.collect.HashBiMap;
import com.jacketing.parsing.impl.Parser;
import com.jacketing.parsing.impl.services.EnumerationService;
import com.jacketing.parsing.impl.services.WeightService;
import com.jacketing.parsing.impl.structures.EnumeratedAdjacencyList;
import com.jacketing.parsing.impl.structures.Graph;
import com.jacketing.parsing.interfaces.structures.services.EnumeratedNodeMap;
import com.paypal.digraph.parser.GraphParser;
import java.util.HashMap;

public class TestUtil {

  public static Graph graphVariantOne() {
    StringBuffer graphString = new StringBuffer(
      "digraph \"g1\" {\n" +
      "  a [Weight = 2];\n" +
      "  b [Weight = 3];\n" +
      "  a -> b [Weight = 1];\n" +
      "  c [Weight = 3];\n" +
      "  a -> c [Weight = 2];\n" +
      "  d [Weight = 2];\n" +
      "  b -> d [Weight = 2];\n" +
      "  c -> d [Weight = 1];\n" +
      "}"
    );

    return createGraphFromBuffer(graphString);
  }

  public static Graph graphVariantTwo() {
    StringBuffer graphString = new StringBuffer(
      "digraph \"g2\" {\n" +
      "\t0\t [Weight=5];\n" +
      "\t1\t [Weight=6];\n" +
      "\t0 -> 1\t [Weight=15];\n" +
      "\t2\t [Weight=5];\n" +
      "\t0 -> 2\t [Weight=11];\n" +
      "\t3\t [Weight=6];\n" +
      "\t0 -> 3\t [Weight=11];\n" +
      "\t4\t [Weight=4];\n" +
      "\t1 -> 4\t [Weight=19];\n" +
      "\t5\t [Weight=7];\n" +
      "\t1 -> 5\t [Weight=4];\n" +
      "\t6\t [Weight=7];\n" +
      "\t1 -> 6\t [Weight=21];\n" +
      "}\n"
    );

    return createGraphFromBuffer(graphString);
  }

  public static Graph graphVariantThree() {
    return createGraphFromFile("examples/g3/g3.dot");
  }

  private static Graph createGraphFromBuffer(StringBuffer graphString) {
    try {
      Parser parser = Parser.fromStringBuffer(graphString);
      GraphParser graph = parser.parse();
      return createGraph(graph);
    } catch (Exception e) {
      return null;
    }
  }

  private static Graph createGraphFromFile(String path) {
    try {
      Parser parser = Parser.fromFile(path);
      GraphParser graph = parser.parse();
      return createGraph(graph);
    } catch (Exception e) {
      return null;
    }
  }

  private static Graph createGraph(GraphParser graph) {
    EnumerationService enumerationService = new EnumerationService(
      HashBiMap.create()
    );
    EnumeratedNodeMap enumeratedNodeMap = enumerationService.enumerateFromGraph(
      graph
    );
    return new Graph(
      new EnumeratedAdjacencyList(
        graph,
        enumeratedNodeMap,
        new HashMap<>(),
        new HashMap<>()
      ),
      new WeightService(
        graph,
        enumeratedNodeMap,
        new HashMap<>(),
        new HashMap<>()
      )
    );
  }
}
