#include "Chat.h"

int main()
{
    int hSocketRecv, hSocketSend; /* Handle de la socket */
    struct hostent ** infosHost;
    struct in_addr adresseIP;
    struct sockaddr_in adresseSocket;
    unsigned int tailleSockaddr_in;
    int Port = 30051; // Temp
    pthread_t ThreadReception = NULL;

    InitSockets(&hSocketRecv, &hSocketSend, infosHost, &adresseIP, &adresseSocket, &tailleSockaddr_in, Port);
    ThreadReception = InitThread();


    return 0;
}

