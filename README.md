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

- Single or Multi-threaded task scheduler
- Visualization of search with diagnostic graphs (RAM, CPU, Search Space, Best Schedules)

<h3>Team</h3>

| Name            | GitHub                                            |
| --------------- | ------------------------------------------------- |
| James Coppard   | [nzbasic](https://github.com/nzbasic)             |
| Fraser McCallum | [frasermcc9](https://github.com/frasermcc9)       |
| Fuki Babasaki   | [Fuki-UoA](https://github.com/Fuki-UoA)           |
| Bruce Zeng      | [BruceZeng1](https://github.com/BruceZeng1)       |
| Brendon Joe     | [BraveNewWord](https://github.com/BraveNewWord)   |

<h3>Screenshots</h3>

![image](https://user-images.githubusercontent.com/54062686/130344784-80f0228f-0160-43ab-ba3a-5dc9250ab622.png)

<h3>Usage</h3>

The visualization requires Oracle Java 8 to run! If you aren't using the visualization, you can use OpenJDK.

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

Building locally requires Oracle Java 8

1. Clone Repo
2. In IntelliJ:
   - Set Project SDK to Java 8 (if you have multiple Java installations)
   - Set Gradle SDK to Java 8 (if you have multiple Java installations)
   - Sync Gradle
3. Run com.jacketing.Entry

<h3>Wiki</h3>

[Click Here](https://github.com/SoftEng306-2021/project-1-project-1-team-17/wiki)







