userlistlab : userlistlab.o listlab.o
	gcc -o userlistlab userlistlab.o listlab.o

userlistlab.o : userlistlab.c 
	gcc -c userlistlab.c

listlab.o : listlab.c
	gcc -c listlab.c -o listlab.o

run : userlistlab 
	./userlistlab
