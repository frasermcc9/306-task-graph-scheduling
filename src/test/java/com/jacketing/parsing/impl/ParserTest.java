package com.jacketing.parsing.impl;

import static org.junit.Assert.assertEquals;

import com.paypal.digraph.parser.GraphEdge;
import com.paypal.digraph.parser.GraphNode;
import com.paypal.digraph.parser.GraphParser;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

  private GraphParser rawGraph;

  @Before
  public void setUp() throws Exception {
    Parser reader = Parser.fromFile("examples/g1/g1.dot");
    rawGraph = reader.parse();
  }

  @Test
  public void testParse() {
    Map<String, GraphNode> nodes = rawGraph.getNodes();
    Map<String, GraphEdge> edges = rawGraph.getEdges();

    assertEquals(4, nodes.size());
    assertEquals(4, edges.size());
  }

  @Test
  public void testNodeWeights() {
    Map<String, GraphNode> nodes = rawGraph.getNodes();
    int nodeWeight = Integer.parseInt(
      nodes.get("a").getAttribute("Weight").toString()
    );
    assertEquals(2, nodeWeight);
  }
}
