package com.jacketing.parsing.interfaces;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import com.jacketing.parsing.impl.AbstractGraphParser;
import com.jacketing.parsing.impl.BufferGraphParser;
import com.jacketing.parsing.impl.FileGraphParser;
import java.io.Reader;
import org.jetbrains.annotations.NotNull;

public interface ParsingStrategy {
  static ParsingStrategy fromBuffer(
    @NotNull Parser parser,
    @NotNull StringBuffer stringBuffer
  ) {
    return new BufferGraphParser(parser, stringBuffer);
  }

  static ParsingStrategy fromFile(
    @NotNull Parser parser,
    @NotNull Reader fileReader
  ) {
    return new FileGraphParser(parser, fileReader);
  }

  AbstractGraphParser parse() throws ParseException;
}
