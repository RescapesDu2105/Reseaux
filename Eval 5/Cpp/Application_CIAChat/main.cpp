#include <iostream>
#include "Chat.h"

int main()
{
    /*cout << "cc";
    string user;
    Chat chat = Chat();

    // Saisit du nom d'user
    cout << "Entrez le nom d'user : ";
    cin >> user;

    chat.setUser(user);
    chat.getSocketUDP().RecevoirMessageUDP();*/

    SocketUdp socketUDP=SocketUdp((char *)"234.5.5.9", 30060);
    /*cout << "message a envoyé : "<<endl;
    char msgEnvoie[MAXSTRING];
    cin>>msgEnvoie;
    socketUDP.EnvoyerMessageUDP(msgEnvoie);
    socketUDP.RecevoirMessageUDP();*/
    std::cout << "Hello, World!" << std::endl;
    return 0;
}