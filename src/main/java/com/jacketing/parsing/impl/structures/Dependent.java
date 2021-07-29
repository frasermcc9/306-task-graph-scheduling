package com.jacketing.parsing.impl.structures;

public class Dependent {

  private final int transferTime;
  private GraphNode graphNode;

  public Dependent(GraphNode graphNode, int transferTime) {
    this.transferTime = transferTime;
    this.graphNode = graphNode;
  }

  public int getTransferTime() {
    return transferTime;
  }
}
