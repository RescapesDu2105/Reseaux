cmake_minimum_required(VERSION 3.9)
project(Application_CIAChat C)

set(CMAKE_C_STANDARD 11)

add_executable(Application_CIAChat Chat.h Chat.c ProtocoleIACOP.h ProtocoleIACOP.c SocketUDP.h SocketUDP.c ThreadReception.h ThreadReception.c)