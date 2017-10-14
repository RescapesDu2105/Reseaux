#include "Protocoles.h"

int Etat = 0;

char Value[64];
int Port_Check, Taille_Max_Trame;
char IP_Server[16], Sep_Trame[2], Fin_Trame[2], Sep_CSV[2];

int hSocket;

int main (void)
{
    //hSocketService;
    //int i,j, /* variables d'iteration */
    //retRecv; /* Code de retour d'un recv */
    struct hostent * infosHost; /*Infos sur le host : pour gethostbyname */
    struct in_addr adresseIPClient; /* Adresse Internet au format reseau */
    struct sockaddr_in adresseSocketClient;
    unsigned int tailleSockaddr_inClient;
    int Choix1, Choix2, Res;
    char Ticket[SIZE_ID_TICKET] = "";
    //int ret, * retThread;
    //char msgServeur[Taille_Max_Trame];
    struct sigaction ActINT;


    ActINT.sa_handler = HandlerSIGINT;
    sigemptyset(&ActINT.sa_mask);
    ActINT.sa_flags = 0;
    if (sigaction(SIGINT,&ActINT,NULL) == -1)
    {   
        perror("Err de sigaction()");
        exit(1);
    }

    printf("Lecture du fichier de configuration\n");
    getValue("IP_SERVER", IP_Server);
    getValue("PORT_CHCK", Value);
    Port_Check = atoi(Value);
    getValue("SEP_TRAME", Sep_Trame);
    getValue("FIN_TRAME", Fin_Trame);
    getValue("SEP_CSV", Sep_CSV);
    getValue("TAILLE_MAX_TRAME", Value);
    Taille_Max_Trame = atoi(Value);
    
    do {
        Choix1 = 0;
        Choix1 = Menu();
        if (Choix1 == 1) {
            ConnexionClient(&hSocket, &infosHost, &adresseIPClient, IP_Server, &adresseSocketClient, Port_Check, &tailleSockaddr_inClient);             
            Etat = Connexion();
        }
        
        while(Etat == CONNECTED) {
            Choix2 = 0;
            Choix2 = Menu();
            
            switch (Choix2) {
                case 1:                    
                    Res = EnregistrementVoyageurs(Ticket);
                    if (Res == 1)
                        EnregistrementBagagesVoyageurs(Ticket);

                    break;
                case 2:
                    Etat = Deconnexion();
                    if (Etat == DISCONNECTED)
                        FermerConnexion(hSocket);
                    break;
                
                default: break;
            }
        } 
	} while (Choix1 != 2);
    
    return 0;
}


int AnalyseRequeteServeur(char *Requete) {
    int Res = -1, TypeRequete;
    char *Token;
    char Choix;

    Token = strtok(Requete, Sep_Trame);
    TypeRequete = atoi(Token);
    Token = strtok(NULL, Sep_Trame);
    
    switch(TypeRequete)
    {
        case 0:            
            break;
        case 1:
            Res = atoi(Token);
            switch (Res) {
                case -1:
                    printf("L'utilisateur est deja connecte !\n");
                    break;
                case 0:                    
                    printf("Login et/ou mot de passe erroné(s)\n");
                    break;
                case 1:
                    printf("Connecté !\n");
                    break;
                default : break;
            }
            break;
        case 2:
            Res = atoi(Token);
            if (Res == 1)
                printf("Ticket(s) valide(s)\n");
            else if (Res == -1) 
                printf("Des les tickets sont invalides\n");

            break;
        case 3:
            printf("Poids total bagages : %.2f KG\n", atof(Token));
            Token = strtok(NULL, Sep_Trame);
            printf("Token 1 = %s\n", Token);

            if (atof(Token) > 0) {
                printf("Excedent poids : %.2f KG\n", atof(Token));
                Token = strtok(NULL, Sep_Trame);
                printf("Supplément à payer : %d €\n", atoi(Token));
                
            }                
            Res = 1;
            printf("Enregistrement client effectué !\n");

            break;
        case 4:
            
            break;
        default:
            Res = -1;
            break;
    }

    return Res;
}

int Menu() {
    int Choix;

    clear();
    if (Etat == CONNECTED) {
        printf("---------- Menu ----------\n");
        printf("1. Enregistrement de voyageurs\n");
        printf("2. Se déconnecter\n");

        printf (">>> Votre Choix : ");

        LireNombreEntier(&Choix, 1, 2);
    }
    else if (Etat == DISCONNECTED) {        
        printf("---------- Menu ----------\n");
        printf("1. Connexion au serveur\n");
        printf("2. Quitter le programme\n");

        printf (">>> Votre Choix : ");

        LireNombreEntier(&Choix, 1, 2);
    }

    return Choix;
}

