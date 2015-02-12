poly_example : poly_example_main.o poly_ADT.o
	gcc -o poly_example poly_example_main.o poly_ADT.o

poly_example_main.o : poly_example_main.c
	gcc -c poly_example_main.c

poly_ADT.o : poly_ADT.c
	gcc -c poly_ADT.c

run : poly_example
	./poly_example
