#include <iostream>
#include "Chat.h"
#include "ThreadReception.h"

using namespace std;
int main()
{

    bool envoie_msg =true;
    string adressedip=string("234.5.5.9");
    int port = 30051;
    SocketUdp socketUDP=SocketUdp((char *)"234.5.5.9", 30051);

    //THREAD
    ThreadReception t_reception= ThreadReception(adressedip, port);
    t_reception.Start();
    cout << "\033[2J\033[1;1H";

    char msgEnvoie[MAXSTRING];
    while(envoie_msg)
    {
        cout << "message a envoyé : "<<endl;
        cin>>msgEnvoie;

        if(strcasecmp(msgEnvoie,"0")==0)
            envoie_msg = false;
        else
            socketUDP.EnvoyerMessageUDP(msgEnvoie);


    }
    cout<<"fermeture"<<endl;
    socketUDP.~SocketUdp();
    t_reception.~ThreadReception();
    return 0;
}