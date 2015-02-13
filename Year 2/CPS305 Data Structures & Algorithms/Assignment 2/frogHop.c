#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>

//The main pond which contains the lillypads and some simplyfying variables
typedef struct PondStruct {
   int **grid;
   int length;
   int maxIndex;
} Pond;

//The PadSet stores the linked list that stores all the possible positions.
typedef struct PadSetStruct {
   struct PadNodeStruct *first;
   struct PadNodeStruct *last;
} PadSet;

//The PadNodes that are stored as a linked list in PadSets
typedef struct PadNodeStruct{
   int x;
   int y;
   struct PadNodeStruct *next;
} PadNode;

/* problem solving methods */
void find_path(Pond *P, int x, int y, int i, int p);

/* init methods */
Pond* initializePond(int size);
PadSet* initializePadSet();
PadNode* initializePadNode();

/* print methods */
void print_pond(Pond *P);
void print_set(PadSet *pSet);

/* PadSet Modifiers */
PadSet *padSetPopulatedWithNextPads(Pond *P, int x, int y);
void addNode(PadSet *P, PadNode *N);
void addNewNode(PadSet *P, int x, int y);
void freePadSet(PadSet *P) ;

int main( int argc, char *argv[] )
{
   //argument checking.
   if(argv[1]==NULL || argv[2]==NULL || argv[3]==NULL || argc != 4) {
      printf("Invalid number of arguments... exiting.\n");
      printf("Requires 5.\n");
      return 1;
   }
   int size = atoi(argv[1]);
   int xPos = atoi(argv[2]);
   int yPos = atoi(argv[3]);

   //more argument checking.
   if( xPos >= size || xPos <0 || yPos >= size || yPos <0) {
      printf("Invalid starting position in arguments... exiting.\n");
      return 1;
   }

   Pond *pond;
   pond=initializePond(size);
   find_path(pond, xPos, yPos, 1, 0);
   printf("No path found.");
   return 1;
}

/* problem solving methods */

/**
 * Function: find_path
 * Given a pond and starting position,
 * recursively finds the first possible path.
 * *P: The given pond used for the path finding and tracking
 * x: the x position of the frog.
 * y: the y position of the frog.
 * c: the current step in the pathfinding.
 * p: the previous step in the pathfinding.
 */
void find_path(Pond *P, int x, int y, int c, int p)
{
   P->grid[x][y] = c;
   if(c == P->length*P->length) {
      //print_pond(P);
      printf("Path found.\n");
      print_pond(P);
      exit(0);
   } else {
      PadSet *nextValidPadSet = padSetPopulatedWithNextPads(P, x, y);
      PadNode *tempPadNode = nextValidPadSet->first;
      while(tempPadNode!=NULL) {
         find_path(P, tempPadNode->x, tempPadNode->y, c+1, c);
         tempPadNode=tempPadNode->next;
      }
      freePadSet(nextValidPadSet);
      P->grid[x][y]=0;
   }
}

/* init methods */
/**
 * Function: initializePond
 * size: the size of the grid.
 * returns: Pond* to an initialized memory block 
 */
Pond* initializePond(int size)
{
   int i,j;
   Pond *P=malloc(sizeof(Pond));
   P->length=size;
   P->maxIndex=size-1;
   P->grid=calloc(size, sizeof *P->grid);
   for(i=0; i<P->length; i++) {
      P->grid[i]=calloc(size, sizeof(int));
   }
   return P;
}

/**
 * Function: initializePadSet
 * returns PadSet* to initialized memory.
 */
PadSet* initializePadSet()
{
   PadSet *returnPadSet;
   returnPadSet = malloc(sizeof(PadSet));
   returnPadSet->first=NULL;
   return returnPadSet;
}

/**
 * Function: initializePadNode
 * returns: A PadNode pointer to allocated memory.
 */
PadNode* initializePadNode()
{
   PadNode *returnPadNode;
   returnPadNode = malloc(sizeof(PadNode));
   returnPadNode->x=-1;
   returnPadNode->y=-1;
   returnPadNode->next=NULL;
   return returnPadNode;
}

/* printing methods */

/**
 * Function: print_pond
 * Given a Pond, prints it in a formatted fashion.
 */
void print_pond(Pond *P)
{
   int i,j;
   for(i=0; i<P->length; i++) {
      for(j=0; j<P->length; j++) {
         printf("%2d ", P->grid[i][j]);
      }
      putchar('\n');
   }
}

/**
 * Function: print_set
 * Given a PadSet, prints all the nodes in a formatted fashion. (used for testing.)
 */
