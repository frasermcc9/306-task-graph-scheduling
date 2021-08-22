package com.jacketing.parsing.impl.structures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.jacketing.TestUtil;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GraphTest {

  Graph graph;

  @Before
  public void setUp() {
    graph = TestUtil.graphVariantOne();
  }

  @Test
  public void testTranslateFromInt() {
    String original = graph.translate(3);
    Assert.assertEquals("d", original);
  }

  @Test
  public void testTranslateFromString() {
    int enumerated = graph.translate("b");
    Assert.assertEquals(1, enumerated);
  }

  @Test
  public void testCriticalTime() {
    int criticalTime = graph.getCriticalTime();
    assertEquals(7, criticalTime);

    graph = TestUtil.graphVariantTwo();
    criticalTime = graph.getCriticalTime();
    assertEquals(18, criticalTime);
  }

  @Test
  public void testBLevelGraphOne() {
    graph = TestUtil.graphVariantThree();
    assertEquals(315, graph.getBLevel(0));
    assertEquals(126, graph.getBLevel(8));
  }

  @Test
  public void testBLevelGraphFive() {
    graph = TestUtil.graphVariantFive();
    assertEquals(18, graph.getBLevel(0));
    assertEquals(7, graph.getBLevel(5));
  }

  @Test
  public void testBLevelGraphComplex() {
    graph = TestUtil.graphVariantSix();
    assertEquals(94, graph.getBLevel(7));
  }

  @Test
  public void testDuplicateNodesAddsVirtualEdge() {
    graph = TestUtil.graphVariantIdenticalNodes();
    graph.introduceVirtualForIdenticalNodes();

    List<Integer> childNodes = graph.getAdjacencyList().getChildNodes(1);
    assertTrue(childNodes.contains(2));
  }
}