int Connexion() {
    char MsgClient[Taille_Max_Trame], MsgServeur[Taille_Max_Trame];
    char Login[SIZE_LOG_PSW], Mdp[SIZE_LOG_PSW]; 
    int Res = -1;     
    
    clear();
    printf(">>> Nom d'utilisateur : ");
    LireChaine(Login, SIZE_LOG_PSW);
    printf(">>> Mot de passe : ");
    LireMdp(Mdp, SIZE_LOG_PSW);
    sprintf(MsgClient, "%d%s%s%s%s%s", LOGIN_OFFICER, Sep_Trame, Login, Sep_Trame, Mdp, Fin_Trame);
    //sprintf(MsgClient, "%d%s%s%s%s%s", LOGIN_OFFICER, Sep_Trame, "zey", Sep_Trame, "123", Fin_Trame);
    //printf("MsgClient = %s\n", MsgClient);

    EnvoyerMessage(hSocket, MsgClient);    
    memset(MsgServeur, '\0', Taille_Max_Trame);
    RecevoirMessage(hSocket, MsgServeur);
    printf("Recu : %s\n", MsgServeur);
    Res = AnalyseRequeteServeur(MsgServeur);

    if (Res == 1)
        return CONNECTED;
    else
        return DISCONNECTED;
}

int Deconnexion() {
    char Choix[2];
    int Res = -1;
    char MsgClient[Taille_Max_Trame];
    
    do
    {
        printf("Etes-vous sûr de vouloir vous déconnecter ?(O/N) : ");
        LireChaine(Choix, 2);
        Choix[0] = toupper(Choix[0]);
    } while (strcmp(Choix, "O") != 0 && strcmp(Choix, "N") != 0);

    if (strcmp(Choix, "O") == 0) {
        sprintf(MsgClient, "%d%s%s%s", LOGOUT_OFFICER, Sep_Trame, EOC, Fin_Trame);
        EnvoyerMessage(hSocket, MsgClient);    
        memset(MsgClient, 0, Taille_Max_Trame);
        RecevoirMessage(hSocket, MsgClient);
        printf("Recu : %s\n", MsgClient);
        Res = AnalyseRequeteServeur(MsgClient);
        
        if (Res)
            return DISCONNECTED;
        else
            return CONNECTED;
    }
    else
        return CONNECTED;
}

int EnregistrementVoyageurs(char *Ticket) {
    char MsgClient[Taille_Max_Trame];
    int NbAccompagnants;
    int Res;

    clear();
    printf("VOL 362 POWDER-AIRLINES - Peshawar 6h30\n\n");
    printf("Numéro de billet : ");
    LireChaine(Ticket, SIZE_ID_TICKET);
    printf("Nombre d'accompagnants : ");
    LireNombreEntier(&NbAccompagnants, 0, 10);
    
    sprintf(MsgClient, "%d%s%s%s%d%s", CHECK_TICKET, Sep_Trame, Ticket, Sep_Trame, NbAccompagnants, Fin_Trame);
    EnvoyerMessage(hSocket, MsgClient);
    memset(MsgClient, '\0', Taille_Max_Trame);
    RecevoirMessage(hSocket, MsgClient);
    printf("Recu : %s\n", MsgClient);
    Res = AnalyseRequeteServeur(MsgClient);
    printf("Res : %d\n", Res);

    return Res;
}

int EnregistrementBagagesVoyageurs(char *Ticket) {
    char MsgClient[Taille_Max_Trame];
    char Choix;
    int Res, i = 0;
    float Poids;
    
    memset(MsgClient, '\0', Taille_Max_Trame);
    sprintf(MsgClient, "%d", CHECK_LUGGAGE);
    do {
        i++;
        strcat(MsgClient, Sep_Trame);
        printf("----- Bagage n°%d -----\n", i);
        printf("Valise ? (O/N) : ");
        LireOuiNon(&Choix);
        if (Choix = 'O')
            sprintf(MsgClient, "%sVALISE%s", MsgClient, Sep_Trame);
        else
            sprintf(MsgClient, "%sPASVALISE%s", MsgClient, Sep_Trame);

        printf("Poids du bagage n°%d : ", i);
        LireNombreDecimal(&Poids, 0, 75);            
        sprintf(MsgClient, "%s%f", MsgClient, Poids);

        printf("Ajouter un bagage ? (O/N) : ");
        Choix = 'I';
        LireOuiNon(&Choix);
        printf("\n\n");
    } while (Choix == 'O');
    strcat(MsgClient, Fin_Trame);

    EnvoyerMessage(hSocket, MsgClient);
    memset(MsgClient, '\0', Taille_Max_Trame);
    RecevoirMessage(hSocket, MsgClient);
    printf("Recu : %s\n", MsgClient);
    Res = AnalyseRequeteServeur(MsgClient);

    if (Res) {
        printf("Paiement effectué ? (O/N) : ");
        Choix = 'I';
        LireOuiNon(&Choix);
        if (Choix == 'N') {             
            printf("Voyage client annulé !\n");
        }
        sprintf(MsgClient, "%d%s%d%s", PAYMENT_DONE, Sep_Trame, Res, Fin_Trame);
        
        EnvoyerMessage(hSocket, MsgClient);    
        memset(MsgClient, '\0', Taille_Max_Trame);
        RecevoirMessage(hSocket, MsgClient);
        printf("Recu : %s\n", MsgClient);
        Res = AnalyseRequeteServeur(MsgClient);
        printf("Transaction terminé !\n");
    }

    return Res;
}

void HandlerSIGINT(int p)
{
    char MsgClient[Taille_Max_Trame];
    
    if (Etat == CONNECTED) {
        sprintf(MsgClient, "%d%s%s%s", LOGOUT_OFFICER, Sep_Trame, EOC, Fin_Trame);
        EnvoyerMessage(hSocket, MsgClient);  
    }

    printf("Fin du programme client !");
    exit(1);
}