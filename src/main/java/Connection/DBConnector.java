package Connection;

import java.sql.*;

public class DBConnector {
    private static final String url = "jdbc:mysql://sql7.freesqldatabase.com:3306/sql7781927";
    private static final String user = "sql7781927";
    private static final String password = "JMQmV7HCyN";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.err.println("Conexiunea la baza de date nu s-a putut realiza: " + e.getMessage());
            return null;
        }
    }
}
