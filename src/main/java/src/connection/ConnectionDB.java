package src.connection;

import lombok.Setter;

import java.sql.*;

@Setter
public class ConnectionDB {
    private static String dbURL = "jdbc:mysql://localhost:3306/";
    private static String dbName = "fishproject";
    private static String testDbName = "fishproject_test";
    private static String dbMultiQuery = "?allowMultiQueries=true";
    private static String user = "root";
    private static String password = "dupa";

    public static Connection getConnectionDB(String env) {
        try {
            if(env.equals("PROD")) {
                return DriverManager
                        .getConnection(dbURL + dbName + dbMultiQuery
                                , user, password);
            }
            else if(env.equals("TEST"))
                return DriverManager
                        .getConnection(dbURL + testDbName + dbMultiQuery
                                , user, password);
            else return null;
        } catch (NullPointerException e) {
            System.out.println(e);
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
    public static Statement getStatement() {
        try {
            return getConnectionDB("PROD").createStatement();
        } catch (NullPointerException e) {
            System.out.println(e);
            return null;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    public static Statement getStatement(String env) {
        try {
            return getConnectionDB(env).createStatement();
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }
//    public static Statement getStatementForTestDb() {
//        try {
//            return getConnectionDB("TEST").createStatement();
//        } catch (SQLException e) {
//            System.out.println(e);
//            return null;
//        }
//    }
}