cmake_minimum_required(VERSION 3.8)
project(C___Chat)

set(CMAKE_CXX_STANDARD 11)

set(SOURCE_FILES Socket.cpp Socket.h)
add_library(C___Chat ${SOURCE_FILES})