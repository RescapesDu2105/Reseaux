#include "Protocoles.h"
#include <pthread.h>

char Value[64];
int Port_Check, Max_Clients, Taille_Max_Trame;
char IP_Server[16], Sep_Trame[2], Fin_Trame[2], Sep_CSV[2];

int hSocketConnectee[10]; /* Sockets pour clients*/
char *ConnectedUsers[10];
int indiceCourant=-1, TicketsCSV = -1;
pthread_mutex_t mutexIndiceCourant, mutexConnectedUsers;
pthread_cond_t condIndiceCourant;
pthread_t threadHandle[10]; /* Threads pour clients*/

void * fctThread(int * param);
char * getThreadIdentity();

int main (void)
{
    int hSocketService, hSocketEcoute; /* Socket d'ecoute pour l'attente */
	int i, j; /* variables d'iteration */
	struct hostent * infosHost, **temp; /*Infos sur le host : pour gethostbyname */
	struct in_addr adresseIP; /* Adresse Internet au format reseau */
	struct sockaddr_in adresseSocket;
	int tailleSockaddr_in;
	int ret;
	char msgServeur[Taille_Max_Trame];
    int Erreur = 0, Trouve = 0;
    int TypeRequete = -1;
    struct sigaction ActINT;
        
    ActINT.sa_handler = HandlerSIGINT;
    sigemptyset(&ActINT.sa_mask);
    ActINT.sa_flags = 0;
    if (sigaction(SIGINT,&ActINT,NULL) == -1)
    {   
        perror("Err de sigaction()");
        exit(1);
    }

    puts("* Thread principal serveur demarre *");
    printf("Lecture du fichier de configuration\n");

    getValue("IP_SERVER", IP_Server);
    getValue("PORT_CHCK", Value);
    Port_Check = atoi(Value);
    getValue("SEP_TRAME", Sep_Trame);
    getValue("FIN_TRAME", Fin_Trame);
    getValue("SEP_CSV", Sep_CSV);
    getValue("MAX_CLIENTS", Value);
    Max_Clients = atoi(Value);
    getValue("TAILLE_MAX_TRAME", Value);
    Taille_Max_Trame = atoi(Value);

    for (i = 0; i < Max_Clients; i++) hSocketConnectee[i] = -1;
    for (i = 0; i < Max_Clients; i++) ConnectedUsers[i] = NULL;

    ConnexionServeur(&hSocketEcoute, &infosHost, &adresseIP, IP_Server, &adresseSocket, Port_Check);

    /* Lancement des threads */
	for (i = 0; i < Max_Clients; i++)
	{
		ret = pthread_create(&threadHandle[i],NULL,(void*(*)(void*))fctThread, &i);
		printf("Thread secondaire %d lance !\n", i);
		ret = pthread_detach(threadHandle[i]);
    }
    
    do
	{
		puts("Thread principal : en attente d'une connexion");
		EcouterConnexion(hSocketEcoute);
		AccepterConnexion(&tailleSockaddr_in, &hSocketService, hSocketEcoute, &adresseSocket);

		/* Recherche d'une socket connectee libre */
		printf("Recherche d'une socket connecteee libre ...\n");
		for (j = 0; j < Max_Clients && hSocketConnectee[j] != -1; j++);
		if (j == Max_Clients)
		{
			printf("Plus de connexion disponible\n");
			sprintf(msgServeur, "%d%s%s%s", LOGIN_OFFICER, Sep_Trame, DOC, Fin_Trame);
			EnvoyerMessage(hSocketService, msgServeur);
			FermerConnexion(hSocketService);
		}
		else
		{
            /* Il y a une connexion de libre */
            printf("Connexion sur la socket num. %d\n", j);
            pthread_mutex_lock(&mutexIndiceCourant);
            hSocketConnectee[j] = hSocketService;

            indiceCourant=j;
            pthread_mutex_unlock(&mutexIndiceCourant);
            pthread_cond_signal(&condIndiceCourant);
		}
	}
	while (1);
	/* 10. Fermeture de la socket d'ecoute */
	FermerConnexion(hSocketEcoute);
	printf("Socket serveur fermee\n");
	puts("Fin du thread principal");

	exit(1);
}

