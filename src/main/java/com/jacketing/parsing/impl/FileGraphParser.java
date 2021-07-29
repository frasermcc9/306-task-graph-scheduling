package com.jacketing.parsing.impl;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import java.io.Reader;
import org.jetbrains.annotations.NotNull;

public class FileGraphParser extends AbstractGraphParser {

  private final Reader fileReader;

  public FileGraphParser(@NotNull Parser parser, @NotNull Reader fileReader) {
    super(parser);
    this.fileReader = fileReader;
  }

  @Override
  protected void parseGraphs() {
    try {
      this.getParser().parse(this.fileReader);
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }
}
