package com.jacketing.util;

import static com.jacketing.util.Memoize.useMemo;
import static org.junit.Assert.assertEquals;

import java.util.function.Function;
import org.junit.Test;

public class MemoizeTest {

  int base = 0;
  private final Function<Integer, Integer> addOne = useMemo(v -> v + base + 10);

  @Test
  public void testUseMemoStoresFunctionCall() {
    int value = addOne.apply(4);
    assertEquals(14, value);

    base = 1;
    value = addOne.apply(4);
    assertEquals(14, value);

    value = addOne.apply(5);
    assertEquals(16, value);
  }
}
