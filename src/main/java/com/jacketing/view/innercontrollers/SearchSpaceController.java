package com.jacketing.view.innercontrollers;

import com.jacketing.common.analysis.AlgorithmObserver;
import com.jacketing.parsing.impl.structures.Graph;

public class SearchSpaceController {

  private AlgorithmObserver observer;

  public SearchSpaceController(AlgorithmObserver observer) {
    this.observer = observer;

    new Thread(
      () -> {
        while (true) {
          pollGraph();
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    ).start();
  }

  public void pollGraph() {
    if (observer.hasGraph()) {

    }
  }





}
