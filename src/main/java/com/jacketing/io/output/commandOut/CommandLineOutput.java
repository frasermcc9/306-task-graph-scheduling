package com.jacketing.io.output.commandOut;

import com.jacketing.io.cli.IOContext;

public class CommandLineOutput implements CmdOutFactory {

  @Override
  public void printCmdOutput(IOContext ioContext) {
    System.out.println(ioContext.toString());
  }
}
