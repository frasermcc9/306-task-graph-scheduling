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

package com.jacketing.parsing.impl;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.jacketing.common.log.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraphOptimalSolutionReader {

  private int optimalLength;

  public GraphOptimalSolutionReader(InputStream inputStream)
    throws IOException {
    try {
      String result = CharStreams.toString(
        new InputStreamReader(inputStream, Charsets.UTF_8)
      );

      Pattern p = Pattern.compile(
        "\"Total schedule length\"=(\\d+)",
        Pattern.MULTILINE
      );
      Matcher m = p.matcher(result);

      if (m.find()) {
        String totalLengthString = m.group(1);
        optimalLength = Integer.parseInt(totalLengthString);
      } else {
        optimalLength = -1;
      }
    } catch (Exception e) {
      Log.error("Failed to load optimal length from file.");
      optimalLength = -1;
    }
  }

  public int getOptimalLength() {
    return optimalLength;
  }
}
