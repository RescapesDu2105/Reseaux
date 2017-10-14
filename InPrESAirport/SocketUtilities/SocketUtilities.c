#include "SocketUtilities.h"

void CreerSocket(int *hSocket)
{
	*hSocket = socket(AF_INET, SOCK_STREAM, 0);
	if (*hSocket == -1)
	{
		printf("Erreur de creation de la socket %d\n", errno);
		exit(1);
	}
	else
		printf("Creation de la socket OK\n");
}

void getInfosHost(struct hostent **infosHost, struct in_addr *adresseIP, char *IP)
{
	if ( (*infosHost = gethostbyname(IP)) == 0)
	{
		printf("Erreur d'acquisition d'infos sur le host distant %d\n", errno);
		exit(1);
	}
	else
		printf("Acquisition infos host distant OK\n");

	memcpy(adresseIP, (*infosHost)->h_addr, (*infosHost)->h_length);
	printf("Adresse IP = %s\n",inet_ntoa(*adresseIP));
}

void PreparerSockAddr_In(struct sockaddr_in *adresseSocket, struct hostent *infosHost, int Port)
{
	memset(adresseSocket, 0, sizeof(struct sockaddr_in));
	adresseSocket->sin_family = AF_INET; /* Domaine */
	adresseSocket->sin_port = htons(Port);

	/* conversion numéro de port au format réseau */
	memcpy(&adresseSocket->sin_addr, infosHost->h_addr,infosHost->h_length);
}

void BindSocket(int hSocketEcoute, struct sockaddr_in *adresseSocket)
{
	if (bind(hSocketEcoute, (struct sockaddr *)adresseSocket, sizeof(struct sockaddr_in)) == -1)
	{
		printf("Erreur sur le bind de la socket %d\n", errno);
		exit(1);
	}
	else
		printf("Bind adresse et port socket OK\n");
}

void EcouterConnexion(int hSocketEcoute)
{
	if (listen(hSocketEcoute,SOMAXCONN) == -1)
	{
		printf("Erreur sur le listen de la socket %d\n", errno);
		FermerConnexion(hSocketEcoute);
		exit(1);
	}
	else
		printf("Listen socket OK\n");
}

void AccepterConnexion (int *tailleSockaddr_in, int *hSocketService, int hSocketEcoute, struct sockaddr_in *adresseSocket)
{
	*tailleSockaddr_in = sizeof(struct sockaddr_in);
	if ( (*hSocketService = accept(hSocketEcoute, (struct sockaddr *)adresseSocket, tailleSockaddr_in) ) == -1)
	{
		printf("Erreur sur l'accept de la socket %d\n", errno);
		FermerConnexion(hSocketEcoute);
		exit(1);
	}
	else
		printf("Accept socket OK\n");
}

void OuvrirConnexion (unsigned int *tailleSockaddr_in, int hSocket, struct sockaddr_in *adresseSocket)
{
	int ret;

	*tailleSockaddr_in = sizeof(struct sockaddr_in);
	if (( ret = connect(hSocket, (struct sockaddr *)adresseSocket, *tailleSockaddr_in) )== -1)
	{
		printf("Erreur sur connect de la socket %d\n", errno);
		FermerConnexion(hSocket);
		exit(1);
	}
	else
		printf("Connect socket OK\n\n");
}

void EnvoyerMessage(int hSocket, char *Msg)
{	
	char MsgSocket[TAILLE_MAX_TRAME] = "";

	sprintf(MsgSocket, "%d%s%s", strlen(Msg), SEPARATOR, Msg);

	if (send(hSocket, MsgSocket, TAILLE_MAX_TRAME, 0) == -1)
	{
		printf("Erreur sur le send de la socket %d\n", errno);
		FermerConnexion(hSocket);
		exit(1);
	}

	printf("\nMessage envoye : %s\n", MsgSocket);
}

void EnvoyerStruct(int hSocket, void* Struct, int Size)
{
	if (send(hSocket, Struct, Size, 0) == -1)
		close(hSocket);
		//Erreur de recv sur le socket
}

void RecevoirMessage(int hSocket, char *Message)
{
	int TailleMsgRecu = 0, nbreBytesRecus;
	int LongueurStr;
	char *Ptr, *LongueurMsg;
	char str[TAILLE_MAX_TRAME] = "", str2[TAILLE_MAX_TRAME] = "", str3[TAILLE_MAX_TRAME] = "";
	
	do
	{
		if ((nbreBytesRecus = recv(hSocket, str, TAILLE_MAX_TRAME, 0)) == -1) {
			printf("Erreur pour le recv du socket !\n");
			printf("--- Errno %d : %s ---\n", errno, strerror(errno));
			close(hSocket);
			exit(1);
		}
		else {
			if (TailleMsgRecu == 0) {
				if (nbreBytesRecus == 0) {	
					printf("Connexion fermée\n");	
					return;	
					//Erreur de recv sur le socket
					//close(hSocket);
				}
				else {
					strcpy(str2, str);
					LongueurMsg = strtok_r(str2, SEPARATOR, &Ptr);

					LongueurStr = atoi(LongueurMsg) + strlen(LongueurMsg) + 1;
				}
			}

			TailleMsgRecu += nbreBytesRecus;
			strcat(str3, str);	
		}
	} while(nbreBytesRecus != 0 && nbreBytesRecus != -1 && TailleMsgRecu < LongueurStr);

	if (nbreBytesRecus != 0 && nbreBytesRecus != -1)
		strncpy(Message, &str3[1 + strlen(LongueurMsg)], atoi(LongueurMsg));
}

void RecevoirStruct(int hSocket, void* Struct, int Size) {
	int TailleMesgRecu = 0, nbreBytesRecus;
	int Sortie = 0;

	do
	{
		if ((nbreBytesRecus = recv(hSocket, Struct, Size, 0)) == -1) {
			printf("Erreur pour le recv du socket !\n");
			printf("--- Errno %d : %s ---\n", errno, strerror(errno));
			close(hSocket);
			exit(1);
		}

		if (TailleMesgRecu == 0 && nbreBytesRecus == 0){
			Sortie = 1;
		}

		TailleMesgRecu += nbreBytesRecus;

		Struct += TailleMesgRecu;
		
	} while(Sortie != 1 && TailleMesgRecu < Size);
}

void FermerConnexion(int hSocket)
{
	close(hSocket); /* Fermeture de la socket */
	printf("Socket client fermee\n");
}

void ConnexionClient(int *hSocket, struct hostent **infosHost, struct in_addr *adresseIPClient, char *IP_Server, struct sockaddr_in *adresseSocketClient, int Port, unsigned int *tailleSockaddr_in)
{
	CreerSocket(hSocket);
	getInfosHost(infosHost, adresseIPClient, IP_Server);
	PreparerSockAddr_In(adresseSocketClient, *infosHost, Port);
	OuvrirConnexion(tailleSockaddr_in, *hSocket, adresseSocketClient);
}

void ConnexionServeur(int *hSocketEcoute, struct hostent **infosHost, struct in_addr *adresseIPClient, char *IP_Server, struct sockaddr_in *adresseSocketClient, int Port)
{
	CreerSocket(hSocketEcoute);
	getInfosHost(infosHost, adresseIPClient, IP_Server);
	PreparerSockAddr_In(adresseSocketClient, *infosHost, Port);
	BindSocket(*hSocketEcoute, adresseSocketClient);
}
