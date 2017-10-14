#ifndef NET_H_INCLUDED
#define NET_H_INCLUDED

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h> /* pour les types de socket */
#include <netdb.h> /* pour la structure hostent */
#include <errno.h>
#include <netinet/in.h> /* pour la conversion adresse reseau->format dot
ainsi que le conversion format local/format reseau */
#include <netinet/tcp.h> /* pour la conversion adresse reseau->format dot */
#include <arpa/inet.h> /* pour la conversion adresse reseau->format dot */

#define TAILLE_MAX_TRAME 512

#define EOC "END_OF_CONNEXION"
#define DOC "DENY_OF_CONNEXION"

#define SEPARATOR "$"
// test

void CreerSocket(int *hSocket);
void getInfosHost(struct hostent **, struct in_addr *, char *IP);
void PreparerSockAddr_In(struct sockaddr_in *, struct hostent *, int Port);
void BindSocket(int, struct sockaddr_in *);
void EcouterConnexion(int hSocketEcoute);
void AccepterConnexion (int *, int *, int, struct sockaddr_in *);
void OuvrirConnexion (unsigned int *, int, struct sockaddr_in *);
void FermerConnexion(int hSocket);

void EnvoyerMessage(int hSocket, char *Msg);
void EnvoyerStruct(int hSocket, void* Struct, int Size);
void RecevoirMessage(int hSocket, char *Message);
void RecevoirStruct(int hSocket, void* Struct, int Size);

void ConnexionClient(int *hSocket, struct hostent **infosHost, struct in_addr *adresseIPClient, char *IP_Server, struct sockaddr_in *adresseSocketClient, int Port, unsigned int *tailleSockaddr_in);
void ConnexionServeur(int *hSocketEcoute, struct hostent **infosHost, struct in_addr *adresseIPClient, char *IP_Server, struct sockaddr_in *adresseSocketClient, int Port);

#endif
