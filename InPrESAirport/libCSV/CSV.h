#ifndef UTIL_CSV_H_INCLUDED
#define UTIL_CSV_H_INCLUDED

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

#define MAXSTRING 512

char** LireCSV (char* NomFichier);
void EcrireCSV(char* NomFichier, char* Contenu);

#endif