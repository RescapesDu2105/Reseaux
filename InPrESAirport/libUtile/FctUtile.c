#include "FctUtile.h"

void LireChaine (char *pc, int Max)
{
	char c;
    int i=0, FirstInput;
    
    do
    {
        if (FirstInput == 0)
            while((c = getchar()) != '\n' && c != EOF);

        c = getchar();
        while ((i < Max-1) && (c != '\n'))
        {
            *pc = c;
            pc++;
            i++;
            c = getchar();
        }
        if (c == '\n')
        {
            *pc = '\0';
        }
        else
        {
            printf ("Depassement de capacite !\n");
            FirstInput = 0;
            i = -1;
        }
    }while (i==-1);
}

void LireMdp(char *password, int Max)
{
    static struct termios oldt, newt;
    int i, c, FirstInput = 1;

    do {
        /*saving the old settings of STDIN_FILENO and copy settings for resetting*/
        tcgetattr( STDIN_FILENO, &oldt);
        newt = oldt;

        /*setting the approriate bit in the termios struct*/
        newt.c_lflag &= ~(ECHO);

        /*setting the new bits*/
        tcsetattr( STDIN_FILENO, TCSANOW, &newt);

        /*reading the password from the console*/
        i = 0;

        if (FirstInput == 0)
            while((c = getchar()) != '\n' && c != EOF);

        c = getchar();
        while (i < (Max - 1) && c != '\n'){
            *password = c;
            password++;
            i++;
            c = getchar();
        }
        
        /*resetting our old STDIN_FILENO*/
        tcsetattr( STDIN_FILENO, TCSANOW, &oldt);

        if (c == '\n') {
            *password = '\0';   
            //strcpy(password, password);
            //printf("\nWUT\n");
        }
        else {
            printf("\nMot de passe trop long !\n");
            FirstInput = 0;
            i = -1;
        }

        //printf("password : %s*", (password - i));
    } while (i == -1);
}

void LireNombreEntier (int *pNombre, int Min, int Max)
{
    char c;
    int i, FirstInput = 1;

    do
    {
        i = 0;

        if (FirstInput == 0)
            while((c = getchar()) != '\n' && c != EOF);

        c = getchar();

        while ((c >= '0' && c <= '9') && i <= Max)
        {
            i = i * 10 + (c - 48);
            c = getchar ();
        }

        if(i < Min || i > Max || c != '\n')
        {
            printf("\nErreur : Valeur ou caractere saisi incorrect !\nVeuillez entrer une valeur comprise entre %hd et %hd : ", Min, Max);
            FirstInput = 0;
        }
    } while(i < Min || i > Max || c != '\n');

    *pNombre = i;
}

void LireNombreDecimal (float *pNombre, float Min, float Max)
{
    char c;
    float i, j;
    int CptComma, Cpt, Erreur, FirstInput = 1;

    do
    {
        i = 0;
        j = 0;
        CptComma = 0;
        Cpt = 0;
        Erreur = 0;

        if (FirstInput == 0)
            while((c = getchar()) != '\n' && c != EOF);

        c = getchar();

        if (c == ',')
            Erreur = 1;

        while (Erreur == 0 && ((c >= '0' && c <= '9') || c == ',') && i <= Max)
        {
            if (c == ',')
                CptComma++;
            else if (CptComma == 0)
                i = i * 10 + (c - 48);
            else if (CptComma == 1) {
                j = j * 10 + (c - 48);
                Cpt++;
            }

            if (CptComma <= 1)
                c = getchar ();
            else
                Erreur = 1;
        }

        if(Erreur || i < Min || i > Max || c != '\n')
        {
            printf("\nErreur : Valeur ou caractere saisi incorrect !\nVeuillez entrer une valeur comprise entre %hd et %hd : ", Min, Max);
            FirstInput = 0;
        }
        else {
            if (CptComma == 1) {
                j = j /pow (10, Cpt);
                i = i + j;
            }

        }
    } while(i < Min || i > Max || c != '\n');

    *pNombre = ceilf(i*100)/100;
}


void LireOuiNon(char *pChoix)
{
    int FirstInput = 1;
    char c;
    
    do
    {
        if (FirstInput == 0)
            while((c = getchar()) != '\n' && c != EOF);

        *pChoix = getchar();
        *pChoix = toupper(*pChoix);
        
        if((getchar() != '\n') && *pChoix != 'N' && *pChoix != 'O')
        {
            printf("\nLettre invalide. O pour Oui, N pour Non : ");
            FirstInput = 0;
        }
    } while(*pChoix != 'N' && *pChoix != 'O');
}