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

package com.jacketing.parsing.impl;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Graph;
import com.jacketing.parsing.interfaces.ParsingStrategy;
import java.util.List;
import java.util.Optional;

@Deprecated
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
