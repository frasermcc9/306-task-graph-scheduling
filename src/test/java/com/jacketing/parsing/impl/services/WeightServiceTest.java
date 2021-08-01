package com.jacketing.parsing.impl.services;

import com.jacketing.TestUtil;
import com.jacketing.parsing.impl.structures.Graph;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WeightServiceTest {

  private Graph graph;

  @Before
  public void setUp() {
    graph = TestUtil.graphVariantThree();
  }

  @Test
  public void nodeWeight() {
    int nodeWeight = graph.getNodeWeight(0);
    Assert.assertEquals(126, nodeWeight);
  }

  @Test
  public void edgeWeight() {
    int src = graph.translate("2");
    int target = graph.translate("9");
    int edgeWeight = graph.getEdgeWeight().from(src).to(target);
    Assert.assertEquals(10, edgeWeight);
  }
}
