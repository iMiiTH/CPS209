GameViewer.class : GameViewer.java GameFrame.class
	javac GameViewer.java

GameFrame.class : GameFrame.java GamePanel.class
	javac GameFrame.java

GamePanel.class : TextTimer.class Creature.class CreatureFast.class CreatureSlow.class Predator.class MoveableShape.class
	javac GamePanel.java

TextTimer.class : TextTimer.java
	javac TextTimer.java

Creature.class : Creature.java
	javac Creature.java

CreatureFast.class : CreatureFast.java
	javac CreatureFast.java

CreatureSlow.class : CreatureSlow.java
	javac CreatureSlow.java

Preadator.class : Predator.java
	javac Predator.java

MoveableShape.class : MoveableShape.java
	javac MoveableShape.java

.class : TextTimer.class Creature.class CreatureFast.class CreatureSlow.class Predator.class MoveableShape.class
	javac GamePanel.java

TextTimer.class : TextTimer.java
	javac TextTimer.java

Creature.class : Creature.java
	javac Creature.java

CreatureFast.class : CreatureFast.java
	javac CreatureFast.java

CreatureSlow.class : CreatureSlow.java
	javac CreatureSlow.java

Preadator.class : Predator.java
	javac Predator.java

MoveableShape.class : MoveableShape.java
	javac MoveableShape.java

# useful little function, just type in
# $ make run 
# To build and run the program
# removes all the class files afterwards. 
run : GameViewer.class
	java GameViewer
	rm *.class
