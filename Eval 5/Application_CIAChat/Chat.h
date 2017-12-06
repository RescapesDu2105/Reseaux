#ifndef APPLICATION_CIACHAT_CHAT_H
#define APPLICATION_CIACHAT_CHAT_H

#include "ProtocoleIACOP.h"
#include "ThreadReception.h"

struct ListeMessages
{
    char *Tag;
    char *Message;
};
typedef struct ListeMessages ListeMessages;

void ContacterServeur();

char NomPrenomClient[50];

#endif //APPLICATION_CIACHAT_CHAT_H
