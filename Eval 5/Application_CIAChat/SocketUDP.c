#include "SocketUDP.h"

int CreateSocketUDP()
{
    int hSocket;

    hSocket = socket(AF_INET, SOCK_DGRAM, 0);
    if (hSocket == -1)
    {
        printf("Erreur de creation de la socket %d\n", errno);
        exit(1);
    }
    else
        printf("Creation de la socket OK\n");

    return hSocket;
}

void GetInfosHost(struct hostent ** infosHost, struct in_addr * adresseIP)
{
    if ( (*infosHost = gethostbyname("localhost"))==0)
    {
        printf("Erreur d'acquisition d'infos sur le host %d\n", errno);
        exit(1);
    }
    else
        printf("Acquisition infos host OK\n");

    memcpy(&adresseIP, (*infosHost)->h_addr, (*infosHost)->h_length);
    printf("Adresse IP host = %s\n", inet_ntoa(*adresseIP));
}

void PrepareSockAddrIn(struct sockaddr_in *adresseSocket, unsigned int *tailleSockaddr_in, int Port)
{
    *tailleSockaddr_in = sizeof(struct sockaddr_in);
    memset(adresseSocket, 0, *tailleSockaddr_in);
    adresseSocket->sin_family = AF_INET;
    adresseSocket->sin_port = htons(Port);
    adresseSocket->sin_addr.s_addr = inet_addr("234.5.5.9");
    puts("adresseSocket prete");
}

void BindSocket(int hSocket, struct sockaddr_in adresseSocket, unsigned int tailleSockaddr_in)
{
    if (bind(hSocket, (struct sockaddr *)&adresseSocket, tailleSockaddr_in) == -1)
    {
        printf("Erreur sur le bind de la socket %d\n", errno);
        exit(1);
    }
    else printf("Bind adresse et port socket OK\n");
}

void SettingUpSocket(int hSocket, struct sockaddr_in adresseSocket, unsigned int tailleSockaddr_in)
{
    memcpy(&mreq.imr_multiaddr, &adresseSocket.sin_addr, tailleSockaddr_in);
    mreq.imr_interface.s_addr = htonl(INADDR_ANY);
    printf("Utilisation de l'adresse %s\n", inet_ntoa(mreq.imr_interface));
    setsockopt (hSocket, IPPROTO_IP, IP_ADD_MEMBERSHIP, &mreq, sizeof(mreq));
}

void EnvoyerMessageUDP(char* Message, int hSocket, struct sockaddr_in adresseSocket, unsigned int tailleSockaddr_in)
{
    if (sendto(hSocket, Message, MAXSTRING, 0, &adresseSocket, tailleSockaddr_in) == -1)
    {
        printf("Erreur sur le sendto de la socket %d\n", errno);
        CloseSocketUDP(hSocket);
        exit(1);
    }
    else
    {
        printf("Sendto socket OK\n");
    }
}

void RecevoirMessageUDP(int hSocket, struct sockaddr_in adresseSocket, unsigned int tailleSockaddr_in)
{
    char msg[MAXSTRING];
    int nbreRecv, cpt=0;

    memset(msg, 0, MAXSTRING);
    if ((nbreRecv = recvfrom(hSocket, msg, MAXSTRING, 0, (struct sockaddr *)&adresseSocket,&tailleSockaddr_in)) == -1)
    {
        printf("Erreur sur le recvfrom de la socket %d\n", errno);
        CloseSocketUDP(hSocket);
        exit(1);
    }
    else
    {
        printf("Recvfrom socket OK\n");
    }
    msg[nbreRecv+1]=0;
    printf("Message recu = %s\n", msg);
    printf("Adresse de l'emetteur = %s\n", inet_ntoa(adresseSocket.sin_addr));
}

void CloseSocketUDP(int hSocket)
{
    close(hSocket); /* Fermeture de la socket */
    printf("Socket client fermee\n");
}