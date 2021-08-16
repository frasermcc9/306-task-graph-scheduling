package com.jacketing.view.innercontrollers;

import com.jacketing.common.analysis.AlgorithmObserver;
import com.jacketing.parsing.impl.structures.EnumeratedAdjacencyList;
import javafx.scene.layout.StackPane;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.javafx.FxGraphRenderer;
import java.util.List;
import java.util.Map;

public class SearchSpaceController {

  private AlgorithmObserver observer;
  private Graph graphData;

  public SearchSpaceController(AlgorithmObserver observer, StackPane pane) {
    System.setProperty("org.graphstream.ui", "javafx");
    Graph graph = new SingleGraph("Input Display");
    this.graphData = graph;

    EnumeratedAdjacencyList list = observer.getGraph().getAdjacencyList();

    for (int nodeId : list.getNodeIds()) {
      graph.addNode(nodeId + "");
    }

    for (Map.Entry<Integer, List<Integer>> entry : list.getInAdjacencyList().entrySet()) {
      String key = Integer.toString(entry.getKey());
      for (int value : entry.getValue()) {
        String node = Integer.toString(value);
        graph.addEdge(key + node, key, node);
      }
    }

    for (Node node : graph) {
      node.setAttribute("ui.label", "  " + node.getId());
      node.setAttribute("size", "big");
    }

    graph.setAttribute("ui.stylesheet", "graph { fill-color: #2f2d2e; } node { fill-color: #00aeef; size: 30px; } edge { size: 3px; }");
    FxViewer view = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
    view.enableAutoLayout();

    FxViewPanel panel = (FxViewPanel) view.addView(FxViewer.DEFAULT_VIEW_ID, new FxGraphRenderer());
    pane.getChildren().addAll(panel); // prevent UI shift issues
    this.observer = observer;

    new Thread(
      () -> {
        while (true) {
          pollGraph();
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    ).start();
  }

  public void pollGraph() {
    if (observer.hasGraph()) {

    }
  }





}
