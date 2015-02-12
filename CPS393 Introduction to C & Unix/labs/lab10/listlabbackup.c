/* 
 * Source listADT.c
 * a user compiles listADT.o with their main to use a list ADT
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "listlab.h"

void printNode(NodeType *n);

void init(List *L) {
   L->length=0;
   L->head=NULL;
}

int length(List L) {
   return L.length;
}

void add(List *L, char *c, int age) {
   NodeType *new;
   new = (NodeType *) malloc(sizeof(NodeType));
   new->age = age;
   strcpy(new->firstname, c);

   NodeType *temp = L->head;

   if(L->head==NULL){
      L->head = new;
      L->length++;
      return;
   }
   if(L->head->age > age){
      new->next=L->head;
      L->head=new;
      L->length++;
      return;
   }
   else{
      while(temp->next!=NULL){
         if(temp->next->age >= age){
            new->next = temp->next;
            temp->next=new;
            L->length++;
            return;
         }
      }
      temp->next = new; 
      L->length++;
      return;
   }
}

void print(List L) {
   int i;
   NodeType *p;
   if(L.head==NULL){
      printf("List is: NULL\n");
      return;
   }

   printf("List is: ");
   for (i=0, p=L.head; i<L.length; i++, p=p->next) {
      printf(" [%s,%d]->", p->firstname,p->age);
   }
   printf(" null\n");
}

void printNode(NodeType *n){
   if(n==NULL){
      printf("Node is: NULL\n");
      return;
   }
   printf("Node is: %s, %d\n", n->firstname, n->age);
}

