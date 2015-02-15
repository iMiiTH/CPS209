/*
 * CPS590 Operating Systems and Design
 * Assignment 1
 * Multithreaded Conway's Game of Life without using pthreads
 * Mitchell Mohorovich
 */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sched.h>
#include <math.h>
#include <signal.h>

#define ERR_INVALID_ARGLIST     1
#define ERR_BAD_NUM_YEARS       2
#define ERR_BAD_FILENAME        3
#define ERR_BAD_FILE_FORMAT     4

typedef struct Grid {
   int test;
   int rows;
   int cols;
   int **grid;
} Grid;

typedef struct Stack {
   char *stack;
   char *stackTop;
} Stack;

typedef struct Thread_args {
   int thread_ind; //thread index 
   int thread_inc; //thread increment 
   Grid *mod_grid; //modifying grid
   Grid *ref_grid; //reference grid 
} Thread_args;


Grid *initialize_grid(int rows, int cols);
void free_grid(Grid **grid);
int grid_at(Grid *g, int x, int y);
void grid_set(Grid *g, int x, int y, int i);
int read_grid(Grid *grid, FILE *file);
void print_grid(Grid *g);
int next_cell_state(Grid *g, int x, int y);
void fix_coordinates(Grid *g, int *x, int *y); //DON'T WANT ANY SEGMENTATION FAULTS NOW DO WE?!
int next_state(int c, int n);
int neighbours_at(Grid *g, int x, int y);
int multi_threaded_run(void * arg);
void single_threaded_run(); //loolwe 2003 in hear. 

const int STACK_SIZE = 65536;

// I know I'm an awful person for using global variables, but like, eh.
Grid *reference_grid;
Grid *modifying_grid;
int thread_grid_index_increment; //how verbose can you goooOOOO?

int main (int argc, char ** argv)
{
   Grid *grid_a;
   Grid *grid_b;
   int i, number_of_threads, grid_size;
   unsigned int *cpids;       //keeps track of children's pid's for synchronization
   Stack *thread_stacks;      // Stack structs for easier readability

   // Argument checking
   if( argc != 3 ) {
      fprintf(stderr, "%s: invalid number of arguments\n", argv[0]);
      fprintf(stderr, "requires: [input file] [number of threads]\n");
      exit(ERR_INVALID_ARGLIST);
   }
   FILE *f = fopen(argv[1], "r");
   if(f==NULL){
      printf("ERROR: Invalid file given.\n");
      exit(ERR_BAD_FILENAME);
   }

   // Reading from the file
   fscanf(f, "%d", &grid_size);
   number_of_threads =  atoi(argv[2]);
   thread_grid_index_increment = ceil( (float)grid_size / (float)number_of_threads);

   // Creating the grids 
   grid_a = initialize_grid(grid_size, grid_size);
   grid_b = initialize_grid(grid_size, grid_size);
   if(read_grid(grid_a, f)==ERR_BAD_FILE_FORMAT) {
      printf("Error: Incorrect file contents\n");
      exit(ERR_BAD_FILE_FORMAT);
   }


   // Create stacks for each thread
   thread_stacks = (Stack *) malloc(sizeof(Stack) * number_of_threads);
   cpids = (unsigned int *)malloc(sizeof(unsigned int) * number_of_threads);
   //Populate the stacks
   for(i = 0; i < number_of_threads; i++) {
      thread_stacks[i].stack = (char *)malloc(STACK_SIZE);
      thread_stacks[i].stackTop = thread_stacks[i].stack + STACK_SIZE;
   }

   // Create array to hold the thread indexes, which just hold i, 
   // but if need to be outside the scope of the logic for loop or 
   // else super duper scary things happen.
   int args[number_of_threads];
   for(i = 0; i < number_of_threads; i++)
      args[i] = i;

   print_grid(grid_a);
   sleep(1);

   // And we have lift off.
   // This loop toggles between grid_a and grid_b being the reference_grid and the modifying_grid.
   // It spawns as many threads as the user specifies, and passes the function the thread index.
   // -> The thread index is what tells the thread which row to traverse over.
   // It keeps track of the pid's of each thread and continues once they're all dead. RIP in pieces. :'(
   // It then switches the reference_grid and the modifying grid and continues it again until it's killed. 
   while(1) {
      reference_grid = grid_a;
      modifying_grid = grid_b;

      for(i = 0; i < number_of_threads; i++) {
         args[i] = i;
         cpids[i] = clone(multi_threaded_run, thread_stacks[i].stackTop, CLONE_VM|SIGCHLD, (void *)(&args[i]));
      }

      // Syncrhonize threads.
      for( i = 0; i < number_of_threads; i++) {
         waitpid(-1, cpids[i], NULL);
      }
      print_grid(modifying_grid);
      sleep(1);

      reference_grid = grid_b;
      modifying_grid = grid_a;

      for(i = 0; i < number_of_threads; i++) {
         args[i] = i;
         cpids[i] = clone(multi_threaded_run, thread_stacks[i].stackTop, CLONE_VM|SIGCHLD, (void *)(&args[i]));
      }
      for( i = 0; i < number_of_threads; i++) { //synchronize threads
         waitpid(-1, cpids[i], NULL);
      }
      print_grid(modifying_grid);
      sleep(1);
   }

   /* The single threaddd loop.
   while(1){ 
      reference_grid = grid_a;
      modifying_grid = grid_b;
      print_grid(reference_grid);
      single_threaded_run();
      wait(); 
      sleep(1);
      reference_grid = grid_b;
      modifying_grid = grid_a;
      print_grid(reference_grid);
      wait();
      sleep(1);
   }
   */

   return 0;
}

