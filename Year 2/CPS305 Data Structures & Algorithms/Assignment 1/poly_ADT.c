#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <string.h>

#include "poly_ADT.h"

node * makeNode(int coefficient, int power);
void node_print(node *no);
poly* emptyPoly();

/* Function: poly_print
 * Prints a given poly and all it's nodes in a readable format.
 *
 * *P: The given poly to print.
 */
void poly_print(poly *P)
{ 
   if(P==NULL) {                    
   } else if (P->terms==NULL) { // If the poly is empty, return.
   } else {
      node *tempNode = P->terms;
      node_print(tempNode);
      while( (tempNode=tempNode->next) != NULL ){ // Cycle through the poly, printing each node.
         printf(" + ");
         node_print(tempNode);
      }
   }
   putchar('\n');
}
/* Function: poly_free 
 * Given the pointer to a poly, it deallocates all the memory given to it and its nodes.
 *
 * **P: The poly who will be freed, as well as its nodes.
 */
void poly_free(poly **P)
{
   node *tempNode1;
   node *tempNode2;
   if(P==NULL) { // If the poly is null, there's nothing to do!
      return;
   } else if((*P)->num_terms==0 || (*P)->terms==NULL) {
      // If the poly has no terms, then just free the poly and that's it.
      *P=NULL;
      free(*P);
      return;
   } else {
      // The poly has terms to free, so cycle through them all and free each individual node. 
      tempNode1 = (*P)->terms;
      while( (tempNode2=tempNode1->next) != NULL ) {
         free(tempNode1);
         tempNode1=tempNode2;
         tempNode2=NULL;
      } // Making sure everything has been cleared. 
      free(tempNode1);
      free(tempNode2);
      free(*P);
      *P=NULL;
   }
}

/* Function: poly_duplicate
 * Given a pointer to a poly, creates a duplicate of it and its nodes.
 *
 * *P: The poly to copy.  *
 * returns: A pointer to the new duplicate poly.
 */
poly *poly_duplicate(poly *P )
{
   int i;
   if(P==NULL) return NULL;
 
   // Create a node array the same size of the given poly.
   node **nodeArray = (node **)malloc(sizeof(node)*P->num_terms);

   node *tempNode = P->terms;
   // The following loop cycles through the array.
   // It copies each node and linking the new nodes.
   for(i=0; i<P->num_terms; i++) {
      nodeArray[i]=makeNode(tempNode->coef, tempNode->powr);
      memcpy(nodeArray[i], tempNode, sizeof(node));
      tempNode = tempNode->next;
      if(i>0){ //Link the previous node to the new node.
         nodeArray[i-1]->next=nodeArray[i];
      }
   }
   poly *duplicatePoly = malloc(sizeof(poly));
   duplicatePoly->num_terms = P->num_terms;
   duplicatePoly->terms = nodeArray[0];
   return duplicatePoly;
}

/* Function: poly_add
 * Given two polys, a new poly is given back which is the two combined.
 * P1: The first poly
 * P2: The second poly
 *
 * returns: a new poly which is the sum of the two givens.
 */
poly *poly_add(poly *P1, poly *P2)
{
   node *node1;      // Used for cycling through the nodes of the polys 
   node *node2;
   node *tempNode;
   int highestDegree; // Stores the highest degree to make an array of that size. 
   int i;             // Counter variable

   poly *returnPoly = emptyPoly();

   // The following check if any of the given polys are empty or null.
   if (P1->terms==NULL || P1->num_terms==0) {
      if(P2->terms==NULL || P2->num_terms==0) {
         return returnPoly;
      } else {
         returnPoly = poly_duplicate(P2);
         return returnPoly;
      }
   } else if (P2->terms==NULL || P2->num_terms==0) {
      returnPoly = poly_duplicate(P1);
      return returnPoly;
   }

   // The following loops cycle through the polys, searching for the highest power.
   tempNode=P1->terms;
   highestDegree=tempNode->powr;
   while( (tempNode=tempNode->next)!=NULL ) {
      if(highestDegree<tempNode->powr){
         highestDegree=tempNode->powr;
      }
   }
   tempNode=P2->terms;
   if(highestDegree<tempNode->powr) {
      highestDegree=tempNode->powr;
   }
   while( (tempNode=tempNode->next)!=NULL ) {
      if(highestDegree < tempNode->powr) {
         highestDegree = tempNode->powr;
      }
   }

   // With the highest degree found, an array of that size is created.
   node **nodeArray = (node **)malloc(sizeof(node)*highestDegree);
   for( i=highestDegree; i>0; i--){
      node1=NULL;
      node2=NULL;

      // The following loops cycle through the polys looking for the current powr. 
      // A node is then made using the found coef. 
      tempNode=P1->terms;
      if(tempNode->powr==i){
         node1 = makeNode(tempNode->coef, tempNode->powr);
      } else {
         while( (tempNode=tempNode->next)!=NULL ){
            if(tempNode->powr==i){
               node1 = makeNode(tempNode->coef, tempNode->powr);
               break;
            }
         }
      }

      // The second loop then looks to see if there is also a term with the same powr.
      tempNode=P2->terms;
      if(tempNode->powr==i){
         node2 = makeNode(tempNode->coef, tempNode->powr);
      } else {
         while( (tempNode=tempNode->next)!=NULL ){
            if(tempNode->powr==i){
               node2 = makeNode(tempNode->coef, tempNode->powr);
               break;
            }
         }
      }

      // The folliowing check if the node is proper (scaler not 0, or not null)
      if(node1==NULL && node2==NULL){
         nodeArray[i]=NULL;
      } else if (node1==NULL && node2!=NULL) {
         nodeArray[i]=node2;
      } else if (node1!=NULL && node2==NULL) {
         nodeArray[i]=node1;
      } else {
         nodeArray[i]=makeNode(node1->coef+node2->coef, i);
      }
   }

   // The following links all the nodes in the array together.
   // The linking ignores all nodes that are NULL or equal to 0. 
   tempNode=NULL;
   for(i = highestDegree; i>0; i--) {
      if(nodeArray[i]==NULL){ 
         // Don't link anything, the node is null, no need to link it.
         // Should free it though.
         free(nodeArray[i]);
      } else if (nodeArray[i]->coef==0) {
         // Again, don't do anything, the node is 0. 
         // Should free it though.
         free(nodeArray[i]);
      } else { 
         // A nontrivial node has been found, setting it as the previous' next node.
         returnPoly->num_terms++;
         if(tempNode==NULL){
            tempNode=nodeArray[i];
            returnPoly->terms=tempNode;
         } else { // Link the node together.
            tempNode->next=nodeArray[i];
            tempNode=tempNode->next;
         }
      }
   }

   return returnPoly;
}

