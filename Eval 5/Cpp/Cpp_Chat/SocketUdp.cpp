//
// Created by Doublon on 19/12/2017.
//

#include "SocketUdp.h"
using namespace std;

SocketUdp::SocketUdp(std::string host, int port, bool isIP)
{
    setHSocket(::socket(AF_INET, SOCK_STREAM, 0));
    if(getHSocket() == -1)
        printf("Erreur lors de la création de la socket !");

    memset(&adresseSocketUdp, 0, sizeof(sockaddr_in));

    adresseSocketUdp.sin_family = AF_INET;
    adresseSocketUdp.sin_port = htons(port);
}

int SocketUdp::getHSocket() const {
    return hSocket;
}

void SocketUdp::setHSocket(int hSocket) {
    SocketUdp::hSocket = hSocket;
}
