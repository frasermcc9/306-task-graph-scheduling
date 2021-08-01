// ********************************************************************************
// Copyright 2021 Team Jacketing All Rights Reserved.
//
// NOTICE: All information contained herein is, and remains the property of Team
// Jacketing (the author) and their affiliates, if any. The intellectual and
// technical concepts contained herein are proprietary to Team Jacketing , and
// are protected by copyright law. Dissemination of this information or
// reproduction of this material is strictly forbidden unless prior written
// permission is obtained from the author.
// *******************************************************************************

package com.jacketing;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.jacketing.io.cli.ProgramContext;

public class Entry {

  public static void main(String... argv) {
    ProgramContext programContext = new ProgramContext();

    try {
      JCommander.newBuilder().addObject(programContext).build().parse(argv);
      programContext.validate();
      System.out.println("Starting search...");
    } catch (ParameterException e) {
      System.out.println(e.getMessage());
      System.out.println(programContext.helpText());
    }
  }
}