void * fctThread (int *param)
{
	char * nomCli, buf[Taille_Max_Trame];
	char msgClient[Taille_Max_Trame], msgServeur[Taille_Max_Trame];
	int vr = *param, finDialogue=0, i, iCliTraite;
	char * numThr = getThreadIdentity();
	int hSocketServ;
    int TypeRequete, Res, Etat = DISCONNECTED; 
    LoginClient LC;
    VolVoyageurs VV;
    BagagesVoyageurs BV;

	while (1)
	{
		/* 1. Attente d'un client à traiter */
		pthread_mutex_lock(&mutexIndiceCourant);
		while (indiceCourant == -1)
			pthread_cond_wait(&condIndiceCourant, &mutexIndiceCourant);

		iCliTraite = indiceCourant; 
		indiceCourant= -1;
		hSocketServ = hSocketConnectee[iCliTraite];
        pthread_mutex_unlock(&mutexIndiceCourant);
        
		sprintf(buf, "Je m'occupe du numero %d ...", iCliTraite);
		affThread(numThr, buf);
        /* 2. Dialogue thread-client */
        
		do
		{
			memset(msgClient, '\0', Taille_Max_Trame);
            RecevoirMessage(hSocketServ, msgClient);
            sprintf(buf,"Message recu : %s", msgClient);
            affThread(numThr, buf);
            
            if (Etat == DISCONNECTED)
                TypeRequete = AnalyseRequeteClient(msgClient, &LC); // Res = 1 OU 0
            else if (Etat == CONNECTED)
                TypeRequete = AnalyseRequeteClient(msgClient, &VV); // Res = 2
            else if (Etat == TICKET_CHECKED)
                TypeRequete = AnalyseRequeteClient(msgClient, &BV); // Res = 3
            else if (Etat == LUGGAGES_CHECKED)
                TypeRequete = AnalyseRequeteClient(msgClient, &Res); // Res = 4
            
            switch (TypeRequete) {
                case LOGOUT_OFFICER:
                    finDialogue = 1;
                    Etat = DISCONNECTED;
                    sprintf(msgServeur, "%d%s%s%s", TypeRequete, Sep_Trame, EOC, Fin_Trame);

                    pthread_mutex_lock(&mutexConnectedUsers);        
                    free(ConnectedUsers[iCliTraite]);
                    ConnectedUsers[iCliTraite] = NULL;
                    pthread_mutex_unlock(&mutexConnectedUsers);

                    Etat = DISCONNECTED;
                    break;
                case LOGIN_OFFICER:
                //printf("LOGIN_OFFICER\n");
                    //printf("Main - Login : %s | Pwd : %s\n", LC.Login, LC.Password);
                    
                    pthread_mutex_lock(&mutexConnectedUsers);
                    Res = TraiterLogin(LC.Login, LC.Password, Sep_CSV);
                    pthread_mutex_unlock(&mutexConnectedUsers);
                                        
                    if (Res == 1)
                    {
                        Etat = CONNECTED;
                    }

                    sprintf(msgServeur, "%d%s%d%s", TypeRequete, Sep_Trame, Res, Fin_Trame);
                    break;
                case CHECK_TICKET:
                    
                    Res = verifyTicket(VV.TicketID, VV.NbAccompagnants, Sep_CSV);   
        
                    if (Res == 1) {                                                    
                        Etat = TICKET_CHECKED;
                    }
                    else if (Res == -1) {
                        Etat = CONNECTED;
                    }
        
                    sprintf(msgServeur, "%d%s%d%s", TypeRequete, Sep_Trame, Res, Fin_Trame);
                    break;
                case CHECK_LUGGAGE:
                    BV.PrixSupplement = (int)BV.Overweight * 10;
                    sprintf(msgServeur, "%d%s%.2f%s%.2f%s%d%s", TypeRequete, Sep_Trame, BV.TotalWeight, Sep_Trame, BV.Overweight, Sep_Trame, BV.PrixSupplement, Fin_Trame);
                    
                    Etat = LUGGAGES_CHECKED;
                    break;
                case PAYMENT_DONE:
                    //printf("Main Switch TypeRequete 4\n");
                    //printf("Payment done : %d\n", Res);
        
                    if (Res) {
                        Etat = LUGGAGES_DATA_SAVING;
                        for(i = 0 ; BV.Luggage[i].LuggageNumber != -1 && i < 20 ; i++) {
                            addLuggage(VV.TicketID, BV.Luggage[i].LuggageNumber, BV.Luggage[i].isSuitcase, Sep_CSV);
                        }
                    }

                    sprintf(msgServeur, "%d%s%d%s", TypeRequete, Sep_Trame, 1, Fin_Trame);
                    Etat = CONNECTED;
                    break;
                default: break;
			}

            //if (!finDialogue)
			    EnvoyerMessage(hSocketServ, msgServeur);
		}
		while (!finDialogue);
		/* 3. Fin de traitement */
		pthread_mutex_lock(&mutexIndiceCourant);
        hSocketConnectee[iCliTraite]=-1;
		pthread_mutex_unlock(&mutexIndiceCourant);
    }
    
    FermerConnexion(hSocketServ);
    
	pthread_exit(&vr);

	return 0;
}

char * getThreadIdentity()
{
	unsigned long numSequence;
	char *buf = (char *)malloc(30);
	numSequence = pthread_self( );
	sprintf(buf, "%d.%u", getpid(), numSequence);

	return buf;
}


