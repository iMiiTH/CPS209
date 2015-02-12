/*
 * Assignment 1
 * CPS393
 * 2014.04.07
 * @author Mitchell Mohorovich
 */

#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>

/* The following 7 lines define Colour Strings */
#define ANSI_COLOR_RED     "\x1b[31m"
#define ANSI_COLOR_GREEN   "\x1b[32m"
#define ANSI_COLOR_YELLOW  "\x1b[33m"
#define ANSI_COLOR_BLUE    "\x1b[34m"
#define ANSI_COLOR_MAGENTA "\x1b[35m"
#define ANSI_COLOR_CYAN    "\x1b[36m"
#define ANSI_COLOR_RESET   "\x1b[0m"

static const int HEIGHT=12; /* The height of the grid */
static const int WIDTH=12; /* The width of the grid */
static const double TOLERANCE=0.01; /* The stability tolerance of the grid */
typedef int bool; /* This typedefs a bool type */
#define true 1 /* The true value of bool */
#define false 0 /* The false value of bool */

/*
 * This method fills in the Array of two dimentional array pointers.
 * @param **array the array to be filled
 */
int create2DDoubleArray(double **array);
/*
 * This method prints and formats a grid array.
 * @param **array The array to be printed
 */
int printGrid(double **array);
/*
 * This method calculates the next densities of the weed.
 * @return 0 if stable, 1 otherwise
 */
int NextDensities(double **fromGrid, double **toGrid);
/*
 * This method reads a grid into a File
 * @param *f the pointer to the file holding the initial densities
 * @param **toGrid the pointer to the grid that will hold the initial densities
 * @return 0 if successful, 1 otherwise
 */
int fileToGrid(FILE *f, double **grid);
/*
 * This method takes two 2D array pointers and copies from b to a.
 * @param **b The 2D array copying from.
 * @param **a The 2D array being copied to.
 * @return 0 if successful, 1 otherwise.
 */
int gridCopy(double **b, double **a);
/*
 * This method checks if the grids have reached stability
 * @param **a The first grid.
 * @param **b The second grid to be compared.
 * @return 0 if successful, 1 otherwise.
 */
int isStable(double **a, double **b);
/*
 * This method reads over every character in the file to check for any unallowed characters. These characters are anything that is not a number, space or new line character.
 * @param *f The file pointer to the file being checked.
 * @return 0 if successful, 1 otherwise;
 */
int checkFile(FILE *f);
/*
 * This method counts the number of doubles in the file in order to make sure the input matches the side of the grid.
 * @param *f The file pointer to the file being check.
 * @return 0 if successful, 1 otherwise.
 */
int numCount(FILE *f);

int main(int args, char *argv[])
{
   bool stabilityFound;
   int i, j, years, num;
   double temp;
   double **gridA, **gridB, **stabilityGrid;
   FILE *year0;
   /* The following two if statements check if there are 2 arguments provided */
   if(args < 3){
      fprintf(stderr, "%s: Error: Not enough arguments\nUsage: %s [number of years] [input file]\n", argv[0], argv[0]);
      exit(1);
   }
   if(args > 3){
      fprintf(stderr, "%s: Error: Too many arguments.\nUsage: %s [number of years to calculate] [input file]\n", argv[0], argv[0]);
      exit(1);
   }
   /* Checks if the year is formatted properly */
   if( (years=atoi(argv[1]))==0){
      fprintf(stderr, "%s: Error: years must be a positive integer.\n", argv[0]);
      exit(1);
   }
   if(years < 1){
      fprintf(stderr, "%s: Error: years must be a positive integer.\n", argv[0]);
      exit(1);
   }
   /* Checks if the file can be read */
   if( (year0 = fopen(argv[2], "r")) == NULL){
      fprintf(stderr, "fopen: Error opening file \"%s\".\n", argv[2]);
      exit(1);
   }
   /* checks if the file contains nonnumber characters */
   if( checkFile(year0) == 1){
      fprintf(stderr, "%s: Error: Your file contains illegal characters\n", argv[0]);
      exit(1);
   }
   /* checks if the file has enough or too many numbers */
   if( (num=numCount(year0)) != HEIGHT*WIDTH){
      if(num > HEIGHT*WIDTH)
         fprintf(stderr, "%s: Error: Your file contains too many numbers\n", argv[0]);
      else
         fprintf(stderr, "%s: Error: Your file does not contain enough numbers\n", argv[0]);
      exit(1);
   }

   /* The following creates the two grids */
   gridA = (double **)malloc(HEIGHT*sizeof(double *));
   create2DDoubleArray(gridA);
   gridB = (double **)malloc(HEIGHT*sizeof(double *));
   create2DDoubleArray(gridB);

   /* Reads the file into the grid */
   fileToGrid(year0, gridA);
   fclose(year0);

   printf("Malton at year 0:\n");
   printGrid(gridA); 

   gridCopy(gridA, gridB);

   /* The main loop of the application
    * Calculates the next densities, if stability has been found, then it breaks and outputs the final year.
    */
   for(i=0; i<years; i++){
      printf("Malton after %d years of growth\n", i+1);

      if(NextDensities(gridA, gridB) == 1){
         stabilityFound=true;
         gridCopy(gridB, gridA);    
         printGrid(gridA);
         break;
      }
      gridCopy(gridB, gridA);
      printGrid(gridA);
   }
   if(stabilityFound){
      printf(ANSI_COLOR_BLUE"steady-state reached after %d years",i+1);
      printf(ANSI_COLOR_RESET "\n");
   }
   else{
      printf(ANSI_COLOR_RED "Stability was not reached after %d years", i);
      printf(ANSI_COLOR_RESET "\n");
   }
   exit(0);
}

 /* The descriptions for all methods after this are above their prototypes. */

