/*
 * Copyright 2021 Team Jacketing
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property of Team
 * Jacketing (the author) and its affiliates, if any. The intellectual and
 * technical concepts contained herein are proprietary to Team Jacketing, and
 * are protected by copyright law. Dissemination of this information or
 * reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from the author.
 *
 */

package com.jacketing.algorithm.AO.Collections;

import java.util.Stack;

public class StackCollection<T>
  extends Stack<T>
  implements AlgorithmCollection<T> {

  @Override
  public T poll() {
    return super.pop();
  }

  @Override
  public boolean offer(T item) {
    return this.add(item);
  }
}
