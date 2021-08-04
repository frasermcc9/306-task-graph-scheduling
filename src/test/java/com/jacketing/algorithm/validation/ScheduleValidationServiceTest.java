package com.jacketing.algorithm.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.HashBiMap;
import com.jacketing.algorithm.impl.structures.ScheduleImpl;
import com.jacketing.algorithm.impl.structures.Task;
import com.jacketing.algorithm.interfaces.structures.Schedule;
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

public class ScheduleValidationServiceTest {

  ScheduleValidationService validationService;

  @Before
  public void setUp() throws Exception {
    validationService = new ScheduleValidationService();
  }

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

  public Schedule getValidScheduleG1() {
    Schedule schedule = new ScheduleImpl(2);
    schedule.addTask(new Task(0, 2, 0, 0));
    schedule.addTask(new Task(2, 3, 1, 0));
    schedule.addTask(new Task(4, 3, 2, 1));
    schedule.addTask(new Task(7, 2, 3, 1));
    return schedule;
  }

  public Schedule getInvalidTimeTimeScheduleG1() {
    Schedule schedule = new ScheduleImpl(2);
    schedule.addTask(new Task(0, 2, 0, 0));
    schedule.addTask(new Task(2, 3, 1, 0));
    schedule.addTask(new Task(2, 3, 2, 1));
    schedule.addTask(new Task(7, 2, 3, 1));
    return schedule;
  }

  public Schedule getInvalidTaskOrderScheduleG1() {
    Schedule schedule = new ScheduleImpl(2);
    schedule.addTask(new Task(3, 2, 0, 0));
    schedule.addTask(new Task(0, 3, 1, 0));
    schedule.addTask(new Task(10, 3, 2, 1));
    schedule.addTask(new Task(20, 2, 3, 1));
    return schedule;
  }

  @Test
  public void validateScheduleCorrect() {
    boolean isValid = validationService.validateSchedule(
      getValidScheduleG1(),
      getGraphG1(),
      2
    );
    assertTrue(isValid);
  }

  @Test
  public void validateScheduleInvalidStartTime() {
    boolean isValid = validationService.validateSchedule(
      getInvalidTimeTimeScheduleG1(),
      getGraphG1(),
      2
    );
    assertFalse(isValid);
  }

  @Test
  public void validateScheduleInvalidTaskOrder() {
    boolean isValid = validationService.validateSchedule(
      getInvalidTaskOrderScheduleG1(),
      getGraphG1(),
      2
    );
    assertFalse(isValid);
  }

  @Test
  public void validateInvalidScheduleInvalidProcessorCount() {
    assertFalse(
      validationService.validateSchedule(
        getValidScheduleG1(),
        getGraphG1(),
        200
      )
    );
    assertFalse(
      validationService.validateSchedule(getValidScheduleG1(), getGraphG1(), 0)
    );
    assertFalse(
      validationService.validateSchedule(getValidScheduleG1(), getGraphG1(), -1)
    );
  }
}
