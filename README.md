# Real-Time Desktop Chatting Application (link-up-core)

## Overview
This project is a real-time desktop chatting application developed entirely in Java.  
It features a Java Swing GUI for the client side and a multithreaded server for handling simultaneous user connections over TCP sockets.

The system is built from scratch without using external networking frameworks like HTTP or WebSocket libraries — demonstrating custom protocol design over raw TCP.

## Key Features
- Real-time, bi-directional chat between multiple users.
- Custom communication protocols over TCP sockets.
- Multithreaded server architecture — each client connection handled in a separate thread.
- Desktop client built with Java Swing.
- MySQL integration for user data and chat persistence.

## Technologies Used
- Java SE
- Java Swing (GUI)
- TCP Socket Programming (Custom Protocols)
- Multithreading
- MySQL Database

  ## How to Run
1. Configure MySQL database connection settings in server code.
2. Start the server by running `LinkUpServer.java` (Server mode).
3. Launch the client application by running `LinkUpClient.java`.
4. Connect clients to server using server IP address and designated port.

## Future Improvements
- User authentication (Login/Register).
- Group chats and private chatrooms.
- Offline messaging and message queueing.
- UI/UX redesign with modern styling.

## Author
Mahmudjonov Zohidjon  
(Seoul, South Korea)
