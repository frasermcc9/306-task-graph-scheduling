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

import java.io.FileWriter;
import java.io.IOException;

public class StandardFileSaver implements FileSaver {

  /**
   * Saves the given data to a dot file.
   *
   * @param path the path to save the file
   * @param data the data to save
   */
  @Override
  public void saveFile(String path, String data) throws IOException {
    try (FileWriter writer = new FileWriter(path)) {
      writer.write(data);
    }
  }
}
