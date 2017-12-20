#include <stdio.h>
#include <stdlib.h> /* pour exit */
#include <string.h> /* pour memcpy */
#include <sys/types.h>
#include <sys/socket.h> /* pour les types de socket */
#include <sys/time.h> /* pour les types de socket */
#include <netdb.h> /* pour la structure hostent */
#include <errno.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <arpa/inet.h>
#include <unistd.h>
#define PORT_MULTI 30051 /* Port de la socket serveur */
#define MAXSTRING 100 /* Longueur des messages */
int main()
{
    int hSocket; /* Handle de la socket */
    struct hostent * infosHost;
    struct in_addr adresseIP;
    struct sockaddr_in adresseSocketServeur,adresseSocketClient;
    unsigned int tailleSockaddr_in;
    char msg[MAXSTRING];
    int nbreRecv, cpt=0;
    struct ip_mreq mreq;
    int flagReuse = 1;

    /* 1. Creation de la socket */
    hSocket = socket(AF_INET, SOCK_DGRAM, 0);
    if (hSocket == -1)
    {
        printf("Erreur de creation de la socket %d\n", errno); exit(1);
    }
    else printf("Creation de la socket OK\n");

    /* 2. Acquisition des informations sur l'ordinateur local */
    if ( (infosHost = gethostbyname("234.5.5.9"))==0)
    {
        printf("Erreur d'acquisition d'infos sur le host %d\n", errno); exit(1);
    }
    else printf("Acquisition infos host OK\n");
    memcpy(&adresseIP, infosHost->h_addr, infosHost->h_length);
    printf("Adresse IP host = %s\n",inet_ntoa(adresseIP));

    /* 3. Preparation de la structure sockaddr_in du serveur */
    tailleSockaddr_in = sizeof(struct sockaddr_in);
    memset(&adresseSocketServeur, 0, tailleSockaddr_in);
    adresseSocketServeur.sin_family = AF_INET;
    adresseSocketServeur.sin_port = htons(PORT_MULTI);
    adresseSocketServeur.sin_addr.s_addr = inet_addr("234.5.5.9");
    puts("adresseSocket prete");

    /* 4. Le systeme prend connaissance de l'adresse et du port de la socket */
    if (bind(hSocket, (struct sockaddr *)&adresseSocketServeur,
             tailleSockaddr_in) == -1)
    {
        printf("Erreur sur le bind de la socket %d\n", errno); exit(1);
    }
    else printf("Bind adresse et port socket OK\n");

    /* 5. Parametrage de la socket */
    memcpy(&mreq.imr_multiaddr, &adresseSocketServeur.sin_addr,
           tailleSockaddr_in);
    mreq.imr_interface.s_addr = htonl(INADDR_ANY);
    printf("Utilisation de l'adresse %s\n", inet_ntoa(mreq.imr_interface));
    setsockopt (hSocket, IPPROTO_IP, IP_ADD_MEMBERSHIP, &mreq,sizeof(mreq));
    //unsigned char ttl=2;
    //setsockopt (hSocket, IPPROTO_IP, IP_MULTICAST_TTL, &ttl,sizeof(ttl));

    puts("apres setsockopt");
    do
    {
        /* 6.Reception d'un message serveur */
        memset(msg, 0, MAXSTRING);
        if ((nbreRecv = recvfrom(hSocket, msg, MAXSTRING, 0,(struct sockaddr *)&adresseSocketClient,&tailleSockaddr_in)) == -1)
        {
            printf("Erreur sur le recvfrom de la socket %d\n", errno);
            close(hSocket); /* Fermeture de la socket */
            exit(1);
        }
        else
        {
            printf("Recvfrom socket OK\n");
        }
        msg[nbreRecv+1]=0;
        printf("Message recu = %s\n", msg);
        printf("Adresse de l'emetteur = %s\n", inet_ntoa(adresseSocketClient.sin_addr));
    }
    while (strcmp(msg, "Arret du chat !"));

    /* 9. Fermeture de la socket */
    close(hSocket); /* Fermeture de la socket */
    printf("Socket client fermee\n");
    return 0;
}
