package com.jacketing.parsing.impl;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;

public class BufferGraphParser extends AbstractGraphParser {

  private final StringBuffer stringBuffer;

  public BufferGraphParser(Parser parser, StringBuffer stringBuffer) {
    super(parser);
    this.stringBuffer = stringBuffer;
  }

  @Override
  protected void parseGraphs() {
    try {
      this.getParser().parse(this.stringBuffer);
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }
}
