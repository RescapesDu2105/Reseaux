//
// Created by Doublon on 19/12/2017.
//

#include <unistd.h>
#include "SocketUdp.h"
using namespace std;

/*************CONSTRUCTOR*************/
SocketUdp::SocketUdp(int hsocket , hostent **infohost)
{
    /* 1. Creation de la socket */
    setHSocket(::socket(AF_INET, SOCK_STREAM, 0));
    if(getHSocket() == -1)
        printf("Erreur lors de la création de la socket !");

    /* 2. Acquisition des informations sur l'ordinateur local */
    setInfosHost(*infohost);
    if ( (*infohost = gethostbyname("234.5.5.9"))==0)
    {
        printf("Erreur d'acquisition d'infos sur le host %d\n", errno);
        exit(1);
    }
    else
        printf("Acquisition infos host OK\n");
    //setAdresseIPUdp();
    //memcpy( &adresseIPUdp, getInfosHost()->h_addr,getInfosHost()->h_length);
    memcpy( &adresseIPUdp, (*infohost)->h_addr,(*infohost)->h_length);

    setTailleSockaddr_in(sizeof(struct sockaddr_in));

    /* 3. Preparation de la structure sockaddr_in */
    setAdresseSocketUdp();
    memset(&adresseSocketUdp, 0, getTailleSockaddr_in());
    adresseSocketUdp.sin_family = AF_INET;
    adresseSocketUdp.sin_port = htons(PORTUPD);
    memcpy(&adresseSocketUdp.sin_addr, infosHost->h_addr,infosHost->h_length);

    /* 4. Le systeme prend connaissance de l'adresse et du port de la socket */
    if (bind(hSocket, (struct sockaddr *)&adresseSocketUdp,
             sizeof(struct sockaddr_in)) == -1)
    {
        printf("Erreur sur le bind de la socket %d\n", errno);
        exit(1);
    }
    else printf("Bind adresse et port socket OK\n");
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

void SocketUdp::setAdresseSocketUdp() {
    memset(&adresseSocketUdp, 0, getTailleSockaddr_in());
    adresseSocketUdp.sin_family = AF_INET;
    adresseSocketUdp.sin_port = htons(PORTUPD);
    memcpy(&adresseSocketUdp.sin_addr, getInfosHost()->h_addr,getInfosHost()->h_length);
}

hostent *SocketUdp::getInfosHost() const {
    return infosHost;
}

void SocketUdp::setInfosHost(hostent *infosHost) {
    SocketUdp::infosHost = infosHost;
}

const in_addr &SocketUdp::getAdresseIPUdp() const {
    return adresseIPUdp;
}

void SocketUdp::setAdresseIPUdp() {
    memcpy( &adresseIPUdp, getInfosHost()->h_addr,getInfosHost()->h_length);
}

unsigned int SocketUdp::getTailleSockaddr_in() const {
    return tailleSockaddr_in;
}

void SocketUdp::setTailleSockaddr_in(unsigned int tailleSockaddr_in) {
    SocketUdp::tailleSockaddr_in = tailleSockaddr_in;
}

/*************METHODE*************/
void SocketUdp::EnvoyerMessageUDP() {
    /* 4. Envoi d'un message */
    char msgEnvoie[MAXSTRING];
    printf("Message a envoyer au groupe : ");
    gets(msgEnvoie);
    if (sendto(hSocket, msgEnvoie, MAXSTRING, 0, (struct sockaddr *)&adresseSocketUdp, getTailleSockaddr_in()) == -1)
    {
        printf("Erreur sur le send de la socket %d\n", errno);
        close(hSocket); /* Fermeture de la socket */
        exit(1);
    }
    else printf("Send socket OK\n");
}

void SocketUdp::RecevoirMessageUPD() {
    /* 5.Reception d'un message*/
    char msgRecu[MAXSTRING];
    int nbreRecv, cpt=0 ,tailleSocksddr_in = sizeof(struct sockaddr_in);
    if ((nbreRecv = recvfrom(hSocket, msgRecu, MAXSTRING, 0, (struct sockaddr *&)adresseSocketUdp,&tailleSocksddr_in)) == -1)
    {
        printf("Erreur sur le recvfrom de la socket %d\n", errno);
        close(hSocket); /* Fermeture de la socket */
        exit(1);
    }
    msgRecu[nbreRecv+1]=0;
    printf("Message recu = %s\n", msgRecu);
    printf("Adresse de l'emetteur = %s\n", inet_ntoa(getAdresseSocketUdp().sin_addr));
}


