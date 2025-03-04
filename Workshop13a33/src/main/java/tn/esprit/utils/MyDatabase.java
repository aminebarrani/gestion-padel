package tn.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {

    private final String url = "jdbc:mysql://localhost:3306/boutique";
    private final String username = "root";
    private final String pwd = "";

    private Connection con;
    private static MyDatabase instance;

    private MyDatabase() {
        try {
            con = DriverManager.getConnection(url, username, pwd);
            System.out.println("✅ Connected to database!");
        } catch (SQLException e) {
            System.out.println("❌ Error connecting: " + e.getMessage());
        }
    }

    public static MyDatabase getInstance() {
        if (instance == null) {
            instance = new MyDatabase();
        }
        return instance;
    }

    public Connection getCon() {
        ensureConnectionIsOpen();
        return con;
    }

    private void ensureConnectionIsOpen() {
        try {
            if (con == null || con.isClosed() || !isConnectionValid()) {
                System.out.println("🔄 Connection is closed, null, or invalid. Reconnecting...");
                con = DriverManager.getConnection(url, username, pwd);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error reopening connection: " + e.getMessage());
        }
    }

    private boolean isConnectionValid() {
        try {
            return (con != null && con.isValid(2)); // Check if connection is valid within 2 seconds
        } catch (SQLException e) {
            return false;
        }
    }
}
