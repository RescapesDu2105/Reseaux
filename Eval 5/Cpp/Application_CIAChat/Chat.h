//
// Created by Philippe on 20/12/2017.
//

#ifndef APPLICATION_CIACHAT_CHAT_H
#define APPLICATION_CIACHAT_CHAT_H

#include <cstdio>
#include <cstdlib>
#include <iostream>
#include <fstream>
#include <string>
#include <cstring>
#include <termios.h>
#include "SocketUdp.h"

using namespace std;


class Chat
{
private:
    string user;
    SocketUdp *socketUDP;

public:
    Chat();
    ~Chat();

    const string &getUser() const;
    void setUser(const string &user);

    SocketUdp *getSocketUDP() const;
    void setSocketUDP(SocketUdp *socketUDP);

    const void ClearScreen() const;
    const void Pause() const;

    void sendMessage();
};


#endif //APPLICATION_CIACHAT_CHAT_H
