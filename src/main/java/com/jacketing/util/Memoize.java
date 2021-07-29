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
