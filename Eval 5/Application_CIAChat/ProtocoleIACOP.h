#ifndef APPLICATION_CIACHAT_PROTOCOLEIACOP_H
#define APPLICATION_CIACHAT_PROTOCOLEIACOP_H

#include "SocketUDP.h"

void EnvoyerQuestion(char* Message);
void EnvoyerReponse(char* Message);
void EnvoyerInformation(char* Message);
void EnvoyerMessage(char* Message);

char* LireRequete();

#endif //APPLICATION_CIACHAT_PROTOCOLEIACOP_H
