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

package com.jacketing.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class Memoize<T, U> {

  private final Map<T, U> cache = new ConcurrentHashMap<>();

  private Memoize() {}

  private Function<T, U> createMemo(final Function<T, U> function) {
    return input -> cache.computeIfAbsent(input, function);
  }

  public static <T, U> Function<T, U> useMemo(final Function<T, U> function) {
    return new Memoize<T, U>().createMemo(function);
  }
}
