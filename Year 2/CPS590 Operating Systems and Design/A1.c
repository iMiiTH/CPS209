/*
 * CPS590 Operating Systems
 * Assignment 1
 * Multithreaded Conway's Game of Life without using pthreads
 * Mitchell Mohorovich
 */


#include <stdio.h>
#include <stdlib.h>

#define ERR_INVALID_ARGLIST     1
#define ERR_BAD_NUM_YEARS       2
#define ERR_BAD_FILENAME        3
#define ERR_BAD_FILE_FORMAT     4

#define FALSE   0
#define TRUE    !FALSE

#define GRID_A 0
#define GRID_B 1

typedef struct Grid {
   int rows;
   int cols;
   int **grid;
} Grid;


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

Grid *grid_a;
Grid *grid_b;
Grid *reference_grid;
Grid *modifying_grid;;

void single_threaded_run()
{
   int i, j, temp_state;
   for(i = 0; i<modifying_grid->rows; i++) {
      for( j = 0; j<modifying_grid->cols; j++) {
         grid_set(modifying_grid, i, j, next_cell_state(reference_grid, i, j));
      }
   }
}

void multi_threaded_run()
{
}

void run_thread()
{
}


int main (int argc, char ** argv)
{
   FILE *f = fopen(argv[1], "r");
   if(f==NULL){
      printf("ERROR: Invalid file given.\n");
      exit(1);
   }

   int grid_size;
   fscanf(f, "%d", &grid_size);
   printf("size=%d\n", grid_size);

   grid_a = initialize_grid(grid_size, grid_size);
   grid_b = initialize_grid(grid_size, grid_size);
   if(read_grid(grid_a, f)==ERR_BAD_FILE_FORMAT) {
      printf("Error: Incorrect file contents\n");
   }

   printf("Starting grid:\n");
   print_grid(grid_a);
   sleep(1);
   while(1){ 
      reference_grid = grid_a;
      modifying_grid = grid_b;
      print_grid(reference_grid);
      single_threaded_run();
      sleep(1);
      reference_grid = grid_b;
      modifying_grid = grid_a;
      print_grid(reference_grid);
      sleep(1);
   }

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
   for (i = 0; i < g->rows; ++i) {
      for (j = 0; j < g->cols; ++j) {
         printf("%d ", grid_at(g, i, j));
      }
      putchar('\n');
   }
   putchar('\n');
}

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

int neighbours_at(Grid *g, int x, int y)
{
   int tempx = x;
   int tempy = y;
   fix_coordinates(g, &tempx, &tempy);
   return grid_at(g, tempx, tempy);
}
