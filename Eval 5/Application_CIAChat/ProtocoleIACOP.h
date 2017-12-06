#ifndef APPLICATION_CIACHAT_PROTOCOLEIACOP_H
#define APPLICATION_CIACHAT_PROTOCOLEIACOP_H

#include "SocketUDP.h"

<<<<<<< HEAD
#define POST_QUESTION "Q"
#define ANSWER_QUESTION "RQ"
#define POST_EVENT "I"

=======
>>>>>>> 9e9fe1426f9f6ea8246851cdaecd403903fff0e5
void EnvoyerQuestion(char* Message);
void EnvoyerReponse(char* Message);
void EnvoyerInformation(char* Message);
void EnvoyerMessage(char* Message);

char* LireRequete();

<<<<<<< HEAD
void InitSockets(int *hSocketRecv, int *hSocketSend, struct hostent **infosHost, struct in_addr *adresseIP, struct sockaddr_in *adresseSocket, unsigned int *tailleSockaddr_in, int Port);

=======
>>>>>>> 9e9fe1426f9f6ea8246851cdaecd403903fff0e5
#endif //APPLICATION_CIACHAT_PROTOCOLEIACOP_H
