//
// Created by Philippe on 20/12/2017.
//

#include "Chat.h"

Chat::Chat()
{

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

const void Chat::ActiverEcho(termios oldt) const
{
    // REACTIVE L'AFFICHAGE DANS LE TERMINAL //
    tcsetattr(STDIN_FILENO, TCSANOW, &oldt);
    // ************************************* //
}

const termios Chat::DesactiverEcho(termios oldt) const
{
    // DESACTIVE L'AFFICHAGE DANS LE TERMINAL //
    termios oldt;
    tcgetattr(STDIN_FILENO, &oldt);
    termios newt = oldt;
    newt.c_lflag &= ~ECHO;
    tcsetattr(STDIN_FILENO, TCSANOW, &newt);
    // ************************************** //

    return oldt;
}

void Chat::sendMessage()
{

}



