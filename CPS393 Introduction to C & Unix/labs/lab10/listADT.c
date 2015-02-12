/* Source listADT.c
 a user compiles lsitADT.o with their main to use a list ADT
*/

#include <stdio.h>
#include <stdlib.h>
#include "listADT.h"

void init(List *L) {
  L->length=0;
  L->head=NULL;
}

int lenght(List L) {
  return L.length;
}

void add(List *L, char c) {
  NodeType *new;
  new = (NodeType *) malloc(sizeof(NodeType));
  new->ch=c;
  new->next= L->head;
  L->head=new;
  L->length++;
}

void print(List L) {
 int i;
NodeType *p;

printf("List is: ");
for (i=0, p=L.head; i<L.length; i++, p=p->next) {
  printf(" %c ", p->ch);
}
putchar('\n');
}

