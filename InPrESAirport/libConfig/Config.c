#include "Config.h"

void getValue(char* Config, char *Value) {
    int i = 1, Sortie = 0;
    char buff[MAXSTRING], buff2[MAXSTRING] = "";
    char *Token, *Ptr;
    FILE *Fichier = NULL;

    Fichier = fopen (FICHIER_CONF, "rb");
    if (Fichier == NULL)
    {
        perror("Erreur rencontree lors de l'ouverture du fichier !\n");
    }
    else
    {
        while (!Sortie && fgets(buff, MAXSTRING, Fichier))
        {
            Token = strtok_r(buff, DELIMITEUR_CONF, &Ptr);
            if (strcmp(Token, Config) == 0)
            {
                Sortie = 1; 
                Token = strtok_r(NULL, DELIMITEUR_CONF, &Ptr);
                strcpy(Value, Token);
            }
        }      
        fclose(Fichier);
    }
}

void setValue(char* Config, char* Value) {
    int i = 1, Sortie = 0;
    char buff[MAXSTRING];
    char *Token, *Ptr;
    FILE *Fichier = NULL;
    long offset;

    Fichier = fopen (FICHIER_CONF, "r+b");
    if (Fichier == NULL)
    {
        perror("Erreur rencontree lors de l'ouverture du fichier !\n");
    }
    else
    {
        while (!Sortie && fgets(buff, MAXSTRING, Fichier))
        {
            Token = strtok_r(buff, DELIMITEUR_CONF, &Ptr);
            if (strcmp(Token, Config) == 0)
            {
                Sortie = 1;

                Token = strtok_r(NULL, DELIMITEUR_CONF, &Ptr);
                fseek(Fichier, - strlen(Token), SEEK_CUR);
                fprintf(Fichier, "%s", Value);
            }
        }
        fclose(Fichier);
    }
}

void ChangerPort(int Type, int Value){
    int Sortie = 0;
    char buff[MAXSTRING];
    char *Token, *Ptr;
    FILE *Fichier = NULL;
    long offset;

    Fichier = fopen (FICHIER_CONF, "r+b");
    if (Fichier == NULL)
    {
        perror("Erreur rencontree lors de l'ouverture du fichier !\n");
    }
    else
    {
        while (!Sortie && fgets(buff, MAXSTRING, Fichier))
        {
            Token = strtok_r(buff, DELIMITEUR_CONF, &Ptr);
            if ((Type == PORT_CHCK && strcmp(Token, "PORT_CHCK") == 0) || (Type == PORT_MAX && strcmp(Token, "PORT_MAX")))
            {
                Sortie = 1;

                Token = strtok_r(NULL, DELIMITEUR_CONF, &Ptr);
                fseek(Fichier, - strlen(Token), SEEK_CUR);
                fprintf(Fichier, "%d", Value);
            }
        }
        fclose(Fichier);
    }
}