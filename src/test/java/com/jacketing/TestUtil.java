package com.jacketing;

import com.google.common.base.Charsets;
import com.google.common.collect.HashBiMap;
import com.google.common.io.Resources;
import com.jacketing.parsing.impl.Parser;
import com.jacketing.parsing.impl.services.EnumerationService;
import com.jacketing.parsing.impl.services.WeightService;
import com.jacketing.parsing.impl.structures.EnumeratedAdjacencyList;
import com.jacketing.parsing.impl.structures.Graph;
import com.jacketing.parsing.interfaces.structures.services.EnumeratedNodeMap;
import com.paypal.digraph.parser.GraphParser;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

  public static Graph graphVariantFour() {
    return createGraphFromFile("examples/g4/g4.dot");
  }

  public static Graph graphVariantFive() {
    return createGraphFromFile("examples/g5/g5.dot");
  }

  public static Graph emptyGraph() {
    StringBuffer graphString = new StringBuffer(
      "digraph \"empty\" {\n" + "}\n"
    );
    return createGraphFromBuffer(graphString);
  }

  public static List<GraphResult> getGraphTestSuite() throws IOException {
    return getGraphTestSuite(-1);
  }

  public static List<GraphResult> getGraphTestSuite(int only)
    throws IOException {
    List<GraphResult> graphs = new ArrayList<>();

    String[] graphLocations = { "1.dot", "2.dot", "3.dot", "4.dot", "5.dot" };
    String[] twoCoreResult = loadResource("e2e/sln-2-core.txt", Charsets.UTF_8)
      .split("\n");
    String[] fourCoreResult = loadResource("e2e/sln-4-core.txt", Charsets.UTF_8)
      .split("\n");

    for (int i = 0; i < graphLocations.length; i++) {
      String fileName = graphLocations[i];

      int twoCore = Integer.parseInt(twoCoreResult[i]);
      int fourCore = Integer.parseInt(fourCoreResult[i]);
      String graph = loadResource("e2e/" + fileName, Charsets.UTF_8);

      Graph graphFromString = createGraphFromString(graph);

      GraphResult graphResult = new GraphResult(
        graphFromString,
        twoCore,
        fourCore
      );

      graphs.add(graphResult);
    }

    if (only != -1) {
      ArrayList<GraphResult> exclusiveList = new ArrayList<>();
      exclusiveList.add(graphs.get(only));
      return exclusiveList;
    }

    return graphs;
  }

  @SuppressWarnings("UnstableApiUsage")
  private static String loadResource(String path, Charset charset)
    throws IOException {
    URL resource = Resources.getResource(path);
    return Resources.toString(resource, charset);
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

  private static Graph createGraphFromString(String graphString) {
    try {
      Parser parser = Parser.fromString(graphString);
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

  public static class GraphResult {

    private final Graph g;
    private final int twoCoresResult;
    private final int fourCoresResult;

    public GraphResult(Graph g, int twoCoresResult, int fourCoresResult) {
      this.g = g;
      this.twoCoresResult = twoCoresResult;
      this.fourCoresResult = fourCoresResult;
    }

    public int getTwoCoresResult() {
      return twoCoresResult;
    }

    public int getFourCoresResult() {
      return fourCoresResult;
    }

    public Graph getGraph() {
      return g;
    }

    public boolean checkTwoCores(int time) {
      return time == twoCoresResult;
    }

    public boolean checkFourCores(int time) {
      return time == fourCoresResult;
    }
  }
}
