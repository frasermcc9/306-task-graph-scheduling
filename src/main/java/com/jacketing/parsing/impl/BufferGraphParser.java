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

@Deprecated
public class BufferGraphParser extends AbstractGraphParser {

  private final StringBuffer stringBuffer;

  public BufferGraphParser(Parser parser, StringBuffer stringBuffer) {
    super(parser);
    this.stringBuffer = stringBuffer;
  }

  @Override
  protected void parseGraphs() throws ParseException {
    this.getParser().parse(this.stringBuffer);
  }
}
