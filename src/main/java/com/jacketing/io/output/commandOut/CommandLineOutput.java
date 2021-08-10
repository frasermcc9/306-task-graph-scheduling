package com.jacketing.io.output.commandOut;

import com.jacketing.io.cli.IOContext;

public class CommandLineOutput implements CmdOutFactory {

  private final IOContext ioContext;

  private CommandLineOutput(IOContext ioContext) {
    this.ioContext = ioContext;
  }

  @Override
  public void printCmdOutput(IOContext ioContext) {}
}
