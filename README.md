<h1 align="center">

  ![Logo](https://media.discordapp.net/attachments/826599483126579282/872727060798394398/jacketing-logo4x.png)

  Jacketing Studio Scheduler

</h1>

<div align="center" class="row">

  ![Coverage](/.github/badges/jacoco.svg?raw=true)
  ![Java](https://img.shields.io/badge/Java-1.8-green)

</div>

<h3>About</h3>

Jacketing Studio Scheduler is a multi-platform, parallelized task scheduler with a feature-rich search visualization tool. The program takes an weighted directed acyclic graph as input via a .dot file and finds the optimal schedule for the tasks across the set number of processors. The aim is to help large data centers efficiently schedule any task graph on any number of their computers/processors.

 
<h4>Features:</h4>

- Multi-threaded task scheduler
- Single-threaded option
- Visualization of search with diagnostic graphs (RAM, CPU)

<h3>Team</h3>

| Name            | GitHub                                            |
| --------------- | ------------------------------------------------- |
| James Coppard   | [nzbasic](https://github.com/nzbasic)             |
| Fraser McCallum | [frasermcc9](https://github.com/frasermcc9)       |
| Fuki Babasaki   | [Fuki-UoA](https://github.com/Fuki-UoA)           |
| Bruce Zeng      | [BruceZeng1](https://github.com/BruceZeng1)       |
| Brendon Joe     | [BraveNewWord](https://github.com/BraveNewWord)   |

<h3>Screenshots</h3>

<h3>Usage</h3>

To run the program, type `java -jar scheduler.jar <Input File.dot> <Number of Processors> [OPTIONS]`

```
java -jar scheduler.jar INPUT.dot P [OPTIONS]
INPUT.dot   A task graph with integer weights in dot format
P           Number of processors to schedule the INPUT graph on.

Optional: 
-p N        Use N cores for execution in parallel (default is sequential). 
-v          Visualise the search. 
-o          OUTPUT Output file is named OUTPUT (default is INPUT-output.dot).
```

<h3>Project Setup</h3>

Building locally requires Oracle Java 8 and Java 11 or later.

1. Clone Repo
2. In IntelliJ:
   Set Project SDK to Java 8 
   Set Gradle SDK to Java 11 or later. 
   Sync Gradle
3. Run com.jacketing.Entry


<h3>Wiki</h3>







