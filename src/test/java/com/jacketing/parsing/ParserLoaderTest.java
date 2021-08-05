/*
 * Copyright 2021 Team Jacketing
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property of Team
 * Jacketing (the author) and its affiliates, if any. The intellectual and
 * technical concepts contained herein are proprietary to Team Jacketing, and
 * are protected by copyright law. Dissemination of this information or
 * reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from the author.
 *
 */

package com.jacketing.parsing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.jacketing.common.Loader;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.HashMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ParserLoaderTest {

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  @Test
  public void testParserLoaderLoadsGraph() {
    Loader<Graph> graphLoader = ParserLoader.create(
      "examples/g1/g1.dot",
      HashMap::new
    );

    Graph load = graphLoader.load();

    assertEquals(4, load.getAdjacencyList().getNodeCount());
  }

  @Test
  public void testParserLoaderReturnsNullOnFailedGraph() {
    Loader<Graph> graphLoader = ParserLoader.create(
      "examples/invalid/invalid" + ".dot",
      HashMap::new
    );

    Graph load = graphLoader.load();

    assertNull(load);
  }
}
