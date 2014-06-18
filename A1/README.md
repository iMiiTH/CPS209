Assignment 1
=====

The purpose of this assignment was to practice and understand the concepts of inheritance and interfaces. 

The interface MoveableShape.java was used to provide methods for each object that implemented it, allowing for very different objects to interact with eachother.

####GameViewer.java
This is the main class of the assignment and creates the main JFrame window and JPanel for the game.

####GameFrame.java
This is the main window of the assignment. It is a subclass of JFrame, and has very minor alterations. The main purpose of the GameFrame is to hold the GamePanel.

####GamePanel.java
This class holds the bulk of the game logic. This class is a subclass of JPanel. The GamePanel holds all of the "creatures" as well as the mouselisteners used to control the main character.

####MoveableShape.java
This interface contains useful methods that would be useful when objects interact with eachother. This interface was implemented by all the following classes.

####Creature.java
This is the superclass for CreatureFast and CreatureSlow. This class contains all the methods that every creature shares, and its basic shape was used often during testing.

####CreatureSlow.java
A subclass of Creature, this is a really slow creature that moves slowly and in a pattern. 

####CreatureFast.java
A subclass of Creature, we had to create a smaller, faster more versatile creature with primitive evading abilities. 

####Predator.java
The main character that is controlled by the user using the mouse buttons.

##Running the game.
To run the game, you can:
* Type make run, to run the makefile 
* javac GameViewer.java, java GameViewer
* Add the classes to any IDE and run GameViewer from there.

###Controls
Use the right and left buttons to control the direction of the predator.
The game ends when you've eaten all of the other creatures.
