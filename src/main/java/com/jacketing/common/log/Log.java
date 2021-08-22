/*
 * Copyright 2021 Team Jacketing
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property of Team
 * Jacketing (the author) and its affiliates, if any. The intellectual and
 * technical concepts contained herein are proprietary to Team Jacketing, and
 * are protected by copyright law. Dissemination of this information or
 * reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from the author.
 *
 */

package com.jacketing.common.log;

import static org.fusesource.jansi.Ansi.ansi;

import java.text.MessageFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Log {

  private static LogLevel logLevel = LogLevel.WARN;

  public static void loggingLevel(LogLevel logLevel) {
    Log.logLevel = logLevel;
  }

  private static void print(String message) {
    System.out.println(ansi().render(message));
  }

  public static void debug(String message) {
    if (checkLogLevel(LogLevel.DEBUG)) {
      String out = MessageFormat.format(
        "[{0}] [{1}] @|white [Info]|@: {2}",
        time(),
        thread(),
        message
      );
      print(out);
    }
  }

  public static void info(String message) {
    if (checkLogLevel(LogLevel.INFO)) {
      String out = MessageFormat.format(
        "[{0}] [{1}] @|green [Info]|@: {2}",
        time(),
        thread(),
        message
      );
      print(out);
    }
  }

  public static void warn(String message) {
    if (checkLogLevel(LogLevel.WARN)) {
      String out = MessageFormat.format(
        "[{0}] [{1}] @|yellow [Warn]|@: {2}",
        time(),
        thread(),
        message
      );
      print(out);
    }
  }

  public static void error(String message) {
    if (checkLogLevel(LogLevel.ERROR)) {
      String out = MessageFormat.format(
        "[{0}] [{1}] @|red [Error]|@: {2}",
        time(),
        thread(),
        message
      );
      print(out);
    }
  }

  private static boolean checkLogLevel(LogLevel level) {
    return (level.compareTo(logLevel) >= 0);
  }

  private static String time() {
    LocalTime time = LocalTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    return time.format(formatter);
  }

  private static String thread() {
    return Thread.currentThread().getName();
  }
}
