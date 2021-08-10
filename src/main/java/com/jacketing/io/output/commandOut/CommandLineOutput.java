package com.jacketing.io.output.commandOut;

import com.jacketing.io.cli.ApplicationContext;

public class CommandLineOutput implements CmdOutFactory {

  @Override
  public void printCmdOutput(ApplicationContext context) {
    System.out.println(context.toString());
  }
}