void print_set(PadSet *pSet)
{
   PadNode *P = pSet->first;
   printf("(%d, %d) ", P->x, P->y);
   while(P->next != NULL) {
      printf("-> (%d, %d) ", P->x, P->y);
      P=P->next;
   }
   putchar('\n');
}

/**
 * Function: padSetPopulatedWithNextPads
 * Given a PasSet and coordinates x and y,
 * it is populated with the next positions.
 *
 * These positions are calculated clockwise starting from the "one o'clock" position onwards.
 *
 * *P: The current Pond being used.
 * x: The current x position.
 * y: The current y position.
 * returns: A PadSet pointer populated with PadNodes.
 */
PadSet *padSetPopulatedWithNextPads(Pond *P, int x, int y)
{
   int tempX, tempY;
   PadNode *temp; 
   PadSet *returnPadSet;
   returnPadSet = initializePadSet();

   //1 o'clock
   if(x>=2 && y <= P->maxIndex-1) {
      tempX=x-2; tempY= y+1;
      if(P->grid[tempX][tempY]!=0) {
      } else {
         addNewNode(returnPadSet, tempX, tempY);
      }
   }
   //2 o'clock
   if(x>=1 && y<=P->maxIndex-2) {
      tempX=x-1; tempY=y+2;
      if(P->grid[tempX][tempY]!=0) {
      } else {
         addNewNode(returnPadSet, tempX, tempY);
      }
   }
   //4 o'clock
   if(x<=P->maxIndex-1 && y<=P->maxIndex-2) {
      tempX=x+1; tempY=y+2;
      if(P->grid[tempX][tempY]!=0) {
      } else {
         addNewNode(returnPadSet, tempX, tempY);
      }
   }
   //5 o'clock
   if(x<=P->maxIndex-2 && y<=P->maxIndex-1) {
      tempX=x+2;  tempY=y+1;
      if(P->grid[tempX][tempY]!=0) {
      } else {
         addNewNode(returnPadSet, tempX, tempY);
      }
   }
   //7 o'clock
   if(x<=P->maxIndex-2 && y>=1) {
      tempX=x+2;  tempY=y-1; 
      if(P->grid[tempX][tempY]!=0) {
      } else {
         addNewNode(returnPadSet, tempX, tempY);
      }
   }
   //8 o'clock
   if(x<=P->maxIndex-1 && y>=2) {
      tempX=x+1;  tempY=y-2;
      if(P->grid[tempX][tempY]!=0) {
      } else {
         addNewNode(returnPadSet, tempX, tempY);
      }
   }
   //10 o'clock
   if(x>=1 && y>=2) {
      tempX=x-1;  tempY=y-2;
      if(P->grid[tempX][tempY]!=0) {
      } else {
         addNewNode(returnPadSet, tempX, tempY); 
      }
   }
   //11 o'clock
   if(x>=2 && y >= 1) {
      tempX=x-2;  tempY=y-1;
      if(P->grid[tempX][tempY]!=0) {
      } else {
         addNewNode(returnPadSet, tempX, tempY);
      }
   }

   return returnPadSet;
}

/**
 * Function: addNode
 * Given a PadSet and a PadNode, adds the node to the linked list.
 * *P: The given PadSet to populate.
 * *N: The PadNode to be added.
 */
void addNode(PadSet *P, PadNode *N)
{

   //making sure that to account for the empty and one cases.
   if(P->first==NULL) {
      P->first=N;
      P->last=N;
   } else if (P->first==P->last) {
      P->first->next=N;
      P->last=N;
   } else {
      P->last->next=N;
      P->last=N;
   }
}

/**
 * Function: addNewNode
 * Given a PadSet and coordinates, adds a new node with those coordinates.
 * *P: The PadSet to be added to.
 * x: The x coordinate of the PadNode to be added
 * y: The y coordinate of the PadNode to be added.
 */
void addNewNode(PadSet *P, int x, int y)
{
   PadNode *nodeToAdd = initializePadNode();
   nodeToAdd->x=x; 
   nodeToAdd->y=y;
   addNode(P, nodeToAdd);
}

/**
 * Function: freePadSet
 * Frees the memory used by a PadSet, including all of its PadNodes.
 * *P The PadSet to be freed.
 */
void freePadSet(PadSet *P) 
{
   PadNode *pNode;
   PadNode *nodeToFree;
   pNode=P->first;
   while(pNode!=NULL) {
      nodeToFree=pNode;
      pNode=pNode->next;
      free(nodeToFree);
   }
   free(P);
}
