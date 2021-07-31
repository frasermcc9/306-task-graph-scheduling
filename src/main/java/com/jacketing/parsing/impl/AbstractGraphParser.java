package com.jacketing.parsing.impl;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Graph;
import com.jacketing.parsing.interfaces.ParsingStrategy;
import java.util.List;
import java.util.Optional;

public abstract class AbstractGraphParser implements ParsingStrategy {

  private final Parser parser;
  private List<Graph> graphs;

  public AbstractGraphParser(Parser parser) {
    this.parser = parser;
    this.graphs = null;
  }

  public AbstractGraphParser parse() throws ParseException {
    parseGraphs();
    this.graphs = parser.getGraphs();
    return this;
  }

  public Optional<List<Graph>> getGraphs() {
    return Optional.ofNullable(graphs);
  }

  public Parser getParser() {
    return parser;
  }

  protected abstract void parseGraphs() throws ParseException;
}
