package com.jacketing.parsing.impl.structures;

import com.jacketing.TestUtil;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EnumeratedAdjacencyListTest {

  Graph graphOne;
  Graph graphTwo;

  @Before
  public void setUp() {
    graphOne = TestUtil.graphVariantOne();
    graphTwo = TestUtil.graphVariantTwo();
  }

  @Test
  public void getChildNodes() {
    List<Integer> childNodes = graphTwo.getAdjacencyList().getChildNodes(0);
    Assert.assertEquals(3, childNodes.size());
    Assert.assertEquals(1, childNodes.get(0).intValue());
    Assert.assertEquals(2, childNodes.get(1).intValue());
    Assert.assertEquals(3, childNodes.get(2).intValue());
  }

  @Test
  public void testRootParentCount() {
    int parentCount = graphTwo.getAdjacencyList().parentCount(0);
    Assert.assertEquals(0, parentCount);
  }

  @Test
  public void testParentCountOfNodeWithParent() {
    int parentCount = graphTwo.getAdjacencyList().parentCount(1);
    Assert.assertEquals(1, parentCount);
  }

  @Test
  public void testParentCountOfNodeWithMultipleParents() {
    int parentCount = graphOne.getAdjacencyList().parentCount(3);
    Assert.assertEquals(2, parentCount);
  }

  @Test
  public void parentCountForEach() {
    Map<Integer, Integer> nodeToParentCount = graphOne
      .getAdjacencyList()
      .parentCountForEach();

    Assert.assertEquals(4, nodeToParentCount.size());
    Assert.assertEquals(0, nodeToParentCount.get(0).intValue());
    Assert.assertEquals(1, nodeToParentCount.get(1).intValue());
    Assert.assertEquals(1, nodeToParentCount.get(2).intValue());
    Assert.assertEquals(2, nodeToParentCount.get(3).intValue());
  }

  @Test
  public void getNodeCount() {
    int nodeCount = graphTwo.getAdjacencyList().getNodeCount();
    Assert.assertEquals(7, nodeCount);
  }
}
