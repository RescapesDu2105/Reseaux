//
// Created by Doublon on 21/12/2017.
//

#ifndef APPLICATION_CIACHAT_THREADRECEPTION_H
#define APPLICATION_CIACHAT_THREADRECEPTION_H

#include <stdio.h>
#include <iostream>
#include <thread>
#include <string>
#include <sys/socket.h>
#include <errno.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <cstdlib>
#include <cstring>
#include <unistd.h>
#include <sstream>

using namespace std;
class ThreadReception
{
    private:
        thread thread_reception;
        bool stop_thread = false;
        string ip;
        int port;
        int tailleSocksddr_in = sizeof(struct sockaddr_in);

        struct sockaddr_in localSock;
        struct ip_mreq imr;
        int sd ,reuse = 1;

    public:
        /************************CONSTRUCTOR***********************/
        ThreadReception(string adresseip , int port);
        ThreadReception(const ThreadReception& t_threadreception);
        ~ThreadReception();

        /***********************GETTER/SETTER***********************/
        const string &getIp() const;

        void setIp(const string &ip);

        int getPort() const;

        void setPort(int port);

    bool isStop_thread() const;

    void setStop_thread(bool stop_thread);

    /*************************METHODS***************************/
        void Start();
        void ThreadMain();
        void RecevoirMessageUDP();
};

#endif //APPLICATION_CIACHAT_THREADRECEPTION_H
