package Connection;

import java.sql.*;

public class DBConnector {
    protected static final Connection connection = connect();

    public static Connection connect() {
        try {
            String url = "jdbc:mysql://localhost:3306/bankingapp";
            String user = "root";
            String password = "";
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.err.println("Conexiunea la baza de date nu s-a putut realiza: " + e.getMessage());
            return null;
        }
    }
}
