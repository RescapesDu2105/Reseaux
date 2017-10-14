#ifndef ACCESSBILBAG_H_INCLUDED
#define ACCESSBILBAG_H_INCLUDED

#include "../CIMP/Protocoles.h"

#define SEP_TICKET "-"

int verifyTicket(char *number, int nbPassengers, char *Sep_CSV); //return 0 ou 1
void addLuggage (char *number, int LuggageNumber, char isSuitcase, char *Sep_CSV);

#endif
