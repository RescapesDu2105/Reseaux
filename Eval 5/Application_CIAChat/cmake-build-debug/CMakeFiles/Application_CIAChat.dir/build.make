# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.9

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list


# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /cygdrive/c/Users/Doublon/.CLion2017.3/system/cygwin_cmake/bin/cmake.exe

# The command to remove a file.
RM = /cygdrive/c/Users/Doublon/.CLion2017.3/system/cygwin_cmake/bin/cmake.exe -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = "/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat"

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = "/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat/cmake-build-debug"

# Include any dependencies generated for this target.
include CMakeFiles/Application_CIAChat.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/Application_CIAChat.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/Application_CIAChat.dir/flags.make

CMakeFiles/Application_CIAChat.dir/Chat.c.o: CMakeFiles/Application_CIAChat.dir/flags.make
CMakeFiles/Application_CIAChat.dir/Chat.c.o: ../Chat.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir="/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat/cmake-build-debug/CMakeFiles" --progress-num=$(CMAKE_PROGRESS_1) "Building C object CMakeFiles/Application_CIAChat.dir/Chat.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/Application_CIAChat.dir/Chat.c.o   -c "/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat/Chat.c"

CMakeFiles/Application_CIAChat.dir/Chat.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/Application_CIAChat.dir/Chat.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E "/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat/Chat.c" > CMakeFiles/Application_CIAChat.dir/Chat.c.i

CMakeFiles/Application_CIAChat.dir/Chat.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/Application_CIAChat.dir/Chat.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S "/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat/Chat.c" -o CMakeFiles/Application_CIAChat.dir/Chat.c.s

CMakeFiles/Application_CIAChat.dir/Chat.c.o.requires:

.PHONY : CMakeFiles/Application_CIAChat.dir/Chat.c.o.requires

CMakeFiles/Application_CIAChat.dir/Chat.c.o.provides: CMakeFiles/Application_CIAChat.dir/Chat.c.o.requires
	$(MAKE) -f CMakeFiles/Application_CIAChat.dir/build.make CMakeFiles/Application_CIAChat.dir/Chat.c.o.provides.build
.PHONY : CMakeFiles/Application_CIAChat.dir/Chat.c.o.provides

CMakeFiles/Application_CIAChat.dir/Chat.c.o.provides.build: CMakeFiles/Application_CIAChat.dir/Chat.c.o


CMakeFiles/Application_CIAChat.dir/ProtocoleIACOP.c.o: CMakeFiles/Application_CIAChat.dir/flags.make
CMakeFiles/Application_CIAChat.dir/ProtocoleIACOP.c.o: ../ProtocoleIACOP.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir="/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat/cmake-build-debug/CMakeFiles" --progress-num=$(CMAKE_PROGRESS_2) "Building C object CMakeFiles/Application_CIAChat.dir/ProtocoleIACOP.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/Application_CIAChat.dir/ProtocoleIACOP.c.o   -c "/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat/ProtocoleIACOP.c"

CMakeFiles/Application_CIAChat.dir/ProtocoleIACOP.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/Application_CIAChat.dir/ProtocoleIACOP.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E "/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat/ProtocoleIACOP.c" > CMakeFiles/Application_CIAChat.dir/ProtocoleIACOP.c.i

CMakeFiles/Application_CIAChat.dir/ProtocoleIACOP.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/Application_CIAChat.dir/ProtocoleIACOP.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S "/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat/ProtocoleIACOP.c" -o CMakeFiles/Application_CIAChat.dir/ProtocoleIACOP.c.s

CMakeFiles/Application_CIAChat.dir/ProtocoleIACOP.c.o.requires:

.PHONY : CMakeFiles/Application_CIAChat.dir/ProtocoleIACOP.c.o.requires

CMakeFiles/Application_CIAChat.dir/ProtocoleIACOP.c.o.provides: CMakeFiles/Application_CIAChat.dir/ProtocoleIACOP.c.o.requires
	$(MAKE) -f CMakeFiles/Application_CIAChat.dir/build.make CMakeFiles/Application_CIAChat.dir/ProtocoleIACOP.c.o.provides.build
.PHONY : CMakeFiles/Application_CIAChat.dir/ProtocoleIACOP.c.o.provides

CMakeFiles/Application_CIAChat.dir/ProtocoleIACOP.c.o.provides.build: CMakeFiles/Application_CIAChat.dir/ProtocoleIACOP.c.o


CMakeFiles/Application_CIAChat.dir/SocketUDP.c.o: CMakeFiles/Application_CIAChat.dir/flags.make
CMakeFiles/Application_CIAChat.dir/SocketUDP.c.o: ../SocketUDP.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir="/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat/cmake-build-debug/CMakeFiles" --progress-num=$(CMAKE_PROGRESS_3) "Building C object CMakeFiles/Application_CIAChat.dir/SocketUDP.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/Application_CIAChat.dir/SocketUDP.c.o   -c "/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat/SocketUDP.c"

CMakeFiles/Application_CIAChat.dir/SocketUDP.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/Application_CIAChat.dir/SocketUDP.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E "/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat/SocketUDP.c" > CMakeFiles/Application_CIAChat.dir/SocketUDP.c.i

CMakeFiles/Application_CIAChat.dir/SocketUDP.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/Application_CIAChat.dir/SocketUDP.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S "/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat/SocketUDP.c" -o CMakeFiles/Application_CIAChat.dir/SocketUDP.c.s

