package src.service;

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

    @Test
    @DisplayName("Add fish - database contain fish")
    void shouldSayThatDatabaseContainAddedFish() {
        String queryInsert = """
                INSERT INTO aquarium() VALUES (1000, 'Atest1' ,1);
                """;
        try (Statement stmt = ConnectionDB.getStatementForTestDb()) {
            stmt.execute(queryInsert);
        } catch (Exception e) {
            System.out.println(e);
        }
        FishService fishService = new FishService();
        String fishNameGiven = "fishTest1";
        String data = fishNameGiven + "\r\n" + "typeTest1" + "\r\n" + 21 + "\r\n" + "Atest1";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        String resultString = "";
        int resultInt = 0;
        //When
        fishService.addFish();
        //Then
        try (ResultSet rs = ConnectionDB.getStatementForTestDb()
                .executeQuery("SELECT * FROM fishproject_test.fish WHERE name='"
                     + fishNameGiven + "';")) {
            while (rs.next()) {
                resultString = rs.getString(2);
                resultInt = rs.getInt(5);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        assertThat(fishNameGiven).isEqualTo(resultString);
        assertThat(resultInt).isGreaterThan(0);
        // After - delete tested record
        String queryDelete = """
                DELETE FROM aquarium WHERE name = 'Atest1';
                DELETE FROM fish WHERE aquarium_id = 1000; 
                """;
        try (Statement stmt = ConnectionDB.getStatementForTestDb()) {
            stmt.execute(queryDelete);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    @DisplayName("Add fish - Catch AquariumFullException")
    void shouldCatchAquariumFullException() {
        String queryInsert = """
                INSERT INTO aquarium() VALUES (1000, 'Atest1' ,1);
                insert into fish() values (500, 'TestX', 'zloty', 23, 1000); 
                """;
        try (Statement stmt = ConnectionDB.getStatementForTestDb()) {
            stmt.execute(queryInsert);
        } catch (Exception e) {
            System.out.println(e);
        }
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        FishService fishService = new FishService();
        String data = "fishTest1" + "\r\n" + "typeTest1" + "\r\n" + 21 + "\r\n" + "Atest1";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        //When
        fishService.addFish();
        //Then
        assertThat("AquariumFullException")
                .isSubstringOf(outContent.toString());

        String queryDelete = """
                DELETE FROM aquarium WHERE name = 'Atest1';
                DELETE FROM fish WHERE aquarium_id = 1000; 
                """;
        try (Statement stmt = ConnectionDB.getStatementForTestDb()) {
            stmt.execute(queryDelete);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    void printAllFishFromAquarium() {
        String queryInsert = """
                INSERT INTO aquarium() VALUES (1000, 'Atest1' ,1);
                insert into fish() values (500, 'TestX', 'zloty', 23, 1000); 
                insert into fish() values (501, 'TestXX', 'silver', 50, 1000);
                """;
        try (Statement stmt = ConnectionDB.getStatementForTestDb()) {
            stmt.execute(queryInsert);
        } catch (Exception e) {
            System.out.println(e);
        }

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String data = "Atest1" + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));

        FishService fishService = new FishService();
        fishService.printAllFishFromAquarium();
        //Then
        assertThat("TestX zloty 23")
                .isSubstringOf(outContent.toString());
        assertThat("TestXX silver 50")
                .isSubstringOf(outContent.toString());

        String queryDelete = """
                DELETE FROM aquarium WHERE name = 'Atest1';
                DELETE FROM fish WHERE aquarium_id = 1000;
                """;
        try (Statement stmt = ConnectionDB.getStatementForTestDb()) {
            stmt.execute(queryDelete);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    void moveFish() {
        String queryInsert = """
                INSERT INTO aquarium() VALUES (1000, 'Atest1' ,1);
                INSERT INTO aquarium() VALUES (1001, 'Atest2' ,1);
                insert into fish() values (500, 'FishX', 'zloty', 23, 1000);
                """;
        try (Statement stmt = ConnectionDB.getStatementForTestDb()) {
            stmt.execute(queryInsert);
        } catch (Exception e) {
            System.out.println(e);
        }

        String data = "FishX" + "\r\n" + "Atest2" + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        FishService fishService = new FishService();
        fishService.moveFish();
        //Then
        assertThat("Fish: FishX - moved to Aquarium: Atest2")
                .isSubstringOf(outContent.toString());

        String queryDelete = """
                DELETE FROM aquarium WHERE name = 'Atest1';
                DELETE FROM aquarium WHERE name = 'Atest2';
                DELETE FROM fish WHERE aquarium_id = 1001;
                """;
        try (Statement stmt = ConnectionDB.getStatementForTestDb()) {
            stmt.execute(queryDelete);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    void getFish() {
        String queryInsert = """
                INSERT INTO aquarium() VALUES (1000, 'Atest1' ,1);
                insert into fish() values (500, 'FishX', 'zloty', 23, 1000);
                """;
        try (Statement stmt = ConnectionDB.getStatementForTestDb()) {
            stmt.execute(queryInsert);
        } catch (Exception e) {
            System.out.println(e);
        }
        Fish fishG = new Fish(500, "FishX", "zloty", 23, 1000);

        FishService fishService = new FishService();
        Fish fishR = fishService.getFish("FishX");
        //Then
        assertThat(fishG).isEqualTo(fishR);

        String queryDelete = """
                DELETE FROM aquarium WHERE name = 'Atest1';
                DELETE FROM fish WHERE aquarium_id = 1000;
                """;
        try (Statement stmt = ConnectionDB.getStatementForTestDb()) {
            stmt.execute(queryDelete);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}