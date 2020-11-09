---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## 1. **Purpose of this guide**
This guide aims to help developers understand how McGymmy (MG) works.
One of the major goals is to centralise all the McGymmy documentation within McGymmy itself.
Another major goal is to better help developers extend McGymmy to improve on its features.

##  2. Icon Legend
<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** This indicates that the following text consists of tips to better utilise MG

</div>
<div markdown="span" class="alert alert-info">

:information_source: **Note:** This indicates important notes for current feature we are looking at<br>

</div>

## 3. A little note from the developers

Thank you for using MG. We sincerely hope that MG plays a part to help you achieve a healthier lifestyle.
We understand that MG may be a little different from traditional GUI applications and it may be a little difficult to start using it initially.
Thus, we recommend you to start by first understanding how MG works at a high level at the Quick start section. Then, explore the Features section.

<div markdown="block" class="alert alert-info">

:bulb: **Tip:** Press Ctrl-F to open the find prompt in your browser. You can type in keywords such as `add` or `edit` to quickly navigate to those parts of the document.

</div>

## 4. **General Information**
McGymmy (MG) is a **desktop app for managing diet and exercise, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, McGymmy can log your diet and exercise tasks, goals and progress done faster than traditional GUI apps.

## 5. **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------


## 6. **Design**

### 6.1 Architecture

<img src="images/ArchitectureDiagram.png" width="450" alt="Architectural diagram"/>

The ***Architecture Diagram*** given above explains the high-level design of the App. Given below is a quick overview of each component.

