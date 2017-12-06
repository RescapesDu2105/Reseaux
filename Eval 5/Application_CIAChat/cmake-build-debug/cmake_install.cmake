<<<<<<< HEAD
# Install script for directory: E:/Dropbox/B3/Réseaux/2017-2018/Reseaux/Eval 5/Application_CIAChat
=======
# Install script for directory: E:/Dropbox/B3/Reseaux/2017-2018/Reseaux/Eval 5/Application_CIAChat
>>>>>>> 9e9fe1426f9f6ea8246851cdaecd403903fff0e5

# Set the install prefix
if(NOT DEFINED CMAKE_INSTALL_PREFIX)
  set(CMAKE_INSTALL_PREFIX "C:/Program Files (x86)/Application_CIAChat")
endif()
string(REGEX REPLACE "/$" "" CMAKE_INSTALL_PREFIX "${CMAKE_INSTALL_PREFIX}")

# Set the install configuration name.
if(NOT DEFINED CMAKE_INSTALL_CONFIG_NAME)
  if(BUILD_TYPE)
    string(REGEX REPLACE "^[^A-Za-z0-9_]+" ""
           CMAKE_INSTALL_CONFIG_NAME "${BUILD_TYPE}")
  else()
    set(CMAKE_INSTALL_CONFIG_NAME "Debug")
  endif()
  message(STATUS "Install configuration: \"${CMAKE_INSTALL_CONFIG_NAME}\"")
endif()

# Set the component getting installed.
if(NOT CMAKE_INSTALL_COMPONENT)
  if(COMPONENT)
    message(STATUS "Install component: \"${COMPONENT}\"")
    set(CMAKE_INSTALL_COMPONENT "${COMPONENT}")
  else()
    set(CMAKE_INSTALL_COMPONENT)
  endif()
endif()

if(CMAKE_INSTALL_COMPONENT)
  set(CMAKE_INSTALL_MANIFEST "install_manifest_${CMAKE_INSTALL_COMPONENT}.txt")
else()
  set(CMAKE_INSTALL_MANIFEST "install_manifest.txt")
endif()

string(REPLACE ";" "\n" CMAKE_INSTALL_MANIFEST_CONTENT
       "${CMAKE_INSTALL_MANIFEST_FILES}")
<<<<<<< HEAD
file(WRITE "E:/Dropbox/B3/Réseaux/2017-2018/Reseaux/Eval 5/Application_CIAChat/cmake-build-debug/${CMAKE_INSTALL_MANIFEST}"
=======
file(WRITE "E:/Dropbox/B3/Reseaux/2017-2018/Reseaux/Eval 5/Application_CIAChat/cmake-build-debug/${CMAKE_INSTALL_MANIFEST}"
>>>>>>> 9e9fe1426f9f6ea8246851cdaecd403903fff0e5
     "${CMAKE_INSTALL_MANIFEST_CONTENT}")
