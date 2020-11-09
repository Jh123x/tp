---
layout: page
title: Joey Chen's Project Portfolio Page
---

## Project: McGymmy

McGymmy is a desktop diet tracker application for health-conscious software engineers.
The user interacts with it using a CLI, and it has a GUI created with JavaFX. It is written in Java, and has about 10 kLoC.

Given below are my contributions to the project.

* **Redesigned Feature**: Redesigned the logic component of McGymmy.
  * What I changed: Replaced the existing parser with the [Apache commons-cli](https://github.com/apache/commons-cli) library, and made the creation of commands follow
  a more declarative style. Also eliminated the need for parsers for each command.
  * Justification: I found that the old architecture was problematic as it required the programmer to create a lot of boilerplate just to achieve simple tasks.
  For more information, you can visit
  [the pull request](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/39).
  * Highlights: Developers now need to write about half the amount of code to get new features working. The code in each command class was also neater, and more intuitive by virtue of being more declarative.
  The design had to be carefully thought out to maximize developer productivity, and cost-benefit analysis had to be carried out for many design alternatives, including the original.
  The implementation was non-trivial, and required changing about 3 kLoC. The new API also had to be documented well enough for fellow developers to understand.
  * Credits: As mentioned above, this design made use of the [Apache commons-cli](https://github.com/apache/commons-cli) library for parsing text from the user, and automatically
  generating help text. All other work is solely by me.

* **New Feature**: Added the ability to create macros.
  * What it does: allows the user to create a 'macro' to run several commands in succession. Macros are saved on disk, and can be deleted in the CLI.
  * Justification: This feature allows users to automate routine, repetitive tasks by creating a macro or alias for it.
  * Highlights: This enhancement works for all commands in the app. The design had to be carefully crafted to ensure the implementation was easy to carry out and understand,
  and followed good design principles, and also to avoid bugs such as infinite loops. The implementation was also non-trivial, and required creating many different classes.
  * Credits: The design and implementation was inspired by [my ip](https://github.com/JoeyChenSmart/ip), but many design and implementation details were revisited and
  tweaked to fit with McGymmy (e.g. saving to storage). Both implementations also rely on the redesigned logic component above.
  
* **New Feature**: Added a help command.
  * Highlights: This feature leverages on the above redesigned logic component. Cleverly reusing information provided by the developer using the declarative style,
  this feature automatically generates help strings for each command. The design was carefully thought out to follow the DRY (don't repeat yourself) principle, to minimize
  bugs due to code duplication.
  * Credits: The idea, design and implementation were all carried out by me.
  
* **Code contributed**: [RepoSense link](https://nus-cs2103-ay2021s1.github.io/tp-dashboard/#breakdown=true&search=joeychensmart&sort=groupTitle&sortWithin=title&since=2020-08-14&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&tabOpen=true&tabType=authorship&tabAuthor=joeychensmart&tabRepo=AY2021S1-CS2103T-W17-3%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other)

* **Project management**:
  * Created the github organization for the group and project repository.
  * Created the issue label `code quality improvements` and several issues related to it (e.g. [\#214](https://github.com/AY2021S1-CS2103T-W17-3/tp/issues/214), [\#215](https://github.com/AY2021S1-CS2103T-W17-3/tp/issues/215).
  * Copied over draft UG from google docs to the repo. [\#22](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/22).

* **Enhancements to existing features**:
  * Fixed bug related to `RuntimeExceptions` with Jun Hua [\#216](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/216).

* **Documentation**:
  * User Guide:
    * Add documentation for the features `macro` [\#63](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/63), `listmacro` and `remmacro` [\#142](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/142)
    * * Note: I wrote the documentation for macro in the PR comment [\#63](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/63), and Jun Hua helped me copy it into the UG in another PR.
    * Added reader-friendly elaboration on command format: [\#93](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/93)
  * Developer Guide:
    * Added implementation details of the `macro` feature, and updated the section on the `logic` component under project architecture.

* **Community**:
  * PRs reviewed (with non-trivial review comments): [\#49](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/49), [\#53](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/53),
  [\#56](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/56), [\#97](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/97), 
  [\#104](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/104), [\#105](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/105), 
  [\#106](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/106)
  * Contributed to forum discussions: [Discussion on CI](https://github.com/nus-cs2103-AY2021S1/forum/issues/264)
  * Reported bugs and suggestions for other teams in the class (examples: [1](https://github.com/JoeyChenSmart/ped/issues/1), [2](https://github.com/JoeyChenSmart/ped/issues/2), [3](https://github.com/JoeyChenSmart/ped/issues/3), [4](https://github.com/JoeyChenSmart/ped/issues/4))

* **Tools**:
  * Integrated a third party library [Apache commons-cli](https://github.com/apache/commons-cli) to the project ([\#39](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/39))
