package com.jacketing.util.RAM;

public class RamReader {

  private final Runtime runtime;
  private RamStatModel model;

  public RamReader(RamStatModel model) {
    this.model = model;
    runtime = Runtime.getRuntime();

    new Thread(() -> {
      while(true) {
        pollRam();
        try {
          Thread.sleep(50);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

  private void pollRam() {
    long usedRam = runtime.totalMemory() - runtime.freeMemory();
    model.change(usedRam);
  }
}
