A2Viewer.class : A2Viewer.java A2Frame.class
	javac A2Viewer.java

A2Frame.class : A2Frame.java A2Component.class
	javac A2Frame.java

A2Component.class : A2Component.java Car.class Truck.class Vehicle.class
	javac A2Component.java 

Car.class : Car.java
	javac Car.java

Truck.class : Truck.java
	javac Truck.java

Vehicle.class : Vehicle.java
	javac Vehicle.java

# simply type in:
# $ make run
# to compile and run the program
# it removes all the class files afterwards.
run : A2Viewer.class
	java A2Viewer
	rm *.class 
