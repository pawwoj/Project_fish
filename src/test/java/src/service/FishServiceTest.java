package src.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class FishServiceTest {

    @Test
    @DisplayName("Add fish - database contain fish")
    void shouldSayThatDatabaseContainAddedFish() {
        FishService fishService = new FishService();
        fishService.getConnectionDB().setDatabaseName("fishproject_test");
        String fishNameGiven = "fishTest1";
        String fishTypeGiven = "typeTest1";
        int fishPriceGiven = 21;
        String aquariumNameGiven = "m10";
        String data = fishNameGiven + "\r\n" + fishTypeGiven + "\r\n" + fishPriceGiven + "\r\n" + aquariumNameGiven;
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        String resultString = "";
        int resultInt = 0;
        //When
        fishService.addFish();
        //Then
        try {
            ResultSet resultSet = fishService.connectionDB.getStatement()
                    .executeQuery("SELECT * FROM fishproject_test.fish WHERE name='" + fishNameGiven + "';");
            while (resultSet.next()) {
                resultString = resultSet.getString(2);
                resultInt = resultSet.getInt(5);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        assertThat(fishNameGiven).isEqualTo(resultString);
        assertThat(resultInt).isGreaterThan(0);
        // After - delete tested record
        try {
            fishService.connectionDB.getStatement()
                    .execute("DELETE FROM fish WHERE name = '" + fishNameGiven + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Add fish - Catch AquariumFullException")
    void shouldCatchAquariumFullException() {
        String databaseName = "fishproject_test";
        String databaseURL = "jdbc:mysql://localhost:3306/";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.getConnection(databaseURL + databaseName, "root", "dupa")
                    .createStatement().execute("INSERT INTO aquarium() VALUES (1000, 'Atest1' ,1);");
            DriverManager.getConnection(databaseURL + databaseName, "root", "dupa")
                    .createStatement().execute("insert into fish() values (500, 'TestX', 'zloty', 23, 1000);");
        } catch (Exception e) {
            System.out.println(e);
        }
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        FishService fishService = new FishService();
        fishService.getConnectionDB().setDatabaseName("fishproject_test");
        String fishNameGiven = "fishTest1";
        String fishTypeGiven = "typeTest1";
        int fishPriceGiven = 21;
        String aquariumNameGiven = "Atest1";
        String data = fishNameGiven + "\r\n" + fishTypeGiven + "\r\n" + fishPriceGiven + "\r\n" + aquariumNameGiven;
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        //When
        // && dataValidate.isNameValidate(aquariumName)) {
        // Aquarium aquarium = aquariumService.getAquarium(aquariumName);
        // getAquarium wewnątrz addFish odwołuje się do bazy produkcyjnej
        fishService.addFish();
        //Then
        assertThat("AquariumFullException")
                .isSubstringOf(outContent.toString());
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.getConnection(databaseURL + databaseName, "root", "dupa")
                    .createStatement().execute("DELETE FROM aquarium WHERE name = '" + "t1" + "';");
            DriverManager.getConnection(databaseURL + databaseName, "root", "dupa")
                    .createStatement().execute("DELETE FROM fish WHERE aquarium_id = 1000;");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    void printAllFishFromAquarium() {
    }

    @Test
    void moveFish() {
    }

    @Test
    void getFish() {
    }

    @Test
    void updateAquariumForFish() {
    }
}