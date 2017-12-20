cmake_minimum_required(VERSION 3.9)
project(Application_CIAChat)

set(CMAKE_CXX_STANDARD 11)

add_executable(Application_CIAChat main.cpp SocketUdp.h SocketUdp.cpp Chat.cpp Chat.h)