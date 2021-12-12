package src.connection;

import lombok.Setter;

import java.sql.*;

@Setter
public class ConnectionDB {
    private String databaseName = "fishproject";

    public Connection getConnectionDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/" + databaseName, "root", "dupa");
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public Statement getStatement() {
        try {
            return getConnectionDB().createStatement();
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }
}