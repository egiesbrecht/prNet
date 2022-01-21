# prNet
A program used to find patterns in complex data-structures

## Content
- [About](#about)
- [How to use](#how-to-use)

## About
This is a pretty abstract java-program made to find patterns in data-structures like lists or any other itarable structure. This code is rather meant to be included in other projects than to be used on its own.

## How to use
 - [ext-package](#ext-package)
 - [prNet-package](#prNet-package)
 - [prNet.manipulable-package](#prNet.manipulable-package)
 - [demo-package](#demo-package)

### ext-package
The "ext" package manages some general usable tools like a file- and a Blob-handler. The class General.java contains some needed but abstract, some usable and some unnecessary methods. You can use something from here but definitely don't need to.

### prNet-package
This is the interesting part. This package contains all necessary methods to actual search for and save patterns. Most of those processes are managed in the PatterUsage.java class. Most of the methods have a javadoc attached but here are the most important of them:

- *public static <T> List<Pattern<T>> findPatterns(Comparison<T, T> comparison, List<List<T>> analyzableElements)* <br>
This method returns all patterns in the given two-dimensional list 'analyzeableElements'. Two different elements will be compared with the lambda-expression given in the 'comparison' parameter. This is the basic method to find patterns. The two-dimensional list can also be replaced by an object that implements the ArrangableToList- or the Spliterator-interface.

- *public static <T> Map<String, Double> findMatchingAuthors(List<Pattern<T>> text, Aspect<T> aspect, Connection conn, boolean ignoreLow, int mode, int min)*<br>
This method finds all authors of patterns in the database which match with the given list of patterns.
This can be done in 4 different ways which are represented with the "mode"-variable through the integers 0 to 3. The default is 2 and should always be used if you aren't sure you need another one.<br><br>
The modes:
 
    - 0: counts the total amount of matching patterns in the database.
    - 1: counts the total amount of matching patterns in the database but only uses the highest of them later.
    - 2: counts the amount of patterns and then calculates the percentage of the matching pattern to all saved patterns by the
    - author.
    - 3: counts the amount of patterns and then calculates the percentage of the matching pattern to all saved patterns by the
    author but only uses the highest of them later.
    Note: the "author" is the creator the given patterns and often defined before. If you don't know what to use here,
    you should look up how vou've analyzed what ever you did to get the patterns that are used here in the text-
    parameter. <br><br>
Parameters:
 
  - text: The list of patterns that will be compared with all authors in the database
  - aspect: The aspect under which the patterns were analyzed
  - conn The SQL-Connection to the database
  - ignoreLow: Decides if every pattern that was found only once should be ignored or not. This should be true if you test a structure that is already saved in the database because it's own patterns won't count in the result and therefore will be ignored afterwards. It's practically deletes all self-references.
  - mode:The above explained mode
 <br>
 
 
- *public static boolean match(List<T> environment, P pattern)*<br>
 This method checks if the pattern matches with the list 'environment'. Pretty much like simple regex.

 
  
