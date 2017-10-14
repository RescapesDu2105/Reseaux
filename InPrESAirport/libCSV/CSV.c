#include "CSV.h"

char** LireCSV (char* NomFichier) {
    int i = 0;
    char buff[MAXSTRING];
    static char *Tab[255];
    FILE *Fichier = NULL;

    for (i = 0; i < 255 ; i++) Tab[i] = NULL;

    i = 0;
    Fichier = fopen (NomFichier, "rb");
    if (Fichier == NULL)
    {
        perror("Erreur rencontree lors de l'ouverture du fichier !\n");
    }
    else
    {
        while (fgets(buff, MAXSTRING, Fichier))
        {
            Tab[i] = (char *) malloc (strlen(buff + 1));
            strcpy(Tab[i], buff);

            i++;
        }
        fclose(Fichier);
    }

    return Tab;
}

void EcrireCSV(char* NomFichier, char* Contenu) {
    FILE *Fichier = NULL;
    char *str;

    Fichier = fopen (NomFichier, "ab");
    if (Fichier == NULL)
    {
        perror("Erreur rencontree lors de l'ouverture du fichier !\n");
    }
    else
    {
        //printf("strlen 8 : %d\n", strlen(Contenu));
        str = (char*) malloc (strlen(Contenu) + 1);

        strcpy(str, Contenu);
        strcat(str, "\n");
        fwrite(str, 1, strlen(str), Fichier);
        fclose(Fichier);
        //printf("Fin EcrireCSV 9\n");
        free(str);
        str = NULL;
    }
}