CMakeFiles/Application_CIAChat.dir/SocketUDP.c.o.requires:

.PHONY : CMakeFiles/Application_CIAChat.dir/SocketUDP.c.o.requires

CMakeFiles/Application_CIAChat.dir/SocketUDP.c.o.provides: CMakeFiles/Application_CIAChat.dir/SocketUDP.c.o.requires
	$(MAKE) -f CMakeFiles/Application_CIAChat.dir/build.make CMakeFiles/Application_CIAChat.dir/SocketUDP.c.o.provides.build
.PHONY : CMakeFiles/Application_CIAChat.dir/SocketUDP.c.o.provides

CMakeFiles/Application_CIAChat.dir/SocketUDP.c.o.provides.build: CMakeFiles/Application_CIAChat.dir/SocketUDP.c.o


CMakeFiles/Application_CIAChat.dir/ThreadReception.c.o: CMakeFiles/Application_CIAChat.dir/flags.make
CMakeFiles/Application_CIAChat.dir/ThreadReception.c.o: ../ThreadReception.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir="/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat/cmake-build-debug/CMakeFiles" --progress-num=$(CMAKE_PROGRESS_4) "Building C object CMakeFiles/Application_CIAChat.dir/ThreadReception.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/Application_CIAChat.dir/ThreadReception.c.o   -c "/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat/ThreadReception.c"

CMakeFiles/Application_CIAChat.dir/ThreadReception.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/Application_CIAChat.dir/ThreadReception.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E "/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat/ThreadReception.c" > CMakeFiles/Application_CIAChat.dir/ThreadReception.c.i

CMakeFiles/Application_CIAChat.dir/ThreadReception.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/Application_CIAChat.dir/ThreadReception.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S "/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat/ThreadReception.c" -o CMakeFiles/Application_CIAChat.dir/ThreadReception.c.s

CMakeFiles/Application_CIAChat.dir/ThreadReception.c.o.requires:

.PHONY : CMakeFiles/Application_CIAChat.dir/ThreadReception.c.o.requires

CMakeFiles/Application_CIAChat.dir/ThreadReception.c.o.provides: CMakeFiles/Application_CIAChat.dir/ThreadReception.c.o.requires
	$(MAKE) -f CMakeFiles/Application_CIAChat.dir/build.make CMakeFiles/Application_CIAChat.dir/ThreadReception.c.o.provides.build
.PHONY : CMakeFiles/Application_CIAChat.dir/ThreadReception.c.o.provides

CMakeFiles/Application_CIAChat.dir/ThreadReception.c.o.provides.build: CMakeFiles/Application_CIAChat.dir/ThreadReception.c.o


# Object files for target Application_CIAChat
Application_CIAChat_OBJECTS = \
"CMakeFiles/Application_CIAChat.dir/Chat.c.o" \
"CMakeFiles/Application_CIAChat.dir/ProtocoleIACOP.c.o" \
"CMakeFiles/Application_CIAChat.dir/SocketUDP.c.o" \
"CMakeFiles/Application_CIAChat.dir/ThreadReception.c.o"

# External object files for target Application_CIAChat
Application_CIAChat_EXTERNAL_OBJECTS =

Application_CIAChat.exe: CMakeFiles/Application_CIAChat.dir/Chat.c.o
Application_CIAChat.exe: CMakeFiles/Application_CIAChat.dir/ProtocoleIACOP.c.o
Application_CIAChat.exe: CMakeFiles/Application_CIAChat.dir/SocketUDP.c.o
Application_CIAChat.exe: CMakeFiles/Application_CIAChat.dir/ThreadReception.c.o
Application_CIAChat.exe: CMakeFiles/Application_CIAChat.dir/build.make
Application_CIAChat.exe: CMakeFiles/Application_CIAChat.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir="/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat/cmake-build-debug/CMakeFiles" --progress-num=$(CMAKE_PROGRESS_5) "Linking C executable Application_CIAChat.exe"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/Application_CIAChat.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/Application_CIAChat.dir/build: Application_CIAChat.exe

.PHONY : CMakeFiles/Application_CIAChat.dir/build

CMakeFiles/Application_CIAChat.dir/requires: CMakeFiles/Application_CIAChat.dir/Chat.c.o.requires
CMakeFiles/Application_CIAChat.dir/requires: CMakeFiles/Application_CIAChat.dir/ProtocoleIACOP.c.o.requires
CMakeFiles/Application_CIAChat.dir/requires: CMakeFiles/Application_CIAChat.dir/SocketUDP.c.o.requires
CMakeFiles/Application_CIAChat.dir/requires: CMakeFiles/Application_CIAChat.dir/ThreadReception.c.o.requires

.PHONY : CMakeFiles/Application_CIAChat.dir/requires

CMakeFiles/Application_CIAChat.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/Application_CIAChat.dir/cmake_clean.cmake
.PHONY : CMakeFiles/Application_CIAChat.dir/clean

CMakeFiles/Application_CIAChat.dir/depend:
	cd "/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat/cmake-build-debug" && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" "/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat" "/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat" "/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat/cmake-build-debug" "/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat/cmake-build-debug" "/cygdrive/d/GitHub/Reseaux/Eval 5/Application_CIAChat/cmake-build-debug/CMakeFiles/Application_CIAChat.dir/DependInfo.cmake" --color=$(COLOR)
.PHONY : CMakeFiles/Application_CIAChat.dir/depend

