//
// Created by Doublon on 21/12/2017.
//

#include"ThreadReception.h"

/************************CONSTRUCTOR***********************/
ThreadReception::ThreadReception(string adresseip , int port)
{
    cout<<"constructeur du thread !"<<endl;
    setIp(adresseip);
    setPort(port);
}

ThreadReception::~ThreadReception()
{
    stop_thread = true;
    /*if(thread_reception.joinable())
        thread_reception.join();*/
    terminate();
}

ThreadReception::ThreadReception(const ThreadReception & t_threadreception) {
    string adressedip=string("234.5.5.9");
    setIp(adressedip);
    setPort(30051);
}

/***********************GETTER/SETTER***********************/
const string &ThreadReception::getIp() const {
    return ip;
}

void ThreadReception::setIp(const string &ip) {
    ThreadReception::ip = ip;
}

int ThreadReception::getPort() const {
    return port;
}

void ThreadReception::setPort(int port) {
    ThreadReception::port = port;
}

/*************************METHODS***************************/


void ThreadReception::ThreadMain()
{
    sd = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
    if(sd == -1)
    {
        cout << "Erreur socket thread reception " << endl;
        exit(0);
    }

    if(setsockopt(sd, SOL_SOCKET, SO_REUSEADDR, (char *)&reuse, sizeof(reuse)) < 0)
    {
        perror("Setting SO_REUSEADDR error");
        exit(1);
    }

    memset(&localSock, 0, sizeof(struct sockaddr_in));
    localSock.sin_family = AF_INET;
    localSock.sin_port = htons(port);
    localSock.sin_addr.s_addr = htonl(INADDR_ANY);

/* 4. Le systeme prend connaissance de l'adresse et du port de la socket */
    if(bind(sd, (struct sockaddr*)&localSock, sizeof(struct sockaddr_in)) == -1)
    {
        cout << "erreur bind " << errno << endl;
        exit(0);
    }

    imr.imr_multiaddr.s_addr = inet_addr(getIp().c_str());
    imr.imr_interface.s_addr = htonl(INADDR_ANY);

    setsockopt(sd , IPPROTO_IP, IP_ADD_MEMBERSHIP, &imr, sizeof(imr));
    printf("Bind adresse et port socket OK\n");

    cout<<"port recu pour sd: "<<port<<endl;
    cout<<"port enregistrer dans la sockaddr_in de sd: "<<localSock.sin_port<<endl;

    RecevoirMessageUDP();
    cout<<"fermeture du thread"<<endl;
/*****************FIN TEST****************************/
}

void ThreadReception::RecevoirMessageUDP()
{
    char msgRecu[1000];
    int nbreRecv;
    cout << "\033[2J\033[1;1H";

    while(!isStop_thread() && -1 != recvfrom(sd, msgRecu, 1000, 0, (struct sockaddr*)&localSock, (socklen_t *) &tailleSocksddr_in))
    {
        cout <<"j'attend un message..."<<endl;
        printf("Message recu = %s\n", msgRecu);
        memset((char *) msgRecu, '\0', sizeof(msgRecu));
    }
}

void ThreadReception::Start()
{
    cout<<"lancement du thread !"<<endl;
    thread_reception = thread(&ThreadReception::ThreadMain,this);
}

bool ThreadReception::isStop_thread() const {
    return stop_thread;
}

void ThreadReception::setStop_thread(bool stop_thread) {
    ThreadReception::stop_thread = stop_thread;
}