int AnalyseRequeteClient(char *Requete, void *Struct)
{
    int Res = -1 , TypeRequete = -1;
    char *Token, *Ptr;
    char *Login, *Pwd; LoginClient *LC;
	VolVoyageurs *VV;
    char isLuggage; int i; BagagesVoyageurs *BV;
    int *Payment;


    Token = strtok_r(Requete, Sep_Trame, &Ptr);
    TypeRequete = atoi(Token);

    switch(TypeRequete)
    {
        case LOGIN_OFFICER:            
            memset(Struct, '\0', sizeof(LoginClient));  
            LC = (LoginClient *)Struct;
            Token = strtok_r(NULL, Sep_Trame, &Ptr);
            if (strcmp(Token, EOC) != 0)
            {
                strcpy(LC->Login, Token);
                Token = strtok_r(NULL, Fin_Trame, &Ptr);
                strcpy(LC->Password, Token);
            }

            break;
        case CHECK_TICKET:
            memset(Struct, '\0', sizeof(VolVoyageurs));  
            VV = (VolVoyageurs *)Struct;
            Token = strtok_r(NULL, Sep_Trame, &Ptr);
            strcpy(VV->TicketID, Token);
            Token = strtok_r(NULL, Fin_Trame, &Ptr);
            VV->NbAccompagnants = atoi(Token);           
            break;
        case CHECK_LUGGAGE:		
            BV = (BagagesVoyageurs *)Struct;
            memset(Struct, '\0', sizeof(BagagesVoyageurs));            
            for(i = 0 ; i < 20 ; i++) BV->Luggage[i].LuggageNumber = -1;
			Token = strtok_r(NULL, Sep_Trame, &Ptr);
            i = 0;
			while (Token != NULL) {
				    BV->Luggage[i].LuggageNumber = i + 1;
				if (strcmp(Token, "VALISE") == 0)
                    BV->Luggage[i].isSuitcase = 'O';
				else
                    BV->Luggage[i].isSuitcase = 'N';
				
				Token = strtok_r(NULL, Sep_Trame, &Ptr);
				BV->Luggage[i].LuggageWeight = atof(Token);
				BV->TotalWeight += BV->Luggage[i].LuggageWeight;

				if (BV->Luggage[i].LuggageWeight - 20 > 0) {
					BV->Overweight += BV->Luggage[i].LuggageWeight - 20;
                }
                
				Token = strtok_r(NULL, Sep_Trame, &Ptr);
                i++;
			}
            break;
            case PAYMENT_DONE: 
                memset(Struct, '\0', sizeof(int));
                Payment = (int *)Struct;            
                Token = strtok_r(NULL, Fin_Trame, &Ptr);
                *Payment = atoi(Token);
                break;
        default: break;
    }

    return TypeRequete;
}

int TraiterLogin(char * Login, char * Pswd, char *Sep_CSV)
{
    char **TabCSV, *LogCSV , *PswdCSV, *Ptr;
    int Trouve = 0, Sortie = 0, i;

    TabCSV=LireCSV(FICHIER_LOGINS);
    for(int i=0 ; (Trouve != 1 || Trouve != -2) && TabCSV[i]!=NULL && i<254 ; i++)
    {
        LogCSV=strtok_r(TabCSV[i], Sep_CSV, &Ptr);
        PswdCSV=strtok_r(NULL, "\r", &Ptr);  

        if(strcmp(LogCSV,Login)==0 && strncmp(PswdCSV, Pswd, strlen(Pswd))==0)
            Trouve=1;
    }
    
    for (i = 0; Trouve != -1 && i < 10 ; i++) { 
        if (ConnectedUsers[i] != NULL && strcmp(ConnectedUsers[i], Login) == 0) 
            Trouve = -1;
    }
    
    if (Trouve == 1) {
        for (i = 0; Sortie != 1 && i < 10 ; i++) { 
            if (ConnectedUsers[i] == NULL) {                
                ConnectedUsers[i] = (char*) malloc (strlen(Login));
                strcpy(ConnectedUsers[i], Login);
                Sortie = 1;
            }
        }
    }

    for(i = 0 ; i < 255 ; i++) {
        free(TabCSV[i]);
        TabCSV[i] = NULL;
    }

    return Trouve;
}

void HandlerSIGINT(int p)
{
    char msgServeur[Taille_Max_Trame];
    // Etat incohérent pour les fichier CSV potentiellement
    sprintf(msgServeur, "%d%s%s%s", LOGOUT_OFFICER, Sep_Trame, EOC, Fin_Trame);
    for (int i = 0; i < 10 ; i++) {
        EnvoyerMessage(hSocketConnectee[i], msgServeur); 
        FermerConnexion(hSocketConnectee[i]); 

        free(ConnectedUsers[i]);
        ConnectedUsers[i] = NULL;
    }

    printf("Fin du programme client !");
    exit(1);
}