package com.jacketing.io.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProgramContextTest {

  private ProgramContext programContext;
  private JCommander builder;

  @Before
  public void setupArgs() {
    programContext = new ProgramContext();
    builder = JCommander.newBuilder().addObject(programContext).build();
  }

  @Test
  public void testInputFile() {
    String[] input = { "INPUT.dot", "2" };
    builder.parse(input);
    Assert.assertEquals("INPUT.dot", programContext.getInputFile());
  }

  @Test
  public void testInvalidInputFile() {
    String[] input = { "INPUT", "2" };
    builder.parse(input);

    try {
      programContext.validate();
      Assert.fail("Exception expected");
    } catch (ParameterException e) {
      Assert.assertEquals(
        ".dot file expected as first parameter, received INPUT",
        e.getMessage()
      );
    }
  }

  @Test
  public void testCoresScheduleOn() {
    String[] input = { "INPUT.dot", "2" };
    builder.parse(input);
    Assert.assertEquals(2, programContext.getProcessorsToScheduleOn());
  }

  @Test
  public void testInvalidCoresScheduleOn() {
    String[] input = { "INPUT.dot", "a" };
    builder.parse(input);

    try {
      programContext.validate();
      Assert.fail("Exception expected");
    } catch (ParameterException e) {
      Assert.assertEquals(
        "Integer expected as second parameter, received a",
        e.getMessage()
      );
    }
  }

  @Test
  public void testCoresCalculateWith() {
    String[] input = { "INPUT.dot", "1", "-p", "2" };
    builder.parse(input);
    Assert.assertEquals(2, programContext.getCoresToCalculateWith());
  }

  @Test
  public void testInvalidCoresCalculateWith() {
    String[] input = { "INPUT.dot", "1", "-p", "a" };

    try {
      builder.parse(input);
      Assert.fail("Exception expected");
    } catch (ParameterException e) {
      Assert.assertEquals(
        "java.lang.NumberFormatException: For input string: \"a\"",
        e.getMessage()
      );
    }
  }

  @Test
  public void testVisualize() {
    String[] input = { "INPUT.dot", "2", "-v" };
    builder.parse(input);
    Assert.assertEquals(true, programContext.isVisualized());
  }

  @Test
  public void testOutput() {
    String[] input = { "INPUT.dot", "2", "-o", "Test.dot" };
    builder.parse(input);
    Assert.assertEquals("Test.dot", programContext.getOutputName());
  }

  @Test
  public void testNoOutput() {
    String[] input = { "INPUT.dot", "2" };
    builder.parse(input);
    Assert.assertEquals("INPUT-output.dot", programContext.getOutputName());
  }
}
