package com.jacketing.algorithm.impl.util.topological;

import static org.junit.Assert.assertEquals;

import com.jacketing.TestUtil;
import com.jacketing.algorithm.interfaces.util.topological.TopologicalSort;
import com.jacketing.parsing.impl.structures.Graph;
import java.util.List;
import org.junit.Test;

public class LayeredTopologicalOrderFinderTest {

  @Test
  public void testLayeredTopologicalSort() {
    Graph graph = TestUtil.graphVariantOne();
    TopologicalSortContext<List<Integer>> listTopologicalSortContext = new TopologicalSortContext<>(
      TopologicalSort.withLayers(graph)
    );

    List<List<Integer>> layers = listTopologicalSortContext.sortedTopological();

    assertEquals(3, layers.size());
    assertEquals((Integer) 0, layers.get(0).get(0));
    assertEquals((Integer) 1, layers.get(1).get(0));
    assertEquals((Integer) 2, layers.get(1).get(1));
    assertEquals((Integer) 3, layers.get(2).get(0));
  }

  @Test
  public void testLayeredTopologicalSortGraphTwo() {
    Graph graph = TestUtil.graphVariantTwo();
    TopologicalSortContext<List<Integer>> listTopologicalSortContext = new TopologicalSortContext<>(
      TopologicalSort.withLayers(graph)
    );

    List<List<Integer>> layers = listTopologicalSortContext.sortedTopological();

    assertEquals(3, layers.size());
    assertEquals(1, layers.get(0).size());
    assertEquals(3, layers.get(1).size());
    assertEquals(3, layers.get(2).size());
  }
}
