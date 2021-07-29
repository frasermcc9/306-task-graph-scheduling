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

public class Entry {

  public static void main(String... argv) {
    Args args = new Args();

    try {
      JCommander.newBuilder().addObject(args).build().parse(argv);
      args.validate();
      System.out.println("Starting search...");
    } catch (ParameterException e) {
      System.out.println(e.getMessage());
      System.out.println(args.helpText());
    }
  }
}