int create2DDoubleArray(double **array)
{
   int i, j;
   /* fills the 2D elements with a 1D array */
   for(i=0; i<HEIGHT; i++){
      for(j=0; j<WIDTH; j++){
         array[i] = (double *) malloc(HEIGHT*sizeof(double));
      }
   } 
   return 0;
}
int printGrid(double **array)
{
   int i, j, k;
   int numOfMinusSigns=7*WIDTH;

   /* prints the array as a grid, if the rows or columns are at the perimiter, it prints out the permimeter as green. */
   for(k=0; k<numOfMinusSigns; k++){
      printf(ANSI_COLOR_GREEN "-" ANSI_COLOR_RESET);
   }
   putchar('\n');
   for(i=0; i<HEIGHT; i++){
      printf(ANSI_COLOR_GREEN "|" ANSI_COLOR_RESET);
      for(j=0; j<WIDTH; j++){
         if(j==0 || j==HEIGHT-1 || i ==0 || i == HEIGHT-1)
            printf(ANSI_COLOR_GREEN "%4.1lf | ", array[i][j] );
         else 
            printf(ANSI_COLOR_RESET "%4.1lf | ", array[i][j]);
      }
      putchar('\n');
      for(k=0; k<numOfMinusSigns; k++){
         if(i==0 || i>=HEIGHT-2)
            printf(ANSI_COLOR_GREEN "-" ANSI_COLOR_RESET);
         else
            printf(ANSI_COLOR_RESET "-");
      }
      putchar('\n');
   }
   putchar('\n');
   return 0;
}

int NextDensities(double **fromGrid, double **toGrid)
{
   int i, j;
   double average;
   for(i=1; i<HEIGHT-1; i++){
      for(j=1; j<WIDTH-1; j++){
         /* Calculates the average and puts it into the next grid */
         average = (fromGrid[i][j] + fromGrid[i+1][j] + fromGrid[i-1][j] + fromGrid[i][j+1] + fromGrid[i][j-1])/5;
         toGrid[i][j] = average;
      }
   }
   if(isStable(fromGrid, toGrid) == 0)
      return 0;
   return 1; 
}

int fileToGrid(FILE *f, double **grid)
{
   rewind(f);  //move to the begining
   int i, j;
   double temp;
   i=0;
   j=0;

   while( (fscanf(f, "%lf", &grid[i][j])!=EOF)){
      i++;
      /* Once the array has reached the end of the row, it moves onto the next column. */
      if(i==HEIGHT){
         i=0;
         j++;
      }
   }
   return 0;
}
int gridCopy(double **b, double **a)
{
   int i, j;
   /* copies all cells to the next grid */
   for(i=0; i<HEIGHT; i++){
      for(j=0;j<WIDTH;j++){
         a[i][j] = b[i][j];
      }
   }
   return 0;
}
int isStable(double **a, double **b)
{
   int i, j;
   for(i=1;i<HEIGHT-1;i++){
      for(j=1;j<WIDTH-1;j++){
         /* checks to see if the difference between year and year+1 is greater than the tolerance */
         if( fabs( a[i][j]-b[i][j] ) >TOLERANCE ){
            return 0;
         }
      }
   }
   /* If it's not greater than the tolerance, then it hasn't broken through the loop and thus is stable. */
   return 1;
}
int checkFile(FILE *f)
{
   rewind(f);
   char c;
   /* checks through all the chracters in the ile for non-allowed characters */
   while( (c=getc(f)) != EOF){
      if( !(isdigit(c)))
         if( c!=' ')
            if( c != '\n' )
               return 1;
   }
   return 0;
}
int numCount(FILE *f)
{
   rewind(f);
   double d;
   int count;
   count=0;
   /* checks for a number, and returns that number */ 
   while( (fscanf(f, "%lf", &d)) != EOF){
      count++;
   }
   printf("%d\n", count);
   return count;
}
