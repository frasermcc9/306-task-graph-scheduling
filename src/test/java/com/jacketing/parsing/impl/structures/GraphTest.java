package com.jacketing.parsing.impl.structures;

import com.jacketing.TestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
    assertTrue(criticalTime == 10);

    graph = TestUtil.graphVariantTwo();
    criticalTime = graph.getCriticalTime();
    assertTrue(criticalTime==54);
  }

}
