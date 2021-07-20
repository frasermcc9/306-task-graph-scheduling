package com.jacketing;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class EntryTest {
    @Test
    public void testTestRunnerRunsTests() {
        Assert.assertEquals(1 + 1, 2);
    }

    @Test
    public void testTestRunnerRunsFailedTests() {
        Assert.assertEquals(1 + 1, 3);
    }
}