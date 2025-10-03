package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection instance;
    private static final String URL = "jdbc:mysql://localhost:3306/Systeme_Gestion_Paiements";
    private static final String USER = "root";
    private static final String PASS = "root";

    private DBConnection() {}



    public static Connection getConnection() {
        if(instance!=null)
            return instance;
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASS);
            return connection;
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}