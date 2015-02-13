/* Source userlistlab.c
compile : gcc userlistlab.c listlab.o
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "listlab.h"

int main(void) {
   List L;
   init(&L);

   add(&L, "Zoe", 20);
   print(L);
   add(&L, "Phoebe", 21);
   print(L);
   add(&L, "Alex", 19);
   print(L);
   add(&L, "David", 20);
   print(L);
   printf("Length is %d\n", length(L));
   return 0;
}


