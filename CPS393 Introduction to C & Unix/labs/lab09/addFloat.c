#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>

int isFloat(char* argument);
int main(int args, char *argv[])
{
   int i, j;
   float arg1;
   FILE *f1;
   FILE *f2;

   if(args>4){
      fprintf(stderr, "Error: Too many arguements\n");
      exit(1);
   }
   if(args<4){
      fprintf(stderr, "Error: Too few arguments\n");
      exit(1);
   }
   if(!isFloat(argv[1])){
      fprintf(stderr, "Error: Argument 1 is not a float.\n");
      exit(1);
   }
   else
      arg1 = atof(argv[1]);
      
   if((f1 = fopen(argv[2],"r"))==NULL){
      fprintf(stderr, "Error: File %s cannot be opened\n", argv[2]);
      exit(1);
   }

   f2 = fopen(argv[3], "w");
   if(f2 == NULL){
      fprintf(stderr, "Erro: File %s cannot be opened.\n", argv[3]);
      exit(1);
   }
   double tmp;
   while(fscanf(f1, "%lf", &tmp) != EOF ){
      //printf("%f  %f\n", tmp, (tmp+arg1));
      fprintf(f2, "%f\n", (tmp+arg1));
   }
   fclose(f1);
   fclose(f2);
}

int isFloat(char* argument)
{
   char s;
   float num;

   if((num = strtof(argument, NULL)) == 0){
      return 0;
   }
   if(!(isdigit(*argument) || *argument == '-'))
      return 0;
   argument++;
   while(*argument != '\0'){
      if (!(isdigit(*argument) || *argument == '.' )){
            return 0;
      }
      argument++;
   }
   return 1;
}
