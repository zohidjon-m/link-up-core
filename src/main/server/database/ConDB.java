package main.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConDB {
    Connection con;
    private final String url = "jdbc:mysql://localhost:3306/linkup_db";
    private final String user = "root";
    private final String password = "M@xsmith2020";


    public String getUrl(){
        return url;
    }
    public String getUser(){
        return user;
    }
    public String getPassword(){
        return password;
    }

    public Connection connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = getUrl();
            String user = getUser();
            String password = getPassword();
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return con;

    }
}