**`Main`** has two classes called [`Main`](https://github.com/AY2021S1-CS2103T-W17-3/tp/tree/master/src/main/java/jimmy/mcgymmy/Main.java) and [`MainApp`](https://github.com/AY2021S1-CS2103T-W17-3/tp/tree/master/src/main/java/jimmy/mcgymmy/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

Each of the four components,

* defines its *API* in an `interface` with the same name as the Component.
* exposes its functionality using a concrete `{Component Name}Manager` class (which implements the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component (see the class diagram given below) defines its API in the `Logic.java` interface and exposes its functionality using the `LogicManager.java` class which implements the `Logic` interface.

![Class Diagram of the Logic Component](images/LogicClassDiagram.png)

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" alt="Architecture Sequence Diagram"/>

The sections below give more details of each component.

### 6.2 UI component

![Structure of the UI Component](images/UiClassDiagram.png)

**API** :
[`Ui.java`](https://github.com/AY2021S1-CS2103T-W17-3/tp/tree/master/src/main/java/jimmy/mcgymmy/ui/Ui.java)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `FoodListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class.

The `UI` component uses JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2021S1-CS2103T-W17-3/tp/tree/master/src/main/java/jimmy/mcgymmy/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2021S1-CS2103T-W17-3/tp/tree/master/src/main/resources/view/MainWindow.fxml).

The `UI` component,

* Executes user commands using the `Logic` component.
* Listens for changes to `Model` data so that the UI can be updated with the modified data.

### 6.3 Logic component

![Structure of the Logic Component](images/LogicClassDiagram.png)

**API** :
[`Logic.java`](https://github.com/AY2021S1-CS2103T-W17-3/tp/tree/master/src/main/java/jimmy/mcgymmy/logic/Logic.java)

We omit details of the `Macro` component in this section and will only focus on the parsing and execution of non-macro `Command`s.
For more information on macros and the full parsing process please read the section below.

1. `Logic` uses the `McGymmyParser` class to parse the user command.
1. This results in a `CommandExecutable` object which is executed by the `LogicManager`.
1. The command execution can affect the `Model` (e.g. adding a food item).
1. The result of the command execution is encapsulated as a `CommandResult` object which is passed back to the `UI`.
1. In addition, the `CommandResult` object can also instruct the `UI` to perform certain actions, such as displaying help to the user.

Note that our implementation of this component is different from the one in the parent project, [AddressBook3](https://se-education.org/addressbook-level3/).
One notable difference is the use of several `Parameter` classes in the various `Commands`.
We list a few of the benefits of our redesigned solution below.

1. The main motivation for this change was to facilitate the addition of the `macro` feature.
2. The old design of the parser operated at too high an abstraction level, resulting in the developer having to create boilerplate objects like the `EditPersonDescriptor` to implement basic functionality.
3. The new design allows us to code in a more declarative style which is more readable and arguably "self-documenting".
4. Hiding details of each `Parameter` behind an additional layer of abstraction allows us to automate the creation of other features such as parsers for each individual `Command` and automatially generating `help` strings.

Given below is the Sequence Diagram for interactions within the `Logic` component for the `execute("delete 1")` API call.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

For more details on the new architecture you can refer to this [pull request](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/39).
In particular, details on how to create new `Command`s are included in the pull request above.

### 6.4 Model component

![Structure of the Model Component](images/ModelClassDiagram.png)

**API** : [`Model.java`](https://github.com/AY2021S1-CS2103T-W17-3/tp/tree/master/src/main/java/jimmy/mcgymmy/model/Model.java)

The `Model`,

* stores a `UserPref` object that represents the user’s preferences.
* stores the McGymmy data.
* exposes an unmodifiable `ObservableList<Food>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* does not depend on any of the other three components.


<div markdown="span" class="alert alert-info">
   
:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in `McGymmy`, which `Food` references. This allows `McGymmy` to only require one `Tag` object per unique `Tag`, instead of each `Food` needing their own `Tag` object.<br>

![BetterModelClassDiagram](images/BetterModelClassDiagram.png)

</div>


### 6.5 Storage component

![Structure of the Storage Component](images/StorageClassDiagram.png)

**API** : [`Storage.java`](https://github.com/AY2021S1-CS2103T-W17-3/tp/tree/master/src/main/java/jimmy/mcgymmy/storage/Storage.java)

The `Storage` component,
* can save `UserPref` objects in JSON format and read it back.
* can save the McGymmy data in JSON format and read it back.

### 6.6 Common classes

Classes used by multiple components are in the `jimmy.mcgymmy.commons` package.

--------------------------------------------------------------------------------------------------------------------

## 7. **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### 7.1 Clear feature

The clear feature extends model with the ability to delete all the items shown in the UI.

#### 7.1.1 Implementation

Given below is an example usage scenario and how the clear mechanism works

Step 1. The user has decided that he wants to delete all filtered items based on taggings or names.

Step 2. The user uses the find command to search for the items to delete.

Step 3. The User executes `clear`.
The clear command will update the model to delete every occurences of food shown on the UI.
Otherwise, a CommandException will be thrown.

Step 4. Storage is then updated to be the same as model.

The following sequence diagram shows how the clear operation works:

![ClearSequenceDiagram](images/ClearSequenceDiagram.png)

<div markdown="span" class="alert alert-info">
   
:information_source: **Note:** The method <code>Parser::parse</code> is a simplification of the overall parsing sequence which was already covered in showcasing the execution of the delete method. As such, redundant parsing details are not covered here.

</div>

#### 7.1.2 Design Considerations

##### 7.1.2.1 Aspect: How clear executes

* **Alternative 1 (Current Choice):** A refilter using predicate into a list and reupdate food items.

    * Pros: No traversal issue and no issue when food definition changes. Good time and space complexity.
    * Cons: Hard to implement

* **Alternative 2:** An enhanced for loop can be used to remove all occurences straight from the list.

    * Pros: Cleaner code.
    * Cons: Code may break if food properties ever changes due to traversal error. Also, has larger time and space complexity.


### 7.2 Find feature

#### 7.2.1 Implementation

The find mechanism is facilitated by `ModelManager`. It keeps a filtered list (updated via a specified `Predicate`) of food items,
stored internally as a `FilteredList<Food>`. Additionally, it implements the following relevant operation:

* `ModelManager#updateFilteredFoodList(Predicate<Food>)` - Filters internal storage via a `Predicate<Food>` specification.

Given below is an example usage scenario and how the find mechanism behaves at each step.

Step 1. The user launches the application after having used it for a while.

Step 2. The user wants to find food with apple in its name and fruit in its tag.

Step 3. The user executes `find -n apple -t fruit`. The `find` command will check if the inputs are valid, and then parsed (similar to `delete`)
before using these inputs to create conditional `Predicate<Food>` instances (eg. `NameContainsKeywordsPredicate`, `DatePredicate`). The predicates
are then combined and used to filter the `FilteredList<Food>`. The GUI will then display the food items in the filtered list.

The following sequence diagram shows how the find operation works:

![FindSequenceDiagram](images/FindSequenceDiagram.png)

<div markdown="span" class="alert alert-info">
   
:information_source: **Note:** The method <code>Parser::parse</code> is a simplification of the overall parsing sequence which was already covered in showcasing the execution of the delete method. As such, redundant parsing details are not covered here.

</div>

#### 7.2.2 Design consideration:

##### 7.2.2.1 Aspect: Arguments for FindCommand

* **Alternative 1 (current choice):** Completely optional parameters, and ability to `find item` without a `flag`.
    * Pros: More flexibility for user, not restricted by parameter requirements.
    * Cons: Harder to implement, breaks general patten of `flag`-less arguments being compulsory parameters.

* **Alternative 2:** Compulsory `flag`-less name parameter
    * Pros: Easier to implement.
    * Cons: Less user-friendly, user restricted in use of method

##### 7.2.2.2 Aspect: How find works

* **Alternative 1 (current choice):** Filters Food with matching instance of at least one (assuming multiple arguments) argument for a given parameter
    * Pros: Easier to implement and easier to locate more things quickly.
    * Cons: Harder to locate specific food items.

* **Alternative 2:** Filters Food with matching instance of all arguments for a given parameter
    * Pros: Can locate specific food items easily; more narrowed-down search.
    * Cons: More difficult to find something over broad terms.

* **Alternative 3 (proposed best choice):** Allow variation in type of filtering through another optional parameter.
    * Pros: Combined Pros of Alternative 1 and 2, gives user greater autonomy.
    * Cons: Harder to implement, harder to use effectively.

### 7.3 Import feature

The current Import feature is facilitated by `JsonMcGymmyStorage`.
It extends model with the ability to override the current data with an imported one.

#### 7.3.1 Implementation

Given below is an example usage scenario and how the import mechanism behaves at each step.

Step 1. The User just installed his application on his new computer and wants to transfer his old data over.

Step 2. The user transfers his previous save file to `C:/McGymmy/saveFile.json`.

Step 3. The User executes `import c:/McGymmy/saveFile.json`.
The import command will check if the file is valid and exists before calling `JsonMcGymmyStorage`.
`JsonMcGymmyStorage` will call `#readMcGymmy` if the read is successful, the old data will be overwritten.
Otherwise, a CommandException will be thrown.

Step 4. His previous data files will be overwritten and updated.

The following sequence diagram shows how the import operation works:

![ImportSequenceDiagram](images/ImportSequenceDiagram.png)

<div markdown="span" class="alert alert-info">
   
:information_source: **Note:** The method <code>Parser::parse</code> is a simplification of the overall parsing sequence which was already covered in showcasing the execution of the delete method. As such, redundant parsing details are not covered here.

</div>

#### 7.3.2 Design Considerations

##### 7.3.2.1 Aspect: How import executes

* **Alternative 1 (Current Choice):** User keys in the filepath of the save file

    * Pros: Easy to implement
    * Cons: Requires the user to key in path to file

* **Alternative 2:** User keys in path and McGymmy checks subdirectories for valid files

    * Pros: More convenient for the users
    * Cons: Increased implementation complexity (Prone to bugs), May have performance issues in terms of runtime.



### 7.4 Undo feature

#### 7.4.1 Implementation

The proposed undo mechanism is facilitated by `ModelManager` and `History`.

`History` combines `ModelManager`'s `McGymmy`, `Predicate<Food>` and `MacroList` together, then store multiple groups of different versions in a stack, with the most recent version on top.

Whenever there is a change to either `ModelManager`'s data, filter predicate, or macro list, `ModelManager` will pass itself into `History` to be saved into the stack.
Additionally, `ModelManager` implements the following operations:

* `ModelManager#canUndo()` - Checks if there is any older state stored in history.
* `ModelManager#undo()` - Restores the previous McGymmy state from its history.
* `ModelManager#saveCurrentStateToHistory()` - Saves the current McGymmy state in its history.

The first 2 operations are exposed in the `Model` interface as `Model#canUndo()` and `Model#undo()` respectively.

Given below is an example usage scenario and how the undo mechanism behaves at each step.

<div markdown="span" class="alert alert-info">
   
:information_source: **Note:** The type <code>ModelManager**</code> of the objects stored in <code>History</code>  in the following diagrams is used as a simplified presentation for <code>Pair&lt;McGymmy, Pair&lt;Predicate&lt;Food&gt;, MacroList&gt;&gt;</code>, so that the diagrams look less complicated.

</div>

Step 1. The user launches the application for the first time. The `ModelManager` will be initialized with the empty `History`.

![UndoState0](images/UndoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th food item in the McGymmy.
The `delete` command calls `ModelManager#deleteFood(Index)`, which calls `ModelManager#addCurrentStateToHistory()`,
causing the stack to store a copied version of the McGymmy before any data changes happen.
Then McGymmy changes accordingly to delete the food at index 5.

![UndoState1a](images/UndoState1a.png)

![UndoState1b](images/UndoState1b.png)

Step 3. The user executes `add -n Rice …​` to add a new food item. The `add` command calls `ModelManager#addFood(Food)`, which also calls `ModelManager#addCurrentStateToHistory()`,
storing a copied version of the McGymmy into the stack before changing the McGymmy.

![UndoState2a](images/UndoState2a.png)

![UndoState2a](images/UndoState2b.png)

<div markdown="span" class="alert alert-info">

:information_source: **Note:** If a command fails its execution, it will not call `ModelManager#addCurrentStateToHistory()`, so the McGymmy state will not be saved into the `mcGymmyStack`.

</div>

Step 4. The user now decides that adding the food item was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `ModelManager#undo()`,
which will pop the top most state from `mcGymmyStack`, and restores the McGymmy to that state.

![UndoState3](images/UndoState3.png)

<div markdown="span" class="alert alert-info">
   
:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial McGymmy state, then there are no previous McGymmy states to restore. The `undo` command uses `Model#canUndoMcGymmy()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">
   
:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

<div markdown="span" class="alert alert-info">

:information_source: **Note:** The method <code>Parser::parse</code> is a simplification of the overall parsing sequence which was already covered in showcasing the execution of the delete method. As such, redundant parsing details are not covered here.
   
</div>

<div markdown="span" class="alert alert-info">
   
:information_source: **Note:** The method <code>ModelManager::updateModelManager</code> is a simplification of updating the McGymmy, updating the filterPredicate and updating the filteredFoodList separately.

</div>

The following activity diagram summarizes what happens when a user executes a new command:

![AddToHistoryActivityDiagram](images/AddToHistoryActivityDiagram.png)

#### 7.4.2 Design consideration:

##### 7.4.2.1 Aspect: How undo executes

* **Alternative 1 (current choice):** Saves the entire ModelManger database.
    * Pros: Easy to implement.
    * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo by itself.

    * Pros: Will use less memory (e.g. for `delete`, just save the food item being deleted).
    * Cons: We must ensure that the implementation of each individual command is correct.

### 7.5 Macro Command

![Structure of the Macro Component](images/MacroClassDiagram.png)

<div markdown="span" class="alert alert-info">

As with the other class diagram for the *Logic* component above, we omit some details in the above diagram for clarity.

</div>

Macros are a way for users to create shortcuts to run several commands in succession, with arguments. For more information please refer to the [user guide](https://ay2021s1-cs2103t-w17-3.github.io/tp/UserGuide.html).

There are two 'parts' to the macro component:

1. The creation of the macro object itself (e.g. running `macro test a; add -n \a breakfast -c 100`).
2. The execution of the macro object (e.g. running `test -a tau sar pau`).

The creation of the macro object is straightforward as we defer most of the heavy lifting to during the execution of the macro.

#### 7.5.1 A more complete picture of the parsing process

We only described parsing and execution of primitive commands in the section above.
A short description of the process including the parsing of macros is as follows:

1. If command word is `macro`, return a `NewMacroCommand` (which will create a new `macro` when executed).
2. Else if command word is an existing macro, call the `asCommandInstance` method in the `MacroRunner` class to create a `CommandExecutable` from the macro and return that.
3. Else if command word is a primitive command, pass control over to primitive command parser.
4. Otherwise, a `ParseException` will be thrown.

#### 7.5.2 Structure of the macro classes

The main data objects for macros are the `Macro` and `MacroList` classes. These are stored in the `Model`.

`Macro` is an immutable container containing data such as the name, as well as arguments and commands that would be executed in the macro as strings.

`MacroList` is an immutable container of `Macro`s.

Lastly, the `MacroRunner` utility class in the `Logic` component is responsible for turning these `Macro` data objects into `CommandExecutable` objects that can be run.

#### 7.5.3 Execution of macros

Here we detail what exactly happens in each macro's `toCommandExecutable` object.
Throughout this section, we will use the following macro as an example: `macro test a;add -n \a breakfast -c 200`.

1. Parameters supplied by the user are substituted into the macro's lines using regular expressions. For the example if the user enters `test -a rice`, `rice` will be substituted into the `\a` part of `add -n \a breakfast -c 200` to obtain `add -n rice breakfast -c 200`;
2. Pass the list of strings to a new instance of `PrimitiveCommandParser` to get a list of `Command`s. If a `ParseException` occurs, we recast it as a `CommandException` and re-throw it.
3. Execute all these `Command`s in sequence, and return a new `CommandResult` created from concatenating the respective `CommandResults` returned by the `Command`s. If a `CommandException` occurs in this sequence, that exception is thrown with the added message listing the commands that have executed successfully, and commands that have yet to be executed.

Below is a sequence diagram summarizing the main interactions.

![Sequence diagram for macro](images/MacroSequenceDiagram.png)

##### 7.5.4 Aspect: Design alternatives

* **Alternative 1:** 'Compile' a macro into a `Command` using the existing `ParameterSet` classes.
    * Pros: Allows us to check for more potential errors in the macro's format.
    * Cons: Harder to implement and more complicated.

* **Alternative 2: (Current Choice)** Use string substitution along with another parser instance to run commands.
    * Pros: Much simpler implementation and less prone to bugs.
    * Cons: Some errors in the user's macro will only be detected when the macro is being run.
 
--------------------------------------------------------------------------------------------------------------------

## 8. **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## 9. **Appendix: Requirements**

### 9.1 Product scope

**Target user profile**:

* has a need to manage food intake
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps
* is sedentary people who sit in front of their computers

**Value proposition**:
* manage food intake faster than a typical mouse/GUI driven app
* reduce the risk of health issues for people with an unhealthy lifestyle
* help them develop a healthy lifestyle through diet
* help them track their calorie intake
* find trends in their eating habits
* profile setting for them to store different settings


### 9.2 User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a/an …​                              | I want to …​                        | So that I can…​                                                     |
| -------- | ------------------------------------------ | -------------------------------------- | ---------------------------------------------------------------------- |
| `* * *`  | new user                                   | see usage instructions                 | refer to instructions when I forget how to use the App                 |
| `* * *`  | user                                       | track what I am eating                 |                                                                        |
| `* * *`  | user                                       | delete a food item                     | remove food items that I no longer care of                             |
| `* * *`  | user                                       | easily add food items/details          | keep track of my diet                                                  |
| `* * *`  | organised user                             | easily filter my food items            | find food items based on criteria                                      |
| `* * *`  | user                                       | easily export my data                  | transfer my old data to a new computer or as backup                    |
| `* * *`  | user                                       | easily load my data                    | restore the previous backups i have made                               |
| `* * *`  | user                                       | undo my functions                      | fix my accidental mistakes in my commands                              |
| `* *`    | frequent user                              | perform tasks on a group of food items |                                                                        |
| `* *`    | organised user                             | be able to clear food items            | reduce excess clutter within the application                           |
| `* *`    | expert user                                | create shortcuts for tasks             | do frequently performed tasks faster                                   |
| `* *`    | expert user                                | easily view the shortcuts I created    | remember what they do if I forget                                      |
| `* *`    | expert user                                | delete the shortcuts I created         | remove shortcuts I no longer use                                       |
| `* *`    | user                                       | tag my items for identification        | so that I can find them easily                                         |
| `* *`    | user                                       | untag my items                         | in case I made a mistake in tagging                                    |
| `* *`    | user                                       | view detailed statistics               | better keep track of my diet                                           |
| `*`      | programmer                                 | make use of the CLI like design        | get used to CLI in the future                                          |
| `*`      | programmer                                 | be able to customise the application   | improve my workflow and make it faster                                 |
| `*`      | programmer                                 | automate repetitive tasks              | save time and energy                                                   |
| `*`      | programmer                                 | shorten my commands                    | remember them better                                                   |
| `*`      | dietician                                  | view my client's diet                  | ensure that they are eating healthily                                  |
| `*`      | an underweight individual                  | track my caloric count                 | ensure that I am eating at a surplus                                   |
| `*`      | an overweight individual                   | track my caloric count                 | ensure that I am eating at a deficit                                   |
| `*`      | an student athlete                         | track my protein intake                | ensure that my diet promotes recovery                                  |
| `*`      | a diabetic individual                      | find trends of sugary food with tags   | track what I ate that spikes my insulin level                          |
| `*`      | a careless user with typos                 | find food with wrong macro values      | correct inaccurate inputs                                              |
| `*`      | a minimalistic user                        | find outdated food items               | clear them from the list                                               |

### 9.3 Use cases

(For all use cases below, the **System** is `McGymmy` and the **Actor** is the `user`, unless specified otherwise)


**Use case: UC01 List food**

**MSS**
1. User requests to list food (UC01).
2. McGymmy shows a list of food that user has added (UC02).<br>
Use case ends.

**Use case: UC02 Add food**

**MSS**
1. User requests to add food into the list.
2. McGymmy adds the food item into the list.<br>
Use case ends.

**Extensions**
- 1a. The format of add method is invalid.
    - 1a1. McGymmy shows an error message.<br>
    Use case ends.

**Use case: UC03 Delete food**

**MSS**
1. User requests to list food. (UC01)
2. McGymmy shows a list of food.
3. User request to delete a specific food on the list.
4. McGymmy deletes the food.<br>
Use case ends.

**Extensions**
- 2a. The list is empty.<br>
    Use case ends.

- 3a. The given index is invalid.<br>
   - 3a1. McGymmy shows an error message.<br>
   Use case resumes at step 2.

**Use case: UC04 Help**

**MSS**
1. User requests help.
2. McGymmy shows all commands, and the usage of each command.<br>
Use case ends.

**Extensions**
<br>
- 1a. User passes a command as argument into help command.
    - 1a1. Help will show the argument to the command, and the usage of the command.<br>
    Use case end.<br>
- 1b. User passes an invalid command as argument into help command.<br>
    - 1b1. McGymmy will throw an error.<br>
    Use case ends.<br>

**Use case: UC05 Edit food**

**MSS**
1. User requests to list food (UC01).
2. McGymmy shows a list of food.
3. User request to edit a specific food on the list.
4. McGymmy updates the food.
Use case ends.

**Extensions**
- 2a. The list is empty<br>
Use case ends.

- 3a. The given index is invalid.<br>
   - 3a1. McGymmy shows an error message.<br>
   Use case resumes at step 2.

- 3b. The format of edit method is invalid.
   - 3b1. McGymmy shows an error message.<br>
   Use case ends.
    
**Use case: UC06 Tag food**

**MSS**
1. User requests to list food (UC01).
2. McGymmy shows a list of food.
3. User request to tag a specific food on the list.
4. McGymmy updates the food with the given tag.<br>
Use case ends.

**Extensions**
- 2a. The list is empty.<br>
   Use case ends.
    
- 3a. The given index is invalid.<br>
   - 3a1. McGymmy shows an error message.<br>
   Use case resumes at step 2.

- 3b. The given tag already exists for the selected food.<br>
   - 3a1. McGymmy shows an error message.<br>
   Use case resumes at step 2.

- 3c. The format of tag method is invalid.
   - 3c1. McGymmy shows an error message.<br>
   Use case ends.
    
**Use case: UC07 Untag food**

**MSS**
1. User requests to list food (UC01).
2. McGymmy shows a list of food.
3. User request to remove tag from a specific food on the list.
4. McGymmy removes the tag from the food.<br>
Use case ends

**Extensions**
- 2a. The list is empty.<br>
   Use case ends.

- 3a. The given index is invalid.<br>
   - 3a1. McGymmy shows an error message.<br>
   Use case resumes at step 2.

- 3b. The given tag does not exist for the selected food.<br>
   - 3a1. McGymmy shows an error message.<br>
   Use case resumes at step 2.

- 3c. The format of untag method is invalid.
   - 3c1. McGymmy shows an error message.<br>
   Use case ends.
    
**Use case: UC08 Find**

**MSS**
1. User requests to find (filter) food items based on certain criteria.
2. McGymmy shows an updated list based on the filter criteria.
Use case ends.

**Extensions**
- 1a. The format of find method is invalid.
   - 1a1. McGymmy shows an error message.
    Use case ends.
    
**Use case: UC09 Clear**

**MSS**
1. User filters list (UC06).
2. User requests to clear the displayed list.
3. McGymmy deletes all food items in the current displayed list.
Use case ends.

**Extensions**
- 1a. The list is empty.<br>
    Use case ends.

**Use case: UC10 Import a McGymmy data file**

**MSS**

1. User imports McGymmy file to McGymmy.
2. McGymmy overrides the old files with the new McGymmy files.<br>
Use case ends.

**Extensions**
 - 1a. The format for the save file is invalid.
    - 1a1. McGymmy shows an error message.<br>
    Use case ends.
 - 1b. The path to the save file is invalid.
    - 1b1. McGymmy shows an error message.<br>
    Use case ends.
    
**Use case: UC11 Export a McGymmy data file**

**MSS**

1. User exports his McGymmy file.
2. McGymmy creates a copy of his MyGymmy file in the selected directory.<br>
Use case ends.

**Extensions**
 - 1a. The path to directory is invalid.
    - 1a1. McGymmy shows an error message.<br>
    Use case ends.
    
**Use case: UC12 Add a macro command**

**MSS**

1. User creates a macro to execute several commands in sequence.
2. McGymmy adds the macro to the list of available commands.<br>
Use case ends.

**Extensions**

 - 1a. The format of the macro is invalid.
   - 1a1. McGymmy shows an error message.<br>
   Use case ends.
    
**Use case: UC13 Execute a macro command**

**MSS**

1. User executes a macro command.
2. McGymmy executes the commands in the macro command as if the user entered them one by one.<br>
Use case ends.

**Extensions**

 - 1a. The macro does not exist.
    - 1a1. McGymmy shows the error message.<br>
    Use case ends.

 - 2a. One of the executed commands encounter an error.
    - 2a1. McGymmy shows the error message from that command, and displays the commands that successfully executed, and the commands that have yet to execute.<br>
    Use case ends.

**Use case: UC14 Undo the last command**

**MSS**

1. User requests to undo the last command.
2. McGymmy successfully undo the last command.<br>
Use case ends.

**Extensions**

- 1a. McGymmy shows the error message that there is no command left to undo.<br>
    Use case ends.

**Use case: UC15 List all macro commands**

 - Similar to **UC04** except McGymmy lists *macros* instead of *commands*.<br>

**Use case: UC16 Remove a macro**

 - Similar to **UC03** except the user requests to list *macros* instead of *food items*, and requests to delete a *macro* instead of a *food item*.<br>

### 9.4 Non-Functional Requirements

1. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
1. There should be CLI alternatives to every GUI input. (E.g. instead of clicking on a button I can type an equivalent command to achieve the same result.)
1. Should be able to hold up to 1000 food items without a noticeable sluggishness in performance for typical usage.
1. Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
1. The data should be stored locally and should be in a human editable text file.
1. The product should be for a single user i.e. (not a multi-user product).
1. Each Command entered by the user should be processed within 1 second.
1. All numeric values must be accurate to within 5 significant figures.
1. The size of the final compiled JAR file should be less than 100Mb.
1. The software should not depend on a private remote server.


### 9.5 Glossary

* **Built-in commands**: Any other commands except Macro Command.
* **Command Line Interface (CLI)**: A command-line interface (CLI) processes commands to a computer program in the form of lines of text.
* **Flag**: A dash followed by a letter to denote the start of a parameter. For example, in `-n Chicken Rice`, `-n` is a flag for name and `Chicken Rice` is the input that will be processed by the application.
* **Graphical User Interface (GUI)**: A visual way of interacting with a computer using items such as windows, icons, and menus, used by most modern operating systems.
* **Macro command**: A command that allows you to execute multiple built-in commands sequentially with just one line of input.
* **Mainstream OS**: Windows, Linux, Unix, OS-X.
* **McGymmy/MG**: The name of this application, can be used interchangeably in this guide.
* **Parameter**: Inputs given to the flag. For example, in `-n Chicken Rice`, `Chicken Rice` is the parameter passed to the flag `-n`.

--------------------------------------------------------------------------------------------------------------------

## 10. **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">
   
:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### 10.1 Launch and shutdown

1. Initial launch.

   1. Download the JAR file and copy into any folder.

   1. Double-click the JAR file.
      <br>
      Expected: Shows the GUI with a set of sample food items. The window size may not be optimum.

1. Saving window preferences.

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the JAR file.
      <br>
      Expected: The most recent window size and location is retained.

### 10.2 Deleting food items

1. Deleting a food item while all food items are shown.

   1. Prerequisites: List all food items using the `list` command. Multiple food items in the list.

   1. Test case: `delete 1`.<br>
      Expected: First food item is deleted from the list. Details of the deleted food item shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`.<br>
      Expected: No food item is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size).<br>
      Expected: Similar to previous.


### 10.3 Saving data

1. Dealing with missing/corrupted data files.

   1. Delete the 'data' file if any. Relaunch the app by double-clicking the JAR file.
   <br>
   Expected: A new data file is generated.

   1. Open the data file inside the `data` folder using any text editor and edit the file to invalid values.
   <br>
   Expected: A new empty data file is generated which overwrites the old one.

