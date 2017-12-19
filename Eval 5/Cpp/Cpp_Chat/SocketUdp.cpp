//
// Created by Doublon on 19/12/2017.
//

#include "SocketUdp.h"
using namespace std;

SocketUdp::SocketUdp(int hsocket , hostent **infohost)
{
    /* 1. Creation de la socket */
    setHSocket(::socket(AF_INET, SOCK_STREAM, 0));
    if(getHSocket() == -1)
        printf("Erreur lors de la création de la socket !");

    /* 2. Acquisition des informations sur l'ordinateur local */
    setInfosHost(*infohost);
    if ( (*infohost = gethostbyname("localhost"))==0)
    {
        printf("Erreur d'acquisition d'infos sur le host %d\n", errno);
        exit(1);
    }
    else
        printf("Acquisition infos host OK\n");
    //setAdresseIPUdp();
    //memcpy( &adresseIPUdp, getInfosHost()->h_addr,getInfosHost()->h_length);
    memcpy( &adresseIPUdp, (*infohost)->h_addr,(*infohost)->h_length);
}

int SocketUdp::getHSocket() const {
    return hSocket;
}

void SocketUdp::setHSocket(int hSocket) {
    SocketUdp::hSocket = hSocket;
}

const sockaddr_in &SocketUdp::getAdresseSocketUdp() const {
    return adresseSocketUdp;
}

void SocketUdp::setAdresseSocketUdp(const sockaddr_in &adresseSocketUdp) {
    SocketUdp::adresseSocketUdp = adresseSocketUdp;
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
