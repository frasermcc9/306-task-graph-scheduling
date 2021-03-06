package com.jacketing;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EntryTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final PrintStream originalErr = System.err;

  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @After
  public void restoreStreams() {
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  @Test
  public void testInvalidEntry() {
    String[] args = {};
    Entry.main(args);
    String output = outContent.toString();
    Assert.assertEquals(
      "2 arguments expected, received 0",
      output.trim().split("\n")[0].trim()
    );
  }

  @Test
  public void testConstructor() {
    new Entry();
  }
}
