#include "AccessBilBag.h"

int verifyTicket(char *number, int nbPassengers, char *Sep_CSV) {
    char **TabCSV;
    char *Ptr, *Ticket;
    int NbAccompagnants;
    int Ok = -1;
    int i, j;

    TabCSV = LireCSV(FICHIER_TICKETS);

    for(i=0 ; Ok != 0 && TabCSV[i] != NULL && i < MAXSTRING ; i++)
    {
        Ticket = strtok_r(TabCSV[i], Sep_CSV, &Ptr);

        if(strcmp(number, Ticket) == 0)
          Ok = 0;
    }
    
    for (j = nbPassengers ; Ticket != NULL ; j--) {
        Ticket = strtok_r(NULL, Sep_CSV, &Ptr);
    }
    
    j++;
    if(j == 0)
        Ok = 1;
    else
        Ok = -1;
    
    for(i = 0 ; i < 255 ; i++) {
        free(TabCSV[i]);
        TabCSV[i] = NULL;
    }

    return Ok;
}

void addLuggage (char *number, int LuggageNumber, char isSuitcase, char *Sep_CSV) {
    char *Token, *Ptr, LigneCSV[MAXSTRING], NomFichier[50];
    char TicketID[SIZE_ID_TICKET], *NumeroVol, *DateVol, *NumeroTicket; 

    strcpy(TicketID, number);
    NumeroVol = strtok_r(TicketID, SEP_TICKET, &Ptr);
    DateVol = strtok_r(NULL, SEP_TICKET, &Ptr);
    NumeroTicket = strtok_r(NULL, SEP_TICKET, &Ptr);

    sprintf(NomFichier, "%s_%s_lug.csv", NumeroVol, DateVol);

    if(isSuitcase == 'O')
        sprintf(LigneCSV, "%s-WACHARVILROM-%s-%s-%d%sVALISE", NumeroVol, DateVol, NumeroTicket, LuggageNumber, Sep_CSV);
    else
        sprintf(LigneCSV, "%s-WACHARVILROM-%s-%s-%d%sPASVALISE", NumeroVol, DateVol, NumeroTicket, LuggageNumber, Sep_CSV);

    EcrireCSV(NomFichier, LigneCSV);
}
