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

package com.jacketing.io.output.saver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConsoleFileSaver implements FileSaver {

  /**
   * Writes the given data to the console
   *
   * @param unused unused value (should be null)
   * @param data   the data to save
   */
  @Override
  public void saveFile(@Nullable String unused, @NotNull String data) {
    System.out.println(data);
  }
}
