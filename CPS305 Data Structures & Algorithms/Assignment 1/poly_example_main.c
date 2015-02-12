#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>

#include "poly_ADT.h"

int main (void) {

   poly *P0, *P1, *P2, *P3, *M1, *M2, *P4, *P5, *P6, *P7;

   P0=poly_create(4,2,1,3,2,6,5,1,7); //4 terms: 1x^7 + 6x^5 + 3x^2 + 2x^1
   P1=poly_create(3,3,2,1,4,6,5);     //3 terms: 6x^5 + 1x^4 + 3x^2
   P2=poly_create(1,16,2);            //1 term:  16x^2
   P3=poly_create(0);                 // no terms

   printf("P0: "); poly_print(P0);
   printf("P1: "); poly_print(P1);
   printf("P2: "); poly_print(P2);
   printf("P3: "); poly_print(P3);

   M1=poly_scalar_mult(P1,2);
   printf("M1 (P1*2): "); poly_print(M1);

   M2=poly_scalar_mult(P3,4);
   printf("M2 (P3*4): "); poly_print(M2);

   P4=poly_duplicate(M1);
   printf("P4 (dup M1): "); poly_print(P4);

   P5=poly_add(P1,P0);
   printf("P5 (P1+P0): "); poly_print(P5);

   P6=poly_add(P3,P3);
   printf("P6 (P3+P3): "); poly_print(P6); 

   P7=poly_add(P0,P3);
   printf("P7 (P0+P3): "); poly_print(P7);

   poly_free(&P1);
   if (P1==NULL) printf("P1: was freed\n");
   else          printf("P1: is not null!!\n");
   
   
   
   poly *test1=poly_create(4,2,1,3,2,6,5,1,7); //4 terms: 1x^7 + 6x^5 + 3x^2 + 2x^1

   poly *test2=poly_duplicate(test1);
   test2=poly_scalar_mult(test2, 5);

   printf("test1: ");poly_print(test1);
   printf("test2: ");poly_print(test2);

   //printf("%p, %p\n", test1, test2);
   poly_free(&test1);

   poly *MM1 = poly_create(5,1,1,2,2,3,3,4,4,5,5);
   poly *MM2 = poly_create(5,-1,1,-2,2,-3,3,-4,4,-5,5);

   printf("MM1: ");poly_print(MM1);
   printf("MM2: ");poly_print(MM2);


   poly *MM1_Plus_MM2 = poly_add(MM1, MM2);
   printf("(MM1+MM2): ");poly_print(MM1_Plus_MM2);

   poly *MM1_Multiplied_By_5 = poly_scalar_mult(MM1, 5);
   poly *MM2_Multiplied_By_10 = poly_scalar_mult(MM2, 10);

   printf("MM1*5: ");poly_print(MM1_Multiplied_By_5);
   printf("MM2*10: ");poly_print(MM2_Multiplied_By_10);
   printf("MM1: ");poly_print(MM1);
   printf("MM2: ");poly_print(MM2);

}

/*
   When this program was run with a completed poly_ADT.c 
   (with bonus poly_add) it produced the following output:
P0: 1x^7 + 6x^5 + 3x^2 + 2x^1 
P1: 6x^5 + 1x^4 + 3x^2 
P2: 16x^2 
P3: 
M1 (P1*2): 12x^5 + 2x^4 + 6x^2 
M2 (P3*4): 
P4 (dup M1): 12x^5 + 2x^4 + 6x^2 
P5 (P1+P0): 1x^7 + 12x^5 + 1x^4 + 6x^2 + 2x^1 
P6 (P3+P3): 
P7 (P0+P3): 1x^7 + 6x^5 + 3x^2 + 2x^1 
P1: was freed
*/
