#ifndef APPLICATION_CIACHAT_PROTOCOLEIACOP_H
#define APPLICATION_CIACHAT_PROTOCOLEIACOP_H

#include "SocketUDP.h"

#define POST_QUESTION "Q"
#define ANSWER_QUESTION "RQ"
#define POST_EVENT "I"

void EnvoyerQuestion(char* Message);
void EnvoyerReponse(char* Message);
void EnvoyerInformation(char* Message);
void EnvoyerMessage(char* Message);

char* LireRequete();

void InitSockets(int *hSocketRecv, int *hSocketSend, struct hostent **infosHost, struct in_addr *adresseIP, struct sockaddr_in *adresseSocket, unsigned int *tailleSockaddr_in, int Port);

#endif //APPLICATION_CIACHAT_PROTOCOLEIACOP_H
