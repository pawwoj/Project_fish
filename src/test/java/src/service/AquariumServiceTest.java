package src.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import src.model.Aquarium;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class AquariumServiceTest {

    @Test
    @DisplayName("Add aquarium - database contain aquarium")
    void addAquariumTest() {
        // Given
        AquariumService aquariumService = new AquariumService();
        aquariumService.getConnectionDB().setDatabaseName("fishproject_test");
        String givenName = "test11";
        String givenCapacity = "4";
        String data = givenName + "\r\n" + givenCapacity + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        String result = "";
        int capacity = 0;
        // When
        aquariumService.addAquarium();
        // Then
        try {
            ResultSet resultSet = aquariumService.connectionDB.getStatement()
                    .executeQuery("SELECT * FROM fishproject_test.aquarium WHERE name='" + givenName + "';");
            while (resultSet.next()) {
                result = resultSet.getString(2);
                capacity = resultSet.getInt(3);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertThat(result).isEqualTo(givenName);
        assertThat(capacity + "").isEqualTo(givenCapacity);
        // After - delete tested record
        try {
            aquariumService.connectionDB.getStatement()
                    .execute("DELETE FROM aquarium WHERE name = '" + givenName + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Add aquarium - catch WrongDataFormatException when aquarium name = '  '")
    void addAquariumWrongDataFormatException() {
        // Given
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        AquariumService aquariumService = new AquariumService();
        aquariumService.getConnectionDB().setDatabaseName("fishproject_test");
        String givenName = " ";
        String givenCapacity = "4";
        String data = givenName + "\r\n" + givenCapacity + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        // When
        aquariumService.addAquarium();
        //Then
        assertThat("WrongDataFormatException")
                .isSubstringOf(outContent.toString());
    }

    @Test
    @DisplayName("Add aquarium - WrongDataFormatException - aquarium name not entered")
    void addAquariumWrongDataFormatException1() {
        // Given
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        AquariumService aquariumService = new AquariumService();
        aquariumService.getConnectionDB().setDatabaseName("fishproject_test");
        String givenCapacity = "4";
        String data = "\r\n" + givenCapacity + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        // When
        aquariumService.addAquarium();
        //Then
        assertThat("WrongDataFormatException")
                .isSubstringOf(outContent.toString());
    }

    @Test
    @DisplayName("Add aquarium - CapacityNumberException - givenCapacity = -2")
    void addAquariumWrongDataFormatException3() {
        // Given
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        AquariumService aquariumService = new AquariumService();
        aquariumService.getConnectionDB().setDatabaseName("fishproject_test");
        String givenName = "test1";
        String givenCapacity = "-4";
        String data = givenName + "\r\n" + givenCapacity + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        // When
        aquariumService.addAquarium();
        //Then
        assertThat("CapacityNumberException")
                .isSubstringOf(outContent.toString());
    }

    @Test
    @DisplayName("Add aquarium - CapacityNumberException - givenCapacity = '' ")
    void addAquariumWrongDataFormatException4() {
        // Given
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        AquariumService aquariumService = new AquariumService();
        aquariumService.getConnectionDB().setDatabaseName("fishproject_test");
        String givenName = "test1";
        String givenCapacity = "";
        String data = givenName + "\r\n" + givenCapacity + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        // When
        aquariumService.addAquarium();
        //Then
        assertThat("NumberFormatException")
                .isSubstringOf(outContent.toString());
    }

    @Test
    @DisplayName("Add aquarium - CapacityNumberException - givenCapacity = 'text' ")
    void addAquariumWrongDataFormatException5() {
        // Given
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        AquariumService aquariumService = new AquariumService();
        aquariumService.getConnectionDB().setDatabaseName("fishproject_test");
        String givenName = "test1";
        String givenCapacity = "@#";
        String data = givenName + "\r\n" + givenCapacity + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        // When
        aquariumService.addAquarium();
        //Then
        assertThat("NumberFormatException")
                .isSubstringOf(outContent.toString());
    }

    @Test
    @DisplayName("Add aquarium - CapacityNumberException - givenCapacity = null ")
    void addAquariumWrongDataFormatException6() {
        // Given
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        AquariumService aquariumService = new AquariumService();
        aquariumService.getConnectionDB().setDatabaseName("fishproject_test");
        String givenName = "test1";
        String data = givenName + "\r\n" + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        // When
        aquariumService.addAquarium();
        //Then
        assertThat("NumberFormatException").isSubstringOf(outContent.toString());
    }

    @Test
    @DisplayName("Add aquarium - SQLIntegrityConstraintViolationException - 2 aquarium with same name ")
    void addAquariumWrongDataFormatException7() {
        // Given
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        AquariumService aquariumService = new AquariumService();
        aquariumService.getConnectionDB().setDatabaseName("fishproject_test");
        String givenName = "test1";
        String givenCapacity = "2";
        String data = givenName + "\r\n" + givenCapacity + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        // When
        aquariumService.addAquarium();
        //  try to addAquarium with same data
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        aquariumService.addAquarium();
        assertThat("SQLIntegrityConstraintViolationException")
                .isSubstringOf(outContent.toString());
        // After - delete tested record
        try {
            aquariumService.connectionDB.getStatement()
                    .execute("DELETE FROM aquarium WHERE name = '" + givenName + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldReturnAquariumFromDatabase() {
        AquariumService aquariumService = new AquariumService();
        String databaseName = "fishproject_test";
        String givenName = "getAquariumTest";
        String givenCapacity = "2";
        String databaseURL = "jdbc:mysql://localhost:3306/";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.getConnection(databaseURL + databaseName, "root", "dupa")
                    .createStatement().execute("INSERT INTO aquarium(name, capacity) VALUES ("
                            + "'" + givenName + "', " + givenCapacity + ");");
        } catch (Exception e) {
            System.out.println(e);
        }
        aquariumService.getConnectionDB().setDatabaseName(databaseName);
        Aquarium newAquarium = aquariumService.getAquarium(givenName);

        assertThat(newAquarium.getId()).isGreaterThan(0);
        assertThat(newAquarium.getName()).isEqualTo(givenName);
        assertThat(newAquarium.getCapacity()).isEqualTo(Integer.parseInt(givenCapacity));

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.getConnection(databaseURL + databaseName, "root", "dupa")
                    .createStatement().execute("DELETE FROM aquarium WHERE name = '" + givenName + "';");
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @Test
    void shouldReturnTrueWhenChceckingIsAquariumFullOnFullAquarium() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Aquarium aquarium = new Aquarium(100, "t1", 1);
        AquariumService aquariumService = new AquariumService();
        String databaseName = "fishproject_test";
        String databaseURL = "jdbc:mysql://localhost:3306/";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.getConnection(databaseURL + databaseName, "root", "dupa")
                    .createStatement().execute("INSERT INTO aquarium() VALUES (100, 't1' ,1);");
            DriverManager.getConnection(databaseURL + databaseName, "root", "dupa")
                    .createStatement().execute("insert into fish() values (50, 'fishTest1', 'zloty', 23, 100);");
        } catch (Exception e) {
            System.out.println(e);
        }

        aquariumService.getConnectionDB().setDatabaseName(databaseName);
        Boolean isAquariumFullAnswer = aquariumService.isAquariumFull(aquarium);

        assertThat(isAquariumFullAnswer).isEqualTo(true);
        assertThat("AquariumFullException")
                .isSubstringOf(outContent.toString());

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.getConnection(databaseURL + databaseName, "root", "dupa")
                    .createStatement().execute("DELETE FROM aquarium WHERE name = '" + "t1" + "';");
            DriverManager.getConnection(databaseURL + databaseName, "root", "dupa")
                    .createStatement().execute("DELETE FROM fish WHERE aquarium_id = 100;");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    void shouldReturnFalseWhenChceckingIsAquariumFullOnNotFullAquarium() {

        Aquarium aquarium = new Aquarium(100, "t1", 1);

        AquariumService aquariumService = new AquariumService();
        String databaseName = "fishproject_test";
        String databaseURL = "jdbc:mysql://localhost:3306/";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.getConnection(databaseURL + databaseName, "root", "dupa")
                    .createStatement().execute("INSERT INTO aquarium() VALUES (100, 't1' ,1);");
        } catch (Exception e) {
            System.out.println(e);
        }
        aquariumService.getConnectionDB().setDatabaseName(databaseName);
        Boolean isAquariumFullAnswer = aquariumService.isAquariumFull(aquarium);

        assertThat(isAquariumFullAnswer).isEqualTo(false);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.getConnection(databaseURL + databaseName, "root", "dupa")
                    .createStatement().execute("DELETE FROM aquarium WHERE name = '" + "t1" + "';");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    void shouldReturnTrueWhenChceckingIsAquariumEmptyOnEmptyAquarium() {
        Aquarium aquarium = new Aquarium(100, "t1", 1);
        AquariumService aquariumService = new AquariumService();
        String databaseName = "fishproject_test";
        String databaseURL = "jdbc:mysql://localhost:3306/";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.getConnection(databaseURL + databaseName, "root", "dupa")
                    .createStatement().execute("INSERT INTO aquarium() VALUES (100, 't1' ,1);");
        } catch (Exception e) {
            System.out.println(e);
        }
        aquariumService.getConnectionDB().setDatabaseName(databaseName);
        Boolean isAquariumEmptyAnswer = aquariumService.isAquariumEmpty(aquarium);

        assertThat(isAquariumEmptyAnswer).isEqualTo(true);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.getConnection(databaseURL + databaseName, "root", "dupa")
                    .createStatement().execute("DELETE FROM aquarium WHERE name = '" + "t1" + "';");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    void shouldReturnFalseWhenChceckingIsAquariumEmptyOnNotEmptyAquarium() {
        Aquarium aquarium = new Aquarium(100, "t1", 1);
        AquariumService aquariumService = new AquariumService();
        String databaseName = "fishproject_test";
        String databaseURL = "jdbc:mysql://localhost:3306/";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.getConnection(databaseURL + databaseName, "root", "dupa")
                    .createStatement().execute("INSERT INTO aquarium() VALUES (100, 't1' ,1);");
            DriverManager.getConnection(databaseURL + databaseName, "root", "dupa")
                    .createStatement().execute("insert into fish() values (50, 'fishTest1', 'zloty', 23, 100);");
        } catch (Exception e) {
            System.out.println(e);
        }
        aquariumService.getConnectionDB().setDatabaseName(databaseName);
        Boolean isAquariumEmptyAnswer = aquariumService.isAquariumEmpty(aquarium);

        assertThat(isAquariumEmptyAnswer).isEqualTo(false);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.getConnection(databaseURL + databaseName, "root", "dupa")
                    .createStatement().execute("DELETE FROM aquarium WHERE name = '" + "t1" + "';");
            DriverManager.getConnection(databaseURL + databaseName, "root", "dupa")
                    .createStatement().execute("DELETE FROM fish WHERE aquarium_id = 100;");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    void ShouldCatchAquariumNotEmptyExceptionWhenTryingToDeleteNotEmptyAquarium() {
        AquariumService aquariumService = new AquariumService();
        String databaseName = "fishproject_test";
        String databaseURL = "jdbc:mysql://localhost:3306/";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.getConnection(databaseURL + databaseName, "root", "dupa")
                    .createStatement().execute("INSERT INTO aquarium() VALUES (100, 't1' ,1);");
            DriverManager.getConnection(databaseURL + databaseName, "root", "dupa")
                    .createStatement().execute("insert into fish() values (50, 'fishTest1', 'zloty', 23, 100);");
        } catch (Exception e) {
            System.out.println(e);
        }
        String data = "t1" + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        aquariumService.getConnectionDB().setDatabaseName(databaseName);
        aquariumService.deleteAquarium();


        assertThat("src.exception.AquariumNotEmptyException")
                .isSubstringOf(outContent.toString());

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.getConnection(databaseURL + databaseName, "root", "dupa")
                    .createStatement().execute("DELETE FROM aquarium WHERE name = '" + "t1" + "';");
            DriverManager.getConnection(databaseURL + databaseName, "root", "dupa")
                    .createStatement().execute("DELETE FROM fish WHERE aquarium_id = 100;");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    void deleteAquariumSuccess() {
        AquariumService aquariumService = new AquariumService();
        String databaseName = "fishproject_test";
        String databaseURL = "jdbc:mysql://localhost:3306/";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.getConnection(databaseURL + databaseName, "root", "dupa")
                    .createStatement().execute("INSERT INTO aquarium() VALUES (100, 't1' ,1);");
        } catch (Exception e) {
            System.out.println(e);
        }

        String data = "t1" + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        aquariumService.getConnectionDB().setDatabaseName(databaseName);
        aquariumService.deleteAquarium();

        assertThat("Aquarium deleted - Success")
                .isSubstringOf(outContent.toString());
    }
}