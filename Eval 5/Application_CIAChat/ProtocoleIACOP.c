#include "ProtocoleIACOP.h"

void InitSockets(int *hSocketRecv, int *hSocketSend, struct hostent **infosHost, struct in_addr *adresseIP, struct sockaddr_in *adresseSocket, unsigned int *tailleSockaddr_in, int Port)
{
    GetInfosHost(infosHost, adresseIP);
    PrepareSockAddrIn(adresseSocket, tailleSockaddr_in, Port);

    // Socket de réception
    *hSocketRecv = CreateSocketUDP();
    BindSocket(*hSocketRecv, *adresseSocket, *tailleSockaddr_in);
    SettingUpSocket(*hSocketRecv, *adresseSocket, *tailleSockaddr_in);

    // Socket d'envoi
    *hSocketSend = CreateSocketUDP();
    BindSocket(*hSocketSend, *adresseSocket, *tailleSockaddr_in);
    SettingUpSocket(*hSocketSend, *adresseSocket, *tailleSockaddr_in);
}
