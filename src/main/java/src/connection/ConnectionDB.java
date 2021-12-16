package src.connection;

import lombok.Setter;

import java.sql.*;

@Setter
public class ConnectionDB {
    private static String dbURL = "jdbc:mysql://localhost:3306/";
    private static String dbName = "fishproject_test";
    private static String dbMultiQuery = "?allowMultiQueries=true";
    private static String user = "root";
    private static String password = "dupa";

    public static Connection getConnectionDB() {
        try {
            return DriverManager
                    .getConnection(dbURL + dbName + dbMultiQuery
                            , user, password);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static Statement getStatement() {
        try {
            return getConnectionDB().createStatement();
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }
}