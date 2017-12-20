//
// Created by Doublon on 19/12/2017.
//

#ifndef CPP_CHAT_SOCKETUDP_H
#define CPP_CHAT_SOCKETUDP_H

#include <cstdio>
#include <cstdlib> /* pour exit */
#include <cstring> /* pour memcpy */
#include <string>
#include <sys/types.h>
#include <sys/socket.h> /* pour les types de socket */
#include <sys/time.h> /* pour les types de socket */
#include <netdb.h> /* pour la structure hostent */
#include <cerrno>
#include <netinet/in.h> /* pour la conversion adresse reseau->format dot ainsi que le conversion format local/format reseau */
#include <netinet/tcp.h> /* pour la conversion adresse reseau->format dot */
#include <arpa/inet.h> /* pour la conversion adresse reseau->format dot */
#include <unistd.h>
#include <iostream>

#define SEPARATOR "#"
#define ADRESSEIP "234.5.5.9"
#define PORTUPD 30051

#define MAXSTRING 200
using namespace std;

class SocketUdp
{
    private :
        int hSocket; /* Handle de la socket */
        hostent *infosHost;
        struct in_addr adresseIPUdp;
        struct sockaddr_in adresseSocketUdp;
        unsigned int tailleSockaddr_in;

    public :
        /*************CONSTRUCTOR*************/
        SocketUdp(char *adresseip , int port );
        SocketUdp(const SocketUdp& s);
        ~SocketUdp();


        /*************METHODE*************/
        void EnvoyerMessageUDP(char *msgEnvoie);
        void RecevoirMessageUDP();

        void CreerSocket();
        void FindInfosHost(char* adresseip);
        void PreparerSockAddr_In(int port);
        void BindSocket(char* adresseip);

    /*************GETTER/SETTER*************/
        int getHSocket() const;
        void setHSocket(int hSocket);

        const sockaddr_in &getAdresseSocketUdp() const;
        void setAdresseSocketUdp();

    
        const in_addr &getAdresseIPUdp() const;
        void setAdresseIPUdp();

        unsigned int getTailleSockaddr_in() const;
        void setTailleSockaddr_in(unsigned int tailleSockaddr_in);
};

#endif //CPP_CHAT_SOCKETUDP_H
