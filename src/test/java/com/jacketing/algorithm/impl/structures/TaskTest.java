package com.jacketing.algorithm.impl.structures;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TaskTest {

  Task task;

  @Before
  public void setup() {
    task = new Task(4, 8, 0, 1);
  }

  @Test
  public void testGetEndTime() {
    int endTime = task.getEndTime();
    Assert.assertEquals(12, endTime);
  }

  @Test
  public void testGetStart() {
    Assert.assertEquals(4, task.getStartTime());
  }

  @Test
  public void testGetDuration() {
    Assert.assertEquals(8, task.getDuration());
  }

  @Test
  public void testGetId() {
    Assert.assertEquals(0, task.getId());
  }
}
