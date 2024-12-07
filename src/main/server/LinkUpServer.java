package main.server;

import java.io.*;
import java.net.*;
import java.sql.*;

public class LinkUpServer {
    private static final int PORT = 4242;
    protected static Connection connection;

    public static void main(String[] args) {
        // Connect to MySQL database
        connectToDatabase();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/linkup_db";
            String user = "root";
            String password = "M@xsmith2020";
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                // Read request type (e.g., REGISTER or LOGIN)
                String requestType = in.readLine();
                String username = in.readLine();
                String password = in.readLine();

                if ("REGISTER".equalsIgnoreCase(requestType)) {
                    UserAuth auth = new UserAuth();
                    if (auth.registerUser(username, password)) {
                        out.println("REGISTER_SUCCESS");
                    } else {
                        out.println("REGISTER_FAIL");
                    }
                } else if ("LOGIN".equalsIgnoreCase(requestType)) {
                    UserAuth auth = new UserAuth();
                    if (auth.authenticateUser(username, password)) {
                        out.println("LOGIN_SUCCESS");
                    } else {
                        out.println("LOGIN_FAIL");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
