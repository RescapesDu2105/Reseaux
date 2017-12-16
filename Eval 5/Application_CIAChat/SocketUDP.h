#ifndef APPLICATION_CIACHAT_SOCKETUDP_H
#define APPLICATION_CIACHAT_SOCKETUDP_H

#include <stdio.h>
#include <stdlib.h> /* pour exit */
#include <string.h> /* pour memcpy */
#include <sys/types.h>
#include <sys/socket.h> /* pour les types de socket */
#include <sys/time.h> /* pour les types de socket */
#include <netdb.h> /* pour la structure hostent */
#include <errno.h>
#include <netinet/in.h> /* pour la conversion adresse reseau->format dot ainsi que le conversion format local/format reseau */
#include <netinet/tcp.h> /* pour la conversion adresse reseau->format dot */
#include <arpa/inet.h> /* pour la conversion adresse reseau->format dot */
//#include <Winsock2.h>
//#include <ws2tcpip.h>

#define MAXSTRING 200

int CreateSocketUDP();
void GetInfosHost(struct hostent ** infosHost, struct in_addr * adresseIP);
void PrepareSockAddrIn(struct sockaddr_in *adresseSocketServeur, unsigned int *tailleSockaddr_in, int Port);
void BindSocket(int hSocket, struct sockaddr_in adresseSocketServeur, unsigned int tailleSockaddr_in);
void SettingUpSocket(int hSocket, struct ip_mreq mreq, struct sockaddr_in adresseSocketServeur, unsigned int tailleSockaddr_in);
void CloseSocketUDP(int hSocket);

void EnvoyerMessageUDP(char* Message, int hSocket, struct sockaddr_in adresseSocketServeur, unsigned int tailleSockaddr_in);
void RecevoirMessageUDP(int hSocket, struct sockaddr_in adresseSocketClient, unsigned int tailleSockaddr_in);



#endif //APPLICATION_CIACHAT_SOCKETUDP_H
