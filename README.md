# prNet
A program used to find patterns in complex data-structures

## Content
- [About](#about)
- [How to use](#how-to-use)
- [What this project is and what it is not](#What-this-project-is-and-what-not)

## About
This is a pretty abstract java-program made to find patterns in data-structures like lists or any other iterable structure. This code is rather meant to be included in other projects than to be used on its own.

## How to use
 - [The database](#The-Database)
 - [ext-package](#ext-package)
 - [prNet-package](#prNet-package)
   - [Comparison.java](#Comparison)
   - [BasePattern.java](#BasePattern)
   - [Pattern.java](#Pattern)
   - [AspectManager.java](#AspectManager)
   - [PatternUsage.java](#PatternUsage)
   - [Assigner.java](#Assigner)
 - [prNet.manipulable-package](#prNet.manipulable-package)
   - [Transition.java](#Transition)
   - [ManipulablePattern.java](#ManipulablePattern)
   - [ManipulationUsage.java](#ManipulationUsage)
 - [demo-package](#demo-package)
   - [TwoDimensionalMatrix.java](#TwoDimensionalMatrix)
   - [MatrixDemo.java](#MatrixDemo)

### The Database
This project uses a sqlite database to store found patterns. To manage this, I used the sqlite-jdbc driver. I got it from [here](https://github.com/xerial/sqlite-jdbc). The in this repo contained jar file may not be up to date but it is the version I used to develop and test this project.

### ext-package
The "ext" package manages some general usable tools like a file- and a Blob-handler. The class General.java contains some needed but abstract, some usable and some unnecessary methods. You can use something from here but definitely don't need to.

### prNet-package
This is the interesting part. This package contains all necessary methods to actual search for and save patterns.

#### Comparison
This interface is meant to be used as a lambda expression to compare two elements in the structures that may contain patterns. This basically works like a BiPredicate but has different method names and, most important, are serializable.

#### BasePattern
This is a basic pattern. It contains a list of elements where a null value is treated like a "\*" in regex. It also contains a [Comparison](#Comparison) that defines, how two elements of the list are compared.

#### Pattern
This object extends from [BasePattern](#BasePattern) but implements a counter to know how often a pattern occured in the direct comparing process. To be clear, a zero in there means that it wasn't found in termes of comparing all found patterns, so in occurs once in the given data-set. 

#### AspectManager
The AspectManager is an easy tool to manage different aspect under which a data-structure should be analyzed. Most of the classes and method already use this tool to get the needed information they need beside the current set of data they will be analyzing.
To define such an aspect, use this method:<br>
 - #### *define(String name, Class<T> classType, Comparison<T, T> comparison, String table, Function<T, String> keyFunction, Statement stat)*<br>
   Parameters:

   - name: The name of an aspect to recognize it later
   - classType: This is optional. This here is just the class type of the infered object-type so it isn't necessary to cast any object within the method in the lambda expression
   - comparison: The [Comparison](#Comparison) used to compare two elements
   - table: The name of the table in the database in which all found patterns will be stored. If you don't call a method that writes some patterns into the database, this should be just set to something like "table"
   - keyFunction: A lambda expression used to convert an element into a string, used to create an unique key for all equal patterns
   - stat: The SQL-Statement used to connect with the database, if needed
 
To receive a single aspect manually, use the method
 - #### *getAspect(Class<T> classType, String name)*
   Parameters:

   - classType: As above just the class type of the infered object type
   - name: The name of the aspect you want to get
 
If a method doesn't require a comparison or a single aspect as a parameter, it will use all aspect which were defined up to this point.
 

#### PatternUsage
Most of the methods have a javadoc attached but here are the most important of them:

- #### *public static <T> List<Pattern<T>> findPatterns(Comparison<T, T> comparison, List<List<T>> analyzableElements)* <br>
  This method returns all patterns in the given two-dimensional list 'analyzeableElements'. Two different elements will be compared with the lambda-expression given in the 'comparison' parameter. This is the basic method to find patterns. The two-dimensional list can also be replaced by an object that implements the ArrangableToList- or the Spliterator-interface.

- #### *public static <T> Map<String, Double> findMatchingAuthors(List<Pattern<T>> text, Aspect<T> aspect, Connection conn, boolean ignoreLow, int mode, int min)*<br>
  This method finds all authors of patterns in the database which match with the given list of patterns.
  This can be done in 4 different ways which are represented with the "mode"-variable through the integers 0 to 3. The default is 2 and should always be used if you aren't sure you need another one.<br><br>

    The modes:

  * 0: counts the total amount of matching patterns in the database.
  * 1: counts the total amount of matching patterns in the database but only uses the highest of them later.
  * 2: counts the amount of patterns and then calculates the percentage of the matching pattern to all saved patterns by the author.
  * 3: counts the amount of patterns and then calculates the percentage of the matching pattern to all saved patterns by the
  author but only uses the highest of them later.
  Note: the "author" is the creator the given patterns and often defined before. If you don't know what to use here,
  you should look up how vou've analyzed what ever you did to get the patterns that are used here in the text-
  parameter.
   <br>

  Parameters:

     - text: The list of patterns that will be compared with all authors in the database
     - aspect: The aspect under which the patterns were analyzed
     - conn: The SQL-Connection to the database
     - ignoreLow: Decides if every pattern that was found only once should be ignored or not. This should be true if you test a structure that is already saved in the database because it's own patterns won't count in the result and therefore will be ignored afterwards. It's practically deletes all self-references.
     - mode: The above explained mode
 <br>
 
 
- #### *public static boolean match(List<T> environment, P pattern)*<br>
  This method checks if the pattern matches with the list 'environment'. Pretty much like simple regex.
 
- #### *public static void findAndSavePatterns(List<List<T>> analyzeableElements, String author, Statement stat, Aspect<T> aspect)*<br>
 
 
#### Assigner
A class used to assign values to a key. Some classes in this project extend from it to connect a special value to it if needed. This isn't a core part of this project but can be an useful extension.
 

### prNet.manipulable-package
 This package implements tools to manipulate lists and patterns.
#### Transition
 The Transition interface represents a lambda expression that tells how an element will be manipulated, if all necessary conditions match.
 
#### ManipulablePattern
 Another object that extends from the BasePattern class. Beside the standart-pattern-stuff this object keeps a [Transition](#Transition) and a special [Comparison](#Comparison) clause to tell if a [Transition](#Transition) can be applied.
 
 #### ManipulationUsage
 This class extended the [PatternUsage](#PatternUsage) by the method *manipulate(List<T> environment, ManipulablePattern<T> pattern)*. It first checks if the list 'environment' matches the pattern based on the Comparison clause and if it does, it manipulates those positions as the [Transition](#Transition) describes. In my last tests this achieved differences of ~3% to ~8% with the *findMatchingAuthors* method of the [PatternUsage](#PatternUsage) class.
 
 ### demo-package
 A package that contains a demonstration of how to apply parts of this project.
 
 #### TwoDimensionalMatrix
   Just a simple implementation of a two-dimensional-matrix which is build out of an array of nodes.
 
 #### MatrixDemo
   A demonstration of how the [PatternUsage](#PatternUsage) class can be used to analyze an array of random matrices.

 
## What this project is and what not
 This is the codebase for some of my projects which handle larger groups of information. It is designed to handle abstract data, rather than specific one.<br>
 It is not a ready-to-go tool to analyze files, complete programs or databases. If you want to apply this code to a larger scale of data-structures, go for it, as long as you respect the license.
