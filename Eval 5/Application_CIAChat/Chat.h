//
// Created by Philippe on 20/12/2017.
//

#ifndef APPLICATION_CIACHAT_CHAT_H
#define APPLICATION_CIACHAT_CHAT_H

#include <stdio.h>
#include <stdlib.h>
#include <iostream>
#include <fstream>
#include <string>
#include <string.h>
#include <termios.h>
#include "SocketUdp.h"

using namespace std;


class Chat
{
    private:
        string user;
        SocketUdp socketUDP;

    public:
        Chat();
        ~Chat();

        const void ClearScreen() const;
        const void Pause() const;
        const void ActiverEcho(termios oldt) const;
        const termios DesactiverEcho(termios oldt) const;

        void sendMessage();
};


#endif //APPLICATION_CIACHAT_CHAT_H
