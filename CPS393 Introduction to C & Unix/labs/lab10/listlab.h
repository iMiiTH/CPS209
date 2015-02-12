/* 
 * Source listlab.h
 */

typedef struct node NodeType;

struct node {
   char firstname[10];
   int age;
   NodeType *next;
};

typedef struct {
   int length;
   NodeType *head;
} List;

void init(List *L);
void add(List *L, char c[10], int age);
void print(List L);
int length(List L);

