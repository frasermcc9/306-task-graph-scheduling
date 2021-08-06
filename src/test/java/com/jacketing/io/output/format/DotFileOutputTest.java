package com.jacketing.io.output.format;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jacketing.TestUtil;
import com.jacketing.algorithm.impl.structures.Task;
import com.jacketing.algorithm.interfaces.structures.Schedule;
import com.jacketing.algorithm.interfaces.util.ScheduleFactory;
import com.jacketing.io.cli.ProgramContext;
import com.jacketing.io.output.saver.StandardFileSaver;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DotFileOutputTest {

  DotFileFormatter fileFormatter;
  StandardFileSaver fileSaver;

  @Before
  public void setUp() {
    fileFormatter = new DotFileFormatter();
    fileSaver = new StandardFileSaver();
  }

  @After
  public void tearDown() {
    File fileToDelete = new File("output");
    if (fileToDelete.exists()) {
      if (!fileToDelete.delete()) {
        fail("Warning: Failed to delete the test-generated file 'output'");
      }
    }
  }

  public Schedule getScheduleG1() {
    ProgramContext programContext = mock(ProgramContext.class);
    when(programContext.getProcessorsToScheduleOn()).thenReturn(2);
    Schedule schedule = ScheduleFactory.create().newSchedule(programContext);

    schedule.addTask(new Task(0, 2, 0), 0);
    schedule.addTask(new Task(2, 3, 1), 0);
    schedule.addTask(new Task(4, 3, 2), 1);
    schedule.addTask(new Task(7, 2, 3), 1);
    return schedule;
  }

  public String getValidOutputString() {
    return (
      "digraph Default {\n" +
      "\ta [Weight=2, Start=0, Processor=0];\n" +
      "\tb [Weight=3, Start=2, Processor=0];\n" +
      "\tc [Weight=3, Start=4, Processor=1];\n" +
      "\td [Weight=2, Start=7, Processor=1];\n" +
      "\ta -> b [Weight=1];\n" +
      "\ta -> c [Weight=2];\n" +
      "\tb -> d [Weight=2];\n" +
      "\tc -> d [Weight=1];\n" +
      "}"
    );
  }

  @Test
  public void checkOutputCorrect() {
    String output = fileFormatter.formatSchedule(
      getScheduleG1(),
      TestUtil.graphVariantOne()
    );
    assertEquals(getValidOutputString(), output);
  }

  @Test
  public void checkOutputFileGeneration() throws IOException {
    fileSaver.saveFile("output", getValidOutputString());
    String content = new String(
      Files.readAllBytes(Paths.get("output")),
      StandardCharsets.UTF_8
    );
    assertEquals(getValidOutputString(), content);
  }
}
