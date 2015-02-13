
typedef struct nodeT{
  int coef;
  int powr;
  struct nodeT *next;
} node;

typedef struct {
   int num_terms;
   node *terms;
} poly;

/* Prints the poly, in the form c^e + c^e... etc. */
void poly_print(poly *P) ;
/* Goes through the poly and frees each individual node. */
void poly_free(poly **P) ;
/* Copies the data of the poly into a new poly. */
poly *poly_duplicate(poly *P ) ;
/* Takes two polys and adds them together. */
poly *poly_add(poly *P1, poly *P2) ;
/* Allocates the memory for a new poly and adds all of the terms into nodes. */
poly *poly_create(int num,...) ;
/* Multiplies the poly and all its terms by a scalar. */
poly *poly_scalar_mult(poly *P, int x) ;
