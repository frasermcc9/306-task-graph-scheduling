package com.jacketing.parsing.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Id;
import com.alexmerz.graphviz.objects.Node;
import com.jacketing.parsing.interfaces.ParsingStrategy;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Deprecated
public class AbstractGraphParserTest {

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

  public void checkGraphValidity(List<Graph> graphList) {
    assertEquals(1, graphList.size());
    Graph g = graphList.get(0);
    List<Node> nodes = g.getNodes(true);
    assertEquals(4, nodes.size());

    Id nodeId = new Id();
    nodeId.setId("a");
    Node nodeA = g.findNode(nodeId);
    assertEquals(nodeA.getAttribute("Weight"), "2");

    List<Edge> edges = g.getEdges();
    assertEquals(4, edges.size());
  }

  @Test
  public void testParseCreatesGraph() throws ParseException {
    Optional<List<Graph>> graphs = ParsingStrategy
      .fromBuffer(parser, graphString)
      .parse()
      .getGraphs();
    if (graphs.isPresent()) {
      checkGraphValidity(graphs.get());
      return;
    }
    fail();
  }

  @Test
  public void testParseFile() throws FileNotFoundException, ParseException {
    Reader fileReader = new FileReader(new File("examples/g1/g1.dot"));
    Optional<List<Graph>> graphs = ParsingStrategy
      .fromFile(parser, fileReader)
      .parse()
      .getGraphs();
    if (graphs.isPresent()) {
      checkGraphValidity(graphs.get());
      return;
    }
    fail();
  }

  @Test(expected = ParseException.class)
  public void testParseInvalidFile()
    throws FileNotFoundException, ParseException {
    Reader fileReader = new FileReader(
      new File("examples/invalid/invalid" + ".dot")
    );

    ParsingStrategy.fromFile(parser, fileReader).parse().getGraphs();
  }

  @Test(expected = ParseException.class)
  public void testParseInvalidBuffer() throws ParseException {
    ParsingStrategy
      .fromBuffer(parser, new StringBuffer("Something invalid..."))
      .parse()
      .getGraphs();
  }
}
