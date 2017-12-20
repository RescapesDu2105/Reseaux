<<<<<<< HEAD
# Install script for directory: /cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat
=======
# Install script for directory: /cygdrive/e/Dropbox/B3/Reseaux/2017-2018/Reseaux/Eval 5/Application_CIAChat
>>>>>>> a26cc08d89866971e9ea2cee850836ca76361d74

# Set the install prefix
if(NOT DEFINED CMAKE_INSTALL_PREFIX)
  set(CMAKE_INSTALL_PREFIX "/usr/local")
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
file(WRITE "/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat/cmake-build-debug/${CMAKE_INSTALL_MANIFEST}"
=======
file(WRITE "/cygdrive/e/Dropbox/B3/Reseaux/2017-2018/Reseaux/Eval 5/Application_CIAChat/cmake-build-debug/${CMAKE_INSTALL_MANIFEST}"
>>>>>>> a26cc08d89866971e9ea2cee850836ca76361d74
     "${CMAKE_INSTALL_MANIFEST_CONTENT}")
