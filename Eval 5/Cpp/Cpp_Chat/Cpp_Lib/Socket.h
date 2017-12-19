//
// Created by Doublon on 19/12/2017.
//

#ifndef C_CHAT_SOCKET_H
#define C_CHAT_SOCKET_H

#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <string>
#include <iostream>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <errno.h>
#include <sstream>

#define CHUNK_SIZE 500
#define SEPARATOR "#"

class Socket
{
    protected:
        struct sockaddr_in socketAddress;
        int socketHandle;
    public:
        Socket(std::string host, int port, bool isIP);
        Socket(const Socket& s);
        ~Socket();

        /* method */
        void sendString(std::string message);
        void sendStruct(void* stru, int size);

        void receiveStruct(void* stru, int size);
        std::string receiveString();

        void shutdown();
        void close();

        /* getter */
        int getSocketHandle() const;

        /* setter */
        void setSocketHandle(int h);
};

#endif //C_CHAT_SOCKET_H
