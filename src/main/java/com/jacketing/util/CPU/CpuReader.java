package com.jacketing.util.CPU;

import com.jacketing.parsing.impl.CpuUsageFormatter;
import com.sun.management.ThreadMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.util.*;

public class CpuReader {

  private final ThreadMXBean threadBean;
  private final RuntimeMXBean runtimeBean;
  private final List<CpuStatModel> models = new ArrayList<>();

  /**
   * Reads cpu memory at 400ms intervals
   */
  public CpuReader() {
    threadBean = (ThreadMXBean) ManagementFactory.getThreadMXBean();
    runtimeBean = ManagementFactory.getRuntimeMXBean();

    // set this true just in case
    threadBean.setThreadCpuTimeEnabled(true);

    new Thread(
      () -> {
        while (true) {
          pollCpu();
          try {
            Thread.sleep(25);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    )
      .start();
  }

  /**
   * Enable synthetic load to test utilization
   */
  public void setSyntheticLoad() {
    int numCore = 16;
    int numThreadsPerCore = 2;
    double load = 0.8;
    final long duration = 100000;
    for (int thread = 0; thread < numCore * numThreadsPerCore; thread++) {
      new BusyThread("Thread" + thread, load, duration).start();
    }
  }

  /**
   * Add model listener
   * @param model
   */
  public void addModel(CpuStatModel model) {
    models.add(model);
  }

  /**
   * Inspired by: https://stackoverflow.com/questions/29931391/obtaining-cpu-thread-usage-in-java
   */
  private void pollCpu() {
    int sampleTime = 200;
    Map<Long, Long> threadInitialCPU = new HashMap<>();
    Map<Long, Float> threadCPUUsage = new HashMap<>();
    long initialUptime = runtimeBean.getUptime();

    ThreadInfo[] threadInfos = threadBean.dumpAllThreads(false, false);
    for (ThreadInfo info : threadInfos) {
      threadInitialCPU.put(
        info.getThreadId(),
        threadBean.getThreadCpuTime(info.getThreadId())
      );
    }

    try {
      Thread.sleep(sampleTime);
    } catch (InterruptedException ignored) {}

    long upTime = runtimeBean.getUptime();

    Map<Long, Long> threadCurrentCPU = new HashMap<>();
    threadInfos = threadBean.dumpAllThreads(false, false);
    for (ThreadInfo info : threadInfos) {
      threadCurrentCPU.put(
        info.getThreadId(),
        threadBean.getThreadCpuTime(info.getThreadId())
      );
    }

    // elapsedTime is in ms.
    long elapsedTime = (upTime - initialUptime);

    for (ThreadInfo info : threadInfos) {
      // elapsedCpu is in ns
      Long initialCPU = threadInitialCPU.get(info.getThreadId());
      if (initialCPU != null) {
        long elapsedCpu = threadCurrentCPU.get(info.getThreadId()) - initialCPU;
        float cpuUsage = elapsedCpu / (elapsedTime * 1000000F);
        threadCPUUsage.put(info.getThreadId(), cpuUsage);
      }
    }

    for (CpuStatModel model : models) {
      model.change(CpuUsageFormatter.format(threadCPUUsage));
    }
  }
}
