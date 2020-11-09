---
layout: page
title: Sean Chan's Project Portfolio Page
---

## Project: McGymmy
This portfolio aims to document the contributions that I have made to the McGymmy project. McGymmy can be found [here](https://github.com/AY2021S1-CS2103T-W17-3/tp/releases).

## Overview
McGymmy is a desktop diet tracking application to provide a simple an easy way for users to track their diet. The user interacts with it using a CLI, and it has a GUI created with JavaFX. It is written in Java, and has about 10 kLoC.
Given below are my contributions to the project.

## Code contributions
* **New Feature**: Enhanced the clear function to remove food items as shown in the list. [\#104](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/104)
  * What it does: allows the user to remove specific items based on tags, names or date.
  * Highlights: This update allows user the easy removal of food specific food groups.

* **New Feature**: Created food and macro for the model. [\#49](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/87) and export files [\#49](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/94).
  * What it does: Lays the groundwork for future development of the application as UI and parsers require food item to work.
  * Highlights: Allows the user to properly create food items in McGymmy.
  
* **New Feature**: Did pair programming with [Junhua](jh123x.html) to add a `PieChart` with summary values along with the labels for total calories. [\#80](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/80/)
  * What it does: The `PieChart` shows the percentage and weight of the different MacroNutrients as well as the total calories consumed 
  * Justification: The Visualisation of the data allows the user to get a rough idea how much he had consumed, compared to just staring at numbers. It also provides more visual impact to the user and give them a sense of progress.
  * Highlights: The animations of the PieChart was buggy and does not work as expected. As such we disabled the animation.
  * Credits: *JavaFx Library and Documentation*

* **Code contributed**: [Commits](https://github.com/AY2021S1-CS2103T-W17-3/tp/commits/master?author=dcchan98), [RepoSense](https://nus-cs2103-ay2021s1.github.io/tp-dashboard/#breakdown=true&search=dcchan98&sort=groupTitle&sortWithin=title&since=2020-08-14&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&tabOpen=true&tabType=zoom&zA=dcchan98&zR=AY2021S1-CS2103T-W17-3%2Ftp%5Bmaster%5D&zACS=65.23076923076923&zS=2020-08-14&zFS=dcchan98&zU=2020-11-05&zMG=false&zFTF=commit&zFGS=groupByRepos&zFR=false) 

## Other Contributions

* **Project management**:
  * Managed releases `v1.3 trial` and `v1.3` (2 releases) on GitHub

* **Enhancements to existing features**:    
  * **Add Defensive elements**: Made use of assertions to ensure proper caloric amount for macronutrients [\#49](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/49)
    * What it does: It only allows specific value of different MacroNutrients to be keyed in.
    * Justification: As there are only 3 main form of macronutrients our application allows, we want to make sure that the values we add corresponds to these macronutrients exact value.
    
  * **Add Defensive Elements**: Add test for model manager for clear and find command [\#229](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/229)
    * What it does: Ensures the correctness of the model manager on the backend.
    * Justification: Find and clear has to work hand in hand because clear removes the items created by find in model manager's list. This test is thus imperative to ensure there are no regressions should changes be made there in future.
    
  * **Add Defensive elements**: Add relevant test for food [\#57](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/57)
    * What it does: Ensure the correctness of the food class.
    * Justification: The correctness of food is important as the application builds on this.
    * Highlights: Tests detected a major error whereby a food item incorrectly asserts that it has to have more than 0 calories.

* **Documentation**:
  * User Guide:
    * Streamlined entire document to make it user-friendly on front matters [\#93](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/93)
  * Developer Guide:
    * Added implementation details of the `clear` feature. [\#143](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/143)

* **Community**:
    * PRs reviewed (with non-trivial review comments): [\#49](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/49), [\#56](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/56),


