#include "Chat.h"

int main()
{
    int hSocketRecv, hSocketSend; /* Handle de la socket */
<<<<<<< HEAD
    struct hostent ** infosHost;
=======
    struct hostent * infosHost;
>>>>>>> 9e9fe1426f9f6ea8246851cdaecd403903fff0e5
    struct in_addr adresseIP;
    struct sockaddr_in adresseSocket;
    unsigned int tailleSockaddr_in;
    int Port = 30051; // Temp
<<<<<<< HEAD
    pthread_t ThreadReception = NULL;

    InitSockets(&hSocketRecv, &hSocketSend, infosHost, &adresseIP, &adresseSocket, &tailleSockaddr_in, Port);
    ThreadReception = InitThread();


    return 0;
}

=======

    GetInfosHost(&infosHost, &adresseIP);
    PrepareSockAddrIn(&adresseSocket, &tailleSockaddr_in, Port);

    // Socket de réception
    hSocketRecv = CreateSocketUDP();
    BindSocket(hSocketRecv, adresseSocket, tailleSockaddr_in);
    SettingUpSocket(hSocketRecv, adresseSocket, tailleSockaddr_in);

    // Socket d'envoi
    hSocketSend = CreateSocketUDP();
    BindSocket(hSocketSend, adresseSocket, tailleSockaddr_in);
    SettingUpSocket(hSocketSend, adresseSocket, tailleSockaddr_in);

    return 0;
}
>>>>>>> 9e9fe1426f9f6ea8246851cdaecd403903fff0e5
