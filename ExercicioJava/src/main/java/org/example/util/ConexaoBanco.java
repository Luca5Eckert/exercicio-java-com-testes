package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {
    public final static String URL = "jdbc:mysql://localhost:3306/att?useSSL=false&serverTimezone=UTC";
    public final static String USER = "root";
    public final static String PASSWORD = "";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) throws SQLException {
        conectar();
    }
}
