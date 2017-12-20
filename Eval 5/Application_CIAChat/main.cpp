#include <iostream>
#include "Chat.h"

int main()
{
    string user;
    Chat chat = Chat();

    // Saisit du nom d'user
    cout << "Entrez le nom d'user : ";
    cin >> user;

    chat.setUser(user);


    std::cout << "Hello, World!" << std::endl;
    return 0;
}