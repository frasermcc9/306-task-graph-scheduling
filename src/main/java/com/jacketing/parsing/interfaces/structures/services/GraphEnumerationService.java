package com.jacketing.parsing.interfaces.structures.services;

import com.paypal.digraph.parser.GraphParser;

public interface GraphEnumerationService {
  EnumeratedNodeMap enumerateFromGraph(GraphParser graph);
}
