package com.jacketing.parsing.interfaces.structures.services;

import com.alexmerz.graphviz.objects.Graph;

public interface GraphEnumerationService {
  EnumeratedNodeMap enumerateFromGraph(Graph graph);
}