/**
 * Creates a grid ADT with the specified number of rows and columns.
 * @param rows the number of rows
 * @param cols the number of cols
 * @return the new grid.
 */
Grid *initialize_grid(int rows, int cols)
{
   Grid *g = malloc(sizeof *g);
   int i;
   //allocate the columns first, then each row individually
   int **arr = calloc(rows, sizeof *arr);
   for (i = 0; i < rows; ++i) {
      arr[i] = calloc(cols, sizeof **arr);
   }
   g->grid = arr;
   g->rows = rows;
   g->cols = cols;
   return g;
}


/**
 * Frees the given grid.
 * @param grid a pointer to the grid to be freed.
 */
void free_grid(Grid **grid)
{
   int i;
   Grid *g = *grid;
   //free each row in the grid first
   for (i = 0; i < g->rows; ++i) {
      free(g->grid[i]);
   }
   free(g->grid);
   free(g);
   *grid = NULL;
}

/**
 * Gets the element of the grid at the specified coordinates.
 * @param g the grid
 * @param x the x-coordinate
 * @param y the y-coordinate
 * @return the element at the specifed coordinates.
 */
int grid_at(Grid *g, int x, int y) 
{
   /*
      if(g->grid[x][y] == 1)
      return '#';
      else return '-';
      */
   return g->grid[x][y];
}

/**
 * Sets the element of the grid at the specified coordinates.
 * @param g the grid
 * @param x the x-coordinate
 * @param y the y-coordinate
 * @param i the element to set.
 */
void grid_set(Grid *g, int x, int y, int i)
{
   //printf("set %d to %d,%d\n", i, x, y);
   g->grid[x][y] = i;
}

int read_grid(Grid *grid, FILE *file)
{
   int i, j;
   /* loop through the array, and scan the file's numbers into the array*/
   for (i = 0; i < grid->rows && !feof(file); i++) {
      for (j = 0; j < grid->cols && !feof(file); j++) {
         fscanf(file, "%d", &grid->grid[i][j]);
         printf(":%d", grid->grid[i][j]);
      }
      putchar('\n');
   }
   /* if we did not fill the entire array */
   if (i != grid->rows || j != grid->cols)
      return ERR_BAD_FILE_FORMAT;
   return EXIT_SUCCESS;
}

/**
 *  * Prints the given grid.
 *   * @param g the grid to print.
 *    */
