//
// Created by Doublon on 19/12/2017.
//

#include "SocketUdp.h"

/*************CONSTRUCTOR*************/
SocketUdp::SocketUdp(char *adresseip, int port)
{
    CreerSocket();
    FindInfosHost(adresseip);
    PreparerSockAddr_In(port);
    BindSocket(adresseip);
    cout << "Fin de l'initialisation de la Socket UDP !" << endl;
}

SocketUdp::SocketUdp(const SocketUdp& s)
{
    memcpy(&adresseSocketUdp, &s.adresseSocketUdp, sizeof(struct sockaddr_in));
    setHSocket(s.hSocket);
}

SocketUdp::~SocketUdp() {
    if(hSocket != -1)
        close(hSocket);
}

/*************GETTER/SETTER*************/
int SocketUdp::getHSocket() const {
    return hSocket;
}

void SocketUdp::setHSocket(int hSocket) {
    SocketUdp::hSocket = hSocket;
}

const sockaddr_in &SocketUdp::getAdresseSocketUdp() const {
    return adresseSocketUdp;
}

void SocketUdp::setAdresseSocketUdp()
{
    memset(&adresseSocketUdp, 0, getTailleSockaddr_in());
    adresseSocketUdp.sin_family = AF_INET;
    adresseSocketUdp.sin_port = htons(PORTUPD);
    memcpy(&adresseSocketUdp.sin_addr, infosHost->h_addr,infosHost->h_length);
}


const in_addr &SocketUdp::getAdresseIPUdp() const {
    return adresseIPUdp;
}

void SocketUdp::setAdresseIPUdp() {
    memcpy( &adresseIPUdp, infosHost->h_addr,infosHost->h_length);
}

unsigned int SocketUdp::getTailleSockaddr_in() const {
    return tailleSockaddr_in;
}

void SocketUdp::setTailleSockaddr_in(unsigned int tailleSockaddr_in) {
    SocketUdp::tailleSockaddr_in = tailleSockaddr_in;
}

/*************METHODE*************/
void SocketUdp::EnvoyerMessageUDP(char * msgEnvoie) {
    /* 4. Envoi d'un message */
    if (sendto(hSocket, msgEnvoie, MAXSTRING, 0, (struct sockaddr *)&adresseSocketUdp,sizeof(sockaddr_in)) == -1)
    {
        printf("Erreur sur le send de la socket %d\n", errno);
        close(hSocket); /* Fermeture de la socket */
        exit(1);
    }
    else printf("Send socket OK\n");
}

void SocketUdp::RecevoirMessageUDP() {
    /* 5.Reception d'un message*/
    char msgRecu[MAXSTRING];
    int nbreRecv, cpt=0 ,tailleSocksddr_in = sizeof(struct sockaddr_in);
    if ((nbreRecv = recvfrom(getHSocket(), msgRecu, MAXSTRING, 0, (struct sockaddr *)&adresseSocketUdp,(socklen_t *) &tailleSocksddr_in)) == -1)
    {
        printf("Erreur sur le recvfrom de la socket %d\n", errno);
        close(hSocket); /* Fermeture de la socket */
        exit(1);
    }
    msgRecu[nbreRecv+1]=0;
    printf("Message recu = %s\n", msgRecu);
}

void SocketUdp::CreerSocket()
{
    /* 1. Creation de la socket */
    //setHSocket(socket(AF_INET, SOCK_DGRAM, 0));
    hSocket=socket(AF_INET, SOCK_DGRAM, 0);
    if(getHSocket() == -1)
    {
        printf("Erreur lors de la création de la socket !");
        exit(1);
    }
}

void SocketUdp::FindInfosHost(char* adresseip)
{
    /* 2. Acquisition des informations sur l'ordinateur local */
    if ( (infosHost = gethostbyname(adresseip))==0)
    {
        printf("Erreur d'acquisition d'infos sur le host %d\n", errno);
        exit(1);
    }
    printf("Acquisition infos host OK\n");
    memcpy( &adresseIPUdp, infosHost->h_addr,infosHost->h_length);
    printf("Adresse IP = %s\n",inet_ntoa(adresseIPUdp));
}

void SocketUdp::PreparerSockAddr_In(int port)
{
    //setsockopt(hSocket, SOL_SOCKET, SO_REUSEPORT, (char *)&reuse, sizeof(reuse));

    /******************DEBUT TEST******************************/
    int reuse = 1;
    if(setsockopt(hSocket, SOL_SOCKET, SO_REUSEADDR, (char *)&reuse, sizeof(reuse)) < 0) {
        perror("Setting SO_REUSEADDR error");
        close(getHSocket());
        exit(1);
    }
    memset(&adresseSocketUdp, 0, sizeof(struct sockaddr_in));
    adresseSocketUdp.sin_family = AF_INET;
    adresseSocketUdp.sin_port = htons(port);
    adresseSocketUdp.sin_addr.s_addr = htonl(INADDR_ANY);
    /*****************FIN TEST****************************/
}

void SocketUdp::BindSocket(char* adresseip)
{


    /* 4. Le systeme prend connaissance de l'adresse et du port de la socket */
    if (bind(hSocket, (struct sockaddr *)&adresseSocketUdp, sizeof(sockaddr_in)) < 0)
    {
        printf("Erreur sur le bind de la socket %d\n", errno);
        exit(1);
    }

    struct ip_mreq imr;
    imr.imr_multiaddr.s_addr = inet_addr(adresseip);
    imr.imr_interface.s_addr = inet_addr("255.255.255.255");

    setsockopt(hSocket , IPPROTO_IP, IP_ADD_MEMBERSHIP, &imr, sizeof(imr));
    printf("Bind adresse et port socket OK\n");
    printf("port : %d\n", adresseSocketUdp.sin_port);
}



