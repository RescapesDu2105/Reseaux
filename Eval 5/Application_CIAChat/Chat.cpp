//
// Created by Philippe on 20/12/2017.
//

#include "Chat.h"

Chat::Chat()
{
    socketUDP = new SocketUdp((char *)ADRESSEIP, PORTUPD);
}

Chat::~Chat()
= default;

/*Permet de nettoyer l'écran*/
const void Chat::ClearScreen() const
{
    cout << "\033[2J\033[1;1H";
}

/*Permet de mettre le programme en pause*/
const void Chat::Pause() const
{
    fflush(stdin);
    cout << endl << endl << "Appuyer sur Enter pour continuer..." << endl;
    system("read");
}

void Chat::sendMessage()
{

}

const string &Chat::getUser() const
{
    return user;
}

void Chat::setUser(const string &user)
{
    Chat::user = user;
}

SocketUdp *Chat::getSocketUDP() const
{
    return socketUDP;
}

void Chat::setSocketUDP(SocketUdp *socketUDP)
{
    Chat::socketUDP = socketUDP;
}



