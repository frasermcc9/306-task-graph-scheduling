package com.jacketing.io.output.commandOut;

import com.jacketing.io.cli.ApplicationContext;
import com.jacketing.io.cli.IOContext;

public interface CmdOutFactory {
  void printCmdOutput(ApplicationContext context);
}
