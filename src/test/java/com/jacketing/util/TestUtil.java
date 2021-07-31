package com.jacketing.util;

import com.alexmerz.graphviz.Parser;
import com.google.common.collect.HashBiMap;
import com.jacketing.parsing.impl.services.EnumerationService;
import com.jacketing.parsing.impl.services.WeightService;
import com.jacketing.parsing.impl.structures.EnumeratedAdjacencyList;
import com.jacketing.parsing.impl.structures.Graph;
import com.jacketing.parsing.interfaces.ParsingStrategy;
import com.jacketing.parsing.interfaces.structures.services.EnumeratedNodeMap;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class TestUtil {

  public static Graph graphVariantOne() {
    Parser parser = new Parser();
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

    Optional<List<com.alexmerz.graphviz.objects.Graph>> graphs = ParsingStrategy
      .fromBuffer(parser, graphString)
      .parse()
      .getGraphs();

    if (graphs.isPresent()) {
      com.alexmerz.graphviz.objects.Graph graph = graphs.get().get(0);
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

    return null;
  }
}
