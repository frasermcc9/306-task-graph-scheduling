package com.jacketing.io.output.commandOut;

@FunctionalInterface
public interface CommandOutputFactory {
  CommandOutput create();
}
