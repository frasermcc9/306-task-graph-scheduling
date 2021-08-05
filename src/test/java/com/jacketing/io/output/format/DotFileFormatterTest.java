package com.jacketing.io.output.format;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.HashBiMap;
import com.jacketing.algorithm.impl.structures.Task;
import com.jacketing.algorithm.interfaces.structures.Schedule;
import com.jacketing.algorithm.interfaces.util.ScheduleFactory;
import com.jacketing.io.cli.IOContext;
import com.jacketing.io.cli.ProgramContext;
import com.jacketing.io.output.saver.FileSaverFactory;
import com.jacketing.parsing.impl.Parser;
import com.jacketing.parsing.impl.services.EnumerationService;
import com.jacketing.parsing.impl.services.WeightService;
import com.jacketing.parsing.impl.structures.EnumeratedAdjacencyList;
import com.jacketing.parsing.impl.structures.Graph;
import com.jacketing.parsing.interfaces.structures.services.EnumeratedNodeMap;
import com.paypal.digraph.parser.GraphParser;
import java.util.HashMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DotFileFormatterTest {

  DotFileFormatter fileFormatter;

  private static Graph createGraph(GraphParser graph) {
    EnumerationService enumerationService = new EnumerationService(
      HashBiMap.create()
    );
    EnumeratedNodeMap enumeratedNodeMap = enumerationService.enumerateFromGraph(
      graph
    );
    return new Graph(
      new EnumeratedAdjacencyList(
        graph,
        enumeratedNodeMap,
        new HashMap<>(),
        new HashMap<>()
      ),
      new WeightService(
        graph,
        enumeratedNodeMap,
        new HashMap<>(),
        new HashMap<>()
      )
    );
  }

  private static Graph createGraphFromBuffer(String graphString) {
    try {
      com.jacketing.parsing.impl.Parser parser = Parser.fromStringBuffer(
        new StringBuffer(graphString)
      );
      GraphParser graph = parser.parse();
      return createGraph(graph);
    } catch (Exception e) {
      return null;
    }
  }

  @Before
  public void setUp() throws Exception {
    fileFormatter = new DotFileFormatter();
  }

  @After
  public void tearDown() throws Exception {}

  public Graph getGraphG1() {
    return createGraphFromBuffer(
      "digraph \"g1\" {\n" +
      "  a [Weight = 2];\n" +
      "  b [Weight = 3];\n" +
      "  a -> b [Weight = 1];\n" +
      "  c [Weight = 3];\n" +
      "  a -> c [Weight = 2];\n" +
      "  d [Weight = 2];\n" +
      "  b -> d [Weight = 2];\n" +
      "  c -> d [Weight = 1];\n" +
      "}\n"
    );
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

  @Test
  public void checkOutputCorrect() {
    String output = fileFormatter.formatSchedule(getScheduleG1(), getGraphG1());
    System.out.println(output);
    assertEquals(
      "digraph Default {\n" +
      "\ta [Weight=2, Start=0, Processor=0];\n" +
      "\tb [Weight=3, Start=2, Processor=0];\n" +
      "\tc [Weight=3, Start=4, Processor=1];\n" +
      "\td [Weight=2, Start=7, Processor=1];\n" +
      "\ta -> b [Weight=1];\n" +
      "\ta -> c [Weight=2];\n" +
      "\tb -> d [Weight=2];\n" +
      "\tc -> d [Weight=1];\n" +
      "}",
      output
    );
  }
}