void print_grid(Grid *g)
{
   system("clear");
   int i, j;
   //loop through the grid and print each cell
   for (i = 0; i < g->rows+2; i++) {
      putchar('-');
   }
   putchar('\n');
   for (i = 0; i < g->rows; ++i) {
      putchar('|');
      for (j = 0; j < g->cols; ++j) {
         if(grid_at(g, i, j) == 1){
            putchar('x');
         } else {
            putchar(' ');
         }
      }
      putchar('|');
      putchar('\n');
   }
   for (i = 0; i < g->rows+2; i++) {
      putchar('-');
   }
   putchar('\n');
}

/**
 * For a given coordinate, checks how many neighbours it has.
 * @param g the given Grid to check
 * @param x the x coodinate (row)
 * @param y the y coordinate (column)
 */
int next_cell_state(Grid *g, int x, int y)
{
   int tempx, tempy;
   int neighbour_count = 0;

   neighbour_count += neighbours_at(g, x-1, y-1);
   neighbour_count += neighbours_at(g, x-1, y);
   neighbour_count += neighbours_at(g, x-1, y+1);
   neighbour_count += neighbours_at(g, x, y-1);
   neighbour_count += neighbours_at(g, x, y+1);
   neighbour_count += neighbours_at(g, x+1, y-1);
   neighbour_count += neighbours_at(g, x+1, y);
   neighbour_count += neighbours_at(g, x+1, y+1);

   return next_state(grid_at(g, x, y), neighbour_count);
}

/**
 * Makes sure that the coordinates are within bounds.
 * If they are found not to, they wrap around the array.
 * @param g the grid which must contain the index
 * @param x a pointer to the x index
 * @param y a pointer to the y index
 */
void fix_coordinates(Grid *g, int *x, int *y)
{
   int tempx = *x;
   int tempy = *y;

   if( tempx < 0) 
      tempx  = g->rows + tempx;
   if( tempx > (g->rows)-1 )
      tempx = tempx - g->rows;

   if(tempy < 0)
      tempy = g->cols + tempy;
   if(tempy > (g->cols)-1 )
      tempy = tempy - g->cols;

   *x = tempx;
   *y = tempy; 
}

/**
 * Given a number of neighbours, and the current state; gives back the next state.
 * @param c The current state.
 * @param n the number of neighbours.
 */
int next_state(int c, int n)
{
   if(n < 2)
      return 0;
   if(n > 3)
      return 0;
   if(n == 3)
      return 1;
   if( n >= 2 && n <= 3)
      return c;
}


/**
 * Given a grid and coordinate, this function counts how many neighbours surround it. 
 * @param g The current grid
 * @param x the x coorindate (row)
 * @param y the y coorindate (column)
 * @return The number of neighbours the coordinates have
 */
int neighbours_at(Grid *g, int x, int y)
{
   int tempx = x;
   int tempy = y;
   fix_coordinates(g, &tempx, &tempy);
   return grid_at(g, tempx, tempy);
}

/**
 * This function is sent to threads to calculate the next state for a given number of rows.
 * The row interval is determined by the thread index, and the increment amount, which is
 * based on the size of the grid, and the number of threads the user has set.
 * @param arg A void pointer to the thread index (which is i in the for loop in main)
 * @return The exit code.
 */
int multi_threaded_run(void * arg)
{
   int thread_number = 0;
   if(arg != NULL) {
      thread_number = *((int *)arg);
   } else {
      printf("arg is null\n");
      _exit(0);
   }

   int start, stop, i, j;
   start = thread_number * thread_grid_index_increment;
   stop = (thread_number+1) * thread_grid_index_increment;
   
   //make sure the index doesn't go out of bounds.
   if(stop > modifying_grid->rows) {
      stop = modifying_grid->rows;
   }

   for(i = start; i<stop; i++) {
      for( j = 0; j<modifying_grid->cols; j++) {
         grid_set(modifying_grid, i, j, next_cell_state(reference_grid, i, j));
      }
   }

   _exit(0);
}

/**
 * This function is a single threaded version of Conways Game of Life.
 * It uses global variables: modifying_grid, reference_grid to get the next iteration of the game.
 */
void single_threaded_run()
{
   int i, j, temp_state;
   for(i = 0; i<modifying_grid->rows; i++) {
      for( j = 0; j<modifying_grid->cols; j++) {
         grid_set(modifying_grid, i, j, next_cell_state(reference_grid, i, j));
      }
   }
}
