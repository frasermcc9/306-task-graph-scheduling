package com.jacketing.parsing.impl;

import com.paypal.digraph.parser.GraphParser;
import com.paypal.digraph.parser.GraphParserException;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Parser {

  GraphParser parser;

  private Parser(InputStream stream) {
    this.parser = new GraphParser(stream);
  }

  public static Parser fromFile(String path) throws IOException {
    try (InputStream inputStream = new FileInputStream(path)) {
      return new Parser(inputStream);
    }
  }

  public static Parser fromString(String string) throws IOException {
    try (
      InputStream inputStream = new ByteArrayInputStream(string.getBytes())
    ) {
      return new Parser(inputStream);
    }
  }

  public static Parser fromStringBuffer(StringBuffer stringBuffer)
    throws IOException {
    return fromString(stringBuffer.toString());
  }

  public GraphParser parse() throws GraphParserException {
    return parser;
  }
}
