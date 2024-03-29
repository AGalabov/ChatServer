# ChatServer
FMI "Modern Java Technologies" 2017 - Course Project

## Overview
Console chat server

## Functionality
* The server handles multiple user's requests simultaneously.
* Each user has unique username and password. User's data is stored as a plain text in a file.
* Users can disconnect at any time.
* Each user can send personal messages or files to another active user. A user is active when is registered and logged in the system at the moment.
* Users can communicate using chat-rooms(multiple users chat):
    * A user can be part of many chat-rooms at a time. 
    * Create/Delete chat-room. Only the creator of the room can delete it.
    * Join/Leave an existing chat-room.
    * Show all existing chat-rooms.
    * List all active users in an existing room.
    * Send a message to all members in the room.
    * Chat history of each room is stored as a plain text in a file. When a new user joins the room he receives the whole chat history of the room. 
  
#### User commands:
- `connect <host> <port>` - connects a user to the server
- `register <username> <password>` - registers a user if the username is already taken, server sends appropriate message.
- `login <username> <password>` - logs in a user if exists and is not already logged in.
- `disconnect` - disconnects a user.
- `list-users` - lists all active users.
- `send <username> <message>` - sends `<message>` to the user with username equal to `<username>` if such exists.
- `send-file <username> <file>` - sends `<file>` to the user with username equal to `<username>` if such exists.
- `accept` - accepts a sent file and prommpts question for directory to be saved.
- `decline` - rejects a sent file.
- `create/delete/join/leave-room <room_name>` - creates/deletes/joins/leaves chat-room if does not exists the server sends appropriate message.
- `send-group <group-name> <message>` - sends `<message>` to all users in group with name `<group-name>` if such exists.
- `list-rooms` - lists all active chat-rooms. A chat-room is active if has at least one active user.
- `list-room-users <room>` - lists all active users in the chat-room.
