package com.jacketing.parsing.impl.structures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Graph;
import com.jacketing.parsing.interfaces.ParsingStrategy;
import com.jacketing.parsing.interfaces.structures.GraphRepresentation;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AdjacencyListTest {

  private Parser parser;
  private StringBuffer graphString;

  @Before
  public void setUp() throws Exception {
    parser = new Parser();
    graphString =
      new StringBuffer(
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
  }

  @After
  public void tearDown() throws Exception {}

  public Graph createGraph() throws ParseException {
    Optional<List<Graph>> graphs = ParsingStrategy
      .fromBuffer(parser, graphString)
      .parse()
      .getGraphs();
    if (graphs.isPresent()) {
      return graphs.get().get(0);
    }
    fail();
    return null;
  }

  @Test
  public void testGraphNodeMapping() throws ParseException {
    Graph graph = createGraph();
    AdjacencyList adjacencyList = GraphRepresentation.withAdjacencyList(graph);
    adjacencyList.createRepresentation();
    Map<String, GraphNode> idToNodeMap1 = adjacencyList.getIdToNodeMap();
    assertEquals(4, idToNodeMap1.size());
    assertEquals(2, idToNodeMap1.get("a").getProcessTime());
    assertEquals(3, idToNodeMap1.get("b").getProcessTime());
    assertEquals(3, idToNodeMap1.get("c").getProcessTime());
    assertEquals(2, idToNodeMap1.get("d").getProcessTime());
  }

  @Test
  public void testAdjacencyListCreation() throws ParseException {
    Graph graph = createGraph();
    AdjacencyList adjacencyList = GraphRepresentation.withAdjacencyList(graph);
    adjacencyList.createRepresentation();
    Map<String, GraphNode> idToNodeMap1 = adjacencyList.getIdToNodeMap();
    Map<GraphNode, List<Dependent>> adjacencyListIdToNodeMap = adjacencyList.getAdjacencyList();
    assertEquals(4, adjacencyListIdToNodeMap.size());
    assertEquals(2, adjacencyListIdToNodeMap.get(idToNodeMap1.get("a")).size());
    assertEquals(
      2,
      adjacencyListIdToNodeMap
        .get(idToNodeMap1.get("b"))
        .get(0)
        .getTransferTime()
    );
    assertEquals(
      1,
      adjacencyListIdToNodeMap
        .get(idToNodeMap1.get("c"))
        .get(0)
        .getTransferTime()
    );
  }
}
