#include <stdio.h>
#include <math.h>

double NiquistCapacity ( double B, double M ) ;
double ShannonCapacity ( double B, double SNR ) ;

int main (void) {

   double Bw, SNR, M; /* bandwidth, signal to noise ration and number of signalling levels */

   double Ncapacity, Scapacity ;

   printf("Enter bandwidth in Hz (between 1000 and 4000Hz) and number of signalling levels  (should be power of two, say 2, 4, 8 etc.) for Niquist formula: ");


   scanf("%lf %lf", &Bw, &M ) ;     //COMPLETE THIS 


   //COMPLETE THIS
   Ncapacity = NiquistCapacity(Bw, M);
   printf("Niquist capacity is:\n");

   //COMPLETE THIS
   printf("%lf", Ncapacity);

   printf("Enter bandwidth in Hz (between 1000 and 4000Hz) and signal to noise ratio (larger than 100 and lower than 300) for Shannon formula: ");


   scanf("%lf %lf", &Bw, &SNR) ;     //COMPLETE THIS

   Scapacity = ShannonCapacity(Bw, SNR);
   //COMPLETE THIS
   printf("Shannon capacity is:\n");

   //COMPLETE THIS
   printf("%lf", Scapacity);


   return 0;
}

double NiquistCapacity ( double Bw, double M ) {
   //COMPLETE THIS
   return (2*Bw * log2(M));
}
double ShannonCapacity ( double Bw, double SNR) {
   //COMPLETE THIS
   return(Bw * log2(1+SNR));
}
