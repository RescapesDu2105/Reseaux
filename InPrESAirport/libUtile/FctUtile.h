#ifndef FCT_UTILE_H
#define FCT_UTILE_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <math.h>
#include <termios.h>
#include <unistd.h>  
#include <limits.h>

#define clear() printf("\033[H\033[J")

void LireChaine (char *pc, int max);
void LireMdp(char *password, int Max);
void LireNombreEntier(int *pNombre, int Min, int Max);
void LireNombreDecimal(float *pNombre, float Min, float Max);
void LireOuiNon(char *pChoix);

#endif
