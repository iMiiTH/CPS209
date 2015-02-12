/* lab 08 */
/* exercise for loops and pointers */
/* assume that plain text which needs to be encrypted has
 * only capital letters */

#include <stdio.h>
#include <stdlib.h>


int  Caesar_encrypt(char *p, char *s, int enckey);
int  Caesar_decrypt(char *p, char *s, int enckey);

int main(void){
   char A[]="THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG";
   char B[50], /* stores encrypted output string  */
        C[50];  /* stores decrypted output using string B as source */
   int enckey,
       statuse, statusd; /* they store return values from the functions */

   printf("Plaintext for encryption is : %s \n", A);

   printf("Input numerical key for Caesar's cypher : ");
   scanf("%i", &enckey );  // COMPLETE THIS

   putchar('\n');
   printf("You entered encryption key %d \n", enckey);

   /* encrypt by Caesar's cypher */
   statuse= Caesar_encrypt( A, B, enckey);  // COMPLETE THE INPUT PARAMETERS

   printf("Ciphertext is : %s \n", B);

   /* decrypt by Caesar's cypher */
   statusd = Caesar_decrypt( B, C, enckey );   // COMPLETE THE INPUT PARAMETERS
   printf("Decrypted text is: %s \n", C); 


   exit (0);
}

int  Caesar_encrypt( char *p, char *s, int enckey){         // COMPLETE THE INPUT PARAMETERS
   //COMPLETE THIS
   while( *p ){
      if( *p == ' '){
         *s = ' ';
      }
      else{
         *s = (*p - 'A' + enckey) % 26 + 'A' ;
      }
      p++;
      s++;
   }
   *s = 0;
   return 0;
}

int Caesar_decrypt(char *p, char *s, int enckey){     // COMPLETE THE INPUT PARAMETERS

   //COMPLETE THIS

   while( *p ){
      if( *p == ' ' ){
         *s = ' ';
      }
      else{
         *s = (*p - 'A' - enckey + 26) % 26 + 'A';
      }
      p++ ;
      s++ ;
   }
   *s = 0;
   return 0;
}












