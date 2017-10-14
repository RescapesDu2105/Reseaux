#ifndef PROTOCOLES_H_INCLUDED
#define PROTOCOLES_H_INCLUDED

#include <ctype.h>
#include <signal.h>
#include "../SocketUtilities/SocketUtilities.h"
#include "../libConfig/Config.h"
#include "../libCSV/CSV.h"
#include "../libUtile/FctUtile.h"
#include "../AccessBilBag/AccessBilBag.h"

#define FICHIER_LOGINS "CIMP/logins.csv"
#define FICHIER_TICKETS "CIMP/tickets.csv"

#define SIZE_LOG_PSW 25
#define SIZE_ID_TICKET 18

// Requêtes
#define LOGOUT_OFFICER 0
#define LOGIN_OFFICER 1
#define CHECK_TICKET 2
#define CHECK_LUGGAGE 3
#define PAYMENT_DONE 4

// Etats
#define CONNECTION_CLOSED -1
#define DISCONNECTED 0
#define CONNECTED 1
#define TICKET_CHECKED 2
#define LUGGAGES_CHECKED 3
#define LUGGAGES_DATA_SAVING 4

#define affThread(num, msg) if (msg) printf("th_%s> %s\n", num, msg)

struct LoginClient {
    char Login[SIZE_LOG_PSW];
    char Password[SIZE_LOG_PSW];
};
typedef struct LoginClient LoginClient;

struct VolVoyageurs {
    char TicketID[SIZE_ID_TICKET];
    int NbAccompagnants;
};
typedef struct VolVoyageurs VolVoyageurs;

struct Bagage {
    char isSuitcase;
    int LuggageNumber;
    float LuggageWeight;
};
typedef struct Bagage Bagage;

struct BagagesVoyageurs {
    Bagage Luggage[20];
    float TotalWeight;
    float Overweight;
    int PrixSupplement;
};
typedef struct BagagesVoyageurs BagagesVoyageurs;


int AnalyseRequeteClient(char *Requete, void *Struct);
int TraiterLogin(char * Login, char * Pswd, char *Sep_CSV);
void HandlerSIGINT(int);

int AnalyseRequeteServeur(char *Requete);
int Menu();
int Connexion();
int Deconnexion();
int EnregistrementVoyageurs();
int EnregistrementBagagesVoyageurs();



#endif
