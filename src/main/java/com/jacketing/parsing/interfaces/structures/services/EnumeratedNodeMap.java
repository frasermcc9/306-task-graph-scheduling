package com.jacketing.parsing.interfaces.structures.services;

import com.alexmerz.graphviz.objects.Node;
import com.paypal.digraph.parser.GraphNode;

/**
 * The {@link EnumeratedNodeMap} interface provides the ability to convert
 * string represented IDs to integers, and vice-versa. This is done to ensure
 * that the algorithm can run on integers, which should be faster than strings
 * for operations like comparison.
 */
public interface EnumeratedNodeMap {
  /**
   * Get the string identifier of an enumerated node.
   *
   * @param numeral the int value of the enumerated node.
   * @return the string (original) representation.
   */
  String getIdFromNumeral(int numeral);

  /**
   * Get the enumerated int value of the string identifier.
   *
   * @param id the string identifier of the node
   * @return the enumerated int value.
   */
  int getEnumerated(String id);

  /**
   * Helper method for quickly getting an enumerated value from a raw node
   *
   * @param n the node
   * @return its enumerated value
   * @deprecated the library that uses this node is no longer used
   */
  @Deprecated
  default int getEnumeratedNode(Node n) {
    return this.getEnumerated(n.getId().getId());
  }

  /**
   * Helper method for quickly getting an enumerated value from a raw node
   *
   * @param n the node
   * @return its enumerated value
   */
  default int getEnumeratedNode(GraphNode n) {
    return this.getEnumerated(n.getId());
  }
}
