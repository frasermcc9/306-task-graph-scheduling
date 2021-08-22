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

import com.paypal.digraph.parser.GraphParser;
import com.paypal.digraph.parser.GraphParserException;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Parser {

  GraphParser parser;
  GraphOptimalSolutionReader optimalSolutionReader;

  private Parser(InputStream stream1, InputStream stream2) throws IOException {
    optimalSolutionReader = new GraphOptimalSolutionReader(stream1);
    this.parser = new GraphParser(stream2);
  }

  public static Parser fromFile(String path) throws IOException {
    try (
      InputStream inputStream1 = new FileInputStream(path);
      InputStream inputStream2 = new FileInputStream(path)
    ) {
      return new Parser(inputStream1, inputStream2);
    }
  }

  public static Parser fromString(String string) throws IOException {
    try (
      InputStream inputStream1 = new ByteArrayInputStream(string.getBytes());
      InputStream inputStream2 = new ByteArrayInputStream(string.getBytes())
    ) {
      return new Parser(inputStream1, inputStream2);
    }
  }

  public static Parser fromStringBuffer(StringBuffer stringBuffer)
    throws IOException {
    return fromString(stringBuffer.toString());
  }

  public GraphParser parse() throws GraphParserException {
    return parser;
  }

  public int getOptimalLength() {
    return optimalSolutionReader.getOptimalLength();
  }
}