/* Function: poly_create
 * Creates a new poly pointer populated with nodes.
 *
 * num: the number of terms that will be held in the poly
 *
 * returns: a new poly populated with the other given arguements.
 */
poly *poly_create(int num, ...)
{
   int size = num; // store the size of the polynomial
   poly *returnPoly = emptyPoly();
   va_list arguments;                     
   // Store all of the arguments after the num variable.
   va_start ( arguments, num );           

   if(num>0){
      node *firstNode = makeNode(va_arg(arguments, int), va_arg(arguments, int));
      // Loop through all of the arguments, creating a linked list of nodes.
      for ( int x = 1; x < num; x++ )        
      {
         int coefficient = va_arg(arguments, int);
         int power = va_arg(arguments, int);

         node *tempNode = makeNode(coefficient, power);
         tempNode -> next = firstNode;
         firstNode = tempNode;
      }

      returnPoly -> num_terms = num;
      returnPoly -> terms = firstNode;
   }

   va_end ( arguments ); // Cleans up the list
   return returnPoly;
}

/* Function: poly_scalar_mult
 * Multiplies a given poly pointer by a scalar.
 *
 * *P: The poly to multiply
 * x: The scalar that the given poly will be multiplied by.
 *
 * returns: A pointer to a new poly that has been multiplied.
 */
poly *poly_scalar_mult(poly *P, int x)
{
   if(P==NULL) { //Checks for NULL or polys with no terms.
      return NULL;
   }
   if(P->terms == NULL || P->num_terms == 0) {
      poly *returnPoly = (poly *)malloc(sizeof(poly));
      memcpy(returnPoly, P, sizeof(poly));
      return returnPoly;
   }
   // Create new poly to store all the newly multiplied terms.
   poly *scalar_multiplied_poly = poly_duplicate(P);

   node *tempNode = scalar_multiplied_poly->terms;
   tempNode->coef = tempNode->coef*x;

   while( (tempNode=tempNode->next) != NULL ) { 
      // Loop through the new poly, multiplying
      tempNode->coef = tempNode->coef*x;
   }

   return scalar_multiplied_poly;
}

/* Function: makeNode
 * Creates a node with a given coefficient and power.
 *
 * coefficient: The coefficient of the node.
 * power: The power of the node.
 *
 * returns: A pointer to the new node.
 */
node * makeNode(int coefficient, int power)
{
   node *returnNode = (node *)malloc(sizeof(node));
   returnNode -> coef = coefficient;
   returnNode -> powr = power;
   returnNode -> next = NULL;
   return returnNode;
}

/*
 * Function: node_print
 * Given a node, prints it.
 *
 * *no: The given node to print.
 */
void node_print(node *no)
{
   if(no==NULL){
      printf("NULL ");
      return;
   }
   printf("%dx^%d ", no -> coef, no -> powr );
}

/*
 * Function: emptyPoly
 * Creates a poly with no term.
 *
 * returns: A poly pointer with NULL terms and 0 num_terms
 */
poly* emptyPoly()
{
   poly *returnPoly = malloc(sizeof(poly));
   returnPoly->terms=NULL;
   returnPoly->num_terms=0;
   return returnPoly;
}
