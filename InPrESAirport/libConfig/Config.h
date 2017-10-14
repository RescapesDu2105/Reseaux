#ifndef CONFIG_H_INCLUDED
#define CONFIG_H_INCLUDED

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define FICHIER_CONF "CIMP/serveur_checkin.conf"

#define PORT_CHCK 1
#define PORT_MAX 2

#define DELIMITEUR_CONF "=\r"
#define MAXSTRING 512

void getValue(char* Config, char *Value);
void setValue(char* Config, char *Value);
void ChangerPort(int Type, int Value);

#endif