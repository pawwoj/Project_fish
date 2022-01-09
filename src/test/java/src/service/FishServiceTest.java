package src.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import src.connection.ConnectionDB;
import src.model.Fish;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

class FishServiceTest {

    @BeforeAll
    public static void setUp() {
        String setUpQuery = """
                INSERT INTO aquarium VALUES(501, 'aquarium1', 4);
                INSERT INTO fish VALUES(1502, 'fish2', 'typ1' , 12, 501);
                INSERT INTO fish VALUES(1503, 'fish3', 'typ1' , 12, 501);
                INSERT INTO aquarium VALUES(502, 'aquarium2', 1);
                INSERT INTO fish VALUES(1501, 'fish1', 'typ1' , 12, 502);
                INSERT INTO aquarium VALUES(503, 'aquarium3', 1);
                INSERT INTO aquarium VALUES(504, 'aquarium4', 1);
                """;
        try (Statement stmt = ConnectionDB.getStatement("TEST")) {
            stmt.execute(setUpQuery);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @AfterAll
    public static void dumpDatabase() {
        try (Statement stmt = ConnectionDB.getStatement("TEST")) {
            stmt.execute("TRUNCATE table aquarium");
        } catch (Exception e) {
            System.out.println(e);

        }
        try (Statement stmt = ConnectionDB.getStatement("TEST")) {
            stmt.execute("TRUNCATE table fish");
        } catch (Exception e) {
            System.out.println(e);

        }
    }

    @Test
    @DisplayName("Add fish - database contain fish")
    void shouldSayThatDatabaseContainAddedFish() {
        FishService fishService = new FishService("TEST");
        String fishNameGiven = "addFishTest";
        String data = fishNameGiven + "\r\n" + "type" + "\r\n" + 21 + "\r\n" + "aquarium1";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        String resultString = "";
        //When
        fishService.addFish();
        //Then
        try (ResultSet rs = ConnectionDB.getStatement("TEST")
                .executeQuery("SELECT * FROM fishproject_test.fish WHERE name='"
                        + fishNameGiven + "';")) {
            while (rs.next()) {
                resultString = rs.getString(2);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        assertThat(fishNameGiven).isEqualTo(resultString);
    }

    @Test
    @DisplayName("Add fish - Catch AquariumFullException")
    void shouldCatchAquariumFullException() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        FishService fishService = new FishService("TEST");
        String data = "aquFullEx" + "\r\n" + "typeTest1" + "\r\n" + 21 + "\r\n" + "aquarium2";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        //When
        fishService.addFish();
        //Then
        assertThat("AquariumFullException")
                .isSubstringOf(outContent.toString());
    }

    @Test
    void moveFishTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        FishService fishService = new FishService("TEST");
        String data = "fish3" + "\r\n" + "aquarium3";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        fishService.moveFish();
        //Then
        assertThat("Fish: fish3 - moved to Aquarium: aquarium3")
                .isSubstringOf(outContent.toString());
    }

    @Test
    void getFish() {
        Fish fishG = new Fish(1503, "fish3", "typ1", 12);

        FishService fishService = new FishService("TEST");
        Fish fishR = fishService.getFish("fish3");
        //Then
        assertThat(fishG.toString()).isEqualTo(fishR.toString());
    }
}