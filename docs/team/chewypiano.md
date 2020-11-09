---
layout: page
title: Kamil Gimik's Project Portfolio Page
---

## Project: McGymmy

McGymmy is a desktop diet tracker application for health-conscious software engineers.
The user interacts with it using a CLI, and it has a GUI created with JavaFX. It is written in Java, and has about 10 kLoC.

Given below are my contributions to the project.

* **New Feature**: Added the ability to find food items based on criteria. [\#105](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/105)
  * What it does: allows the user to specify certain optional criteria (name, tag, date) in order to filter the displayed food list.
  * Justification: This feature allows for greater navigability within the app as users can filter the food list.
  It also allows for more efficient execution of commands, by allowing the user to execute commands on a localised filtered
  list instead of the entire food list.
  * Highlights: This enhancement required changing the structure of `FindCommand`, amending the `PrimitiveCommandParser` to work
  with optional flag-less parameters as well as creating the `DatePredicate` class to filter by dates. A lot of thought went through to devise
  how to make the `FindCommand` optimally functional and useful.
  
* **Enhancement**: Major refactoring of codebase from AB3 to McGymmy. (Pull requests [\#56](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/56), [\#55](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/55))
  * What it does: sets up the core of the codebase for McGymmy and allows additional enhancements to be incorporated easily.
  * Highlights: Refactored entirety of AB3 to fit food items instead of contacts which involved changing
  thousands of lines of code. Partook in pair programming with [Junhua](jh123x), as a live code reviewer for the refactoring of core commands and overall structure.
  Personally, I refactored all existing AB3 test classes to fit McGymmy which required extensive and exhaustive debugging in order to ensure the "updated"
  tests passed while ensuring the logic remained correct and relevant.

* **Enhancement to existing features**:
  * Wrote additional tests for existing features to increase test coverage. (Pull requests [\#105](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/105), [\#135](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/135))  
  
* **Code contributed**: [RepoSense link](https://nus-cs2103-ay2021s1.github.io/tp-dashboard/#breakdown=true&search=chewypiano&sort=groupTitle&sortWithin=title&since=2020-08-14&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&tabOpen=true&tabType=authorship&zFR=false&tabAuthor=chewypiano&tabRepo=AY2021S1-CS2103T-W17-3%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code)

* **Project management**:
  * Set up and managed milestones v1.1 and v1.2 (2 Milestones).
  * Update shared docs with updated UI images.

* **Documentation**:
  * User Guide:
    * Updated `find` command description and examples. [\#135](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/135)
    * Added in more user-friendly language and formatting. (Pull requests [\#236](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/236), [\#209](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/209))
    * Updated and added UI images. [\#149](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/149)
  * Developer Guide:
    * Added `find` command with associated details and UML diagrams. [\#106](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/106)
    * Updated model class diagram to fit McGymmy. [\#114](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/114)

* **Community**:
  * PRs reviewed (with non-trivial review comments): [\#216](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/216),
  [\#101](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/101), [\#246](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/246), [\#143](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/143),
  [\#49](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/49), [\#219](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/219),
  [\#229](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/229). Full list of PR's reviewed: [PRs](https://github.com/AY2021S1-CS2103T-W17-3/tp/pulls?q=is%3Apr+reviewed-by%3A%40me).
  * Reported bugs and suggestions for other teams in the class (examples: [1](https://github.com/chewypiano/ped/issues/1), [2](https://github.com/chewypiano/ped/issues/2), [3](https://github.com/chewypiano/ped/issues/3), [4](https://github.com/chewypiano/ped/issues/4))
