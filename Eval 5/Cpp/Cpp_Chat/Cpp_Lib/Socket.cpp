//
// Created by Doublon on 19/12/2017.
//

#include "Socket.h"

using namespace std;

Socket::Socket(string host, int port, bool isIP)
{
    socketHandle = ::socket(AF_INET, SOCK_STREAM, 0);

    if(socketHandle == -1)
        throw ErrnoException(errno, "Erreur de creation de la socket");

    memset(&socketAddress, 0, sizeof(sockaddr_in));

    socketAddress.sin_family = AF_INET;
    socketAddress.sin_port = htons(port);

    if(isIP)
    {
        if(inet_pton(AF_INET, host.c_str(), &(socketAddress.sin_addr)) == 0)
            throw ErrnoException(errno, "Erreur d'acquisition d'infos sur l'host");
    }
    else
    {
        struct hostent *infosHost;

        if((infosHost = gethostbyname(host.c_str())) == 0)
            throw ErrnoException(errno, "Erreur d'acquisition d'infos sur l'host");

        memcpy(&socketAddress.sin_addr, infosHost->h_addr, infosHost->h_length);
    }
}

Socket::Socket(const Socket& s)
{
    memcpy(&socketAddress, &s.socketAddress, sizeof(struct sockaddr_in));
    socketHandle = s.socketHandle;
}

Socket::~Socket()
{
    if(socketHandle != -1)
        ::close(socketHandle);
}

/* method */
void Socket::sendString(string message)
{
    string messageSocket;

    messageSocket = Convert::intToString(message.size()) + SEPARATOR + message;

    if(send(socketHandle, messageSocket.c_str(), CHUNK_SIZE, 0) == -1)
        throw ErrnoException(errno, "Erreur sur le send de la socket");
}

void Socket::sendStruct(void* stru, int size)
{
    if(send(socketHandle, stru, size, 0) == -1)
        throw ErrnoException(errno, "Erreur sur le send de la socket");
}

void Socket::receiveStruct(void* stru, int size)
{
    //int totalReceivedLen = 0;
    // int receivedLen;

    if(recv(socketHandle, stru, size, 0) == -1)
        throw ErrnoException(errno, "Erreur sur la rcev de la socket");
}

string Socket::receiveString()
{
    string str;
    char *messageLen;
    int stringLen;
    int receivedLen;
    int totalReceivedLen = 0;

    char buff[CHUNK_SIZE], buff2[CHUNK_SIZE];

    do
    {
        if((receivedLen = recv(socketHandle, buff, CHUNK_SIZE, 0)) == -1)
            throw ErrnoException(errno, "Erreur sur la rcev de la socket");

        if(totalReceivedLen == 0)
        {
            if(receivedLen == 0)
            {
                throw ErrnoException(errno, "Le client est deconnecte");
            }

            strcpy(buff2, buff);
            messageLen = strtok(buff2, SEPARATOR);

            stringLen = atoi(messageLen) + strlen(messageLen) + 1;
        }

        totalReceivedLen += receivedLen;
        str += buff;

    } while(totalReceivedLen < stringLen);

    return str.substr(strlen(messageLen) + 1, atoi(messageLen));
}


void Socket::shutdown()
{
    if(socketHandle != -1)
    {
        ::shutdown(socketHandle, SHUT_RDWR);
        socketHandle = -1;
    }
}

void Socket::close()
{
    if(socketHandle != -1)
    {
        ::close(socketHandle);
        socketHandle = -1;
    }
}

/* getter */
int Socket::getSocketHandle() const
{
    return socketHandle;
}

/* setter */
void Socket::setSocketHandle(int h)
{
    socketHandle = h;
}
