package src.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import src.connection.ConnectionDB;
import src.model.Aquarium;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

class AquariumServiceTest {

    @Test
    @DisplayName("Add aquarium - database contain aquarium")
    void addAquariumTest() {
        // Given
        AquariumService aquariumService = new AquariumService();
        String givenName = "test11";
        String givenCapacity = "4";
        String data = givenName + "\r\n" + givenCapacity + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        String result = "";
        int capacity = 0;
        // When
        aquariumService.addAquarium();
        // Then
        try (ResultSet rs = ConnectionDB.getStatement()
                .executeQuery("SELECT * FROM fishproject_test.aquarium WHERE name='" + givenName + "';")) {
            while (rs.next()) {
                result = rs.getString(2);
                capacity = rs.getInt(3);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertThat(result).isEqualTo(givenName);
        assertThat(capacity + "").isEqualTo(givenCapacity);
        // After - delete tested record
        try (Statement stmt = ConnectionDB.getStatement()) {
            stmt.execute("DELETE FROM aquarium WHERE name = '" + givenName + "';");
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
        String givenName = " ";
        String givenCapacity = "4";
        String data = givenName + "\r\n" + givenCapacity + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        // When
        aquariumService.addAquarium();
        //Then
        assertThat("WrongDataFormatException").isSubstringOf(outContent.toString());
    }

    @Test
    @DisplayName("Add aquarium - WrongDataFormatException - aquarium name not entered")
    void addAquariumWrongDataFormatException1() {
        // Given
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        AquariumService aquariumService = new AquariumService();
        String givenCapacity = "4";
        String data = "\r\n" + givenCapacity + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        // When
        aquariumService.addAquarium();
        //Then
        assertThat("WrongDataFormatException").isSubstringOf(outContent.toString());
    }

    @Test
    @DisplayName("Add aquarium - CapacityNumberException - givenCapacity = -2")
    void addAquariumWrongDataFormatException3() {
        // Given
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        AquariumService aquariumService = new AquariumService();
        String givenName = "test1";
        String givenCapacity = "-4";
        String data = givenName + "\r\n" + givenCapacity + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        // When
        aquariumService.addAquarium();
        //Then
        assertThat("CapacityNumberException").isSubstringOf(outContent.toString());
    }

    @Test
    @DisplayName("Add aquarium - CapacityNumberException - givenCapacity = '' ")
    void addAquariumWrongDataFormatException4() {
        // Given
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        AquariumService aquariumService = new AquariumService();
        String givenName = "test1";
        String givenCapacity = "";
        String data = givenName + "\r\n" + givenCapacity + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        // When
        aquariumService.addAquarium();
        //Then
        assertThat("NumberFormatException").isSubstringOf(outContent.toString());
    }

    @Test
    @DisplayName("Add aquarium - CapacityNumberException - givenCapacity = 'text' ")
    void addAquariumWrongDataFormatException5() {
        // Given
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        AquariumService aquariumService = new AquariumService();
        String givenName = "test1";
        String givenCapacity = "@#";
        String data = givenName + "\r\n" + givenCapacity + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        // When
        aquariumService.addAquarium();
        //Then
        assertThat("NumberFormatException").isSubstringOf(outContent.toString());
    }

    @Test
    @DisplayName("Add aquarium - CapacityNumberException - givenCapacity = null ")
    void addAquariumWrongDataFormatException6() {
        // Given
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        AquariumService aquariumService = new AquariumService();
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
        String givenName = "test1";
        String givenCapacity = "2";
        String data = givenName + "\r\n" + givenCapacity + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        // When
        aquariumService.addAquarium();
        //  try to addAquarium with same data
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        aquariumService.addAquarium();
        assertThat("SQLIntegrityConstraintViolationException").isSubstringOf(outContent.toString());
        // After - delete tested record
        try (Statement stmt = ConnectionDB.getStatement()) {
            stmt.execute("DELETE FROM aquarium WHERE name = '" + givenName + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldReturnAquariumFromDatabase() {
        try (Statement stmt = ConnectionDB.getStatement()) {
            stmt.execute("INSERT INTO aquarium() VALUES (1000, 'Atest1' ,1);");
        } catch (Exception e) {
            System.out.println(e);
        }
        AquariumService aquariumService = new AquariumService();
        Aquarium givenAquarium = new Aquarium(1000, "Atest1", 1);

        Aquarium newAquarium = aquariumService.getAquarium("Atest1");

        assertThat(newAquarium).isEqualTo(givenAquarium);

        try (Statement stmt = ConnectionDB.getStatement()) {
            stmt.execute("DELETE FROM aquarium WHERE name = 'Atest1';");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    void shouldReturnTrueWhenChceckingIsAquariumFullOnFullAquarium() {
        String queryInsert = """
                INSERT INTO aquarium() VALUES (100, 't1' ,1);
                insert into fish() values (50, 'fishTest1', 'zloty', 23, 100);
                """;
        try (Statement stmt = ConnectionDB.getStatement()) {
            stmt.execute(queryInsert);
        } catch (Exception e) {
            System.out.println(e);
        }
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Aquarium aquarium = new Aquarium(100, "t1", 1);
        AquariumService aquariumService = new AquariumService();

        Boolean isAquariumFullAnswer = aquariumService.isAquariumFull(aquarium);

        assertThat(isAquariumFullAnswer).isEqualTo(true);
        assertThat("AquariumFullException")
                .isSubstringOf(outContent.toString());

        String queryDelete = """
                DELETE FROM aquarium WHERE name = 't1';
                DELETE FROM fish WHERE aquarium_id = 100; 
                """;
        try (Statement stmt = ConnectionDB.getStatement()) {
            stmt.execute(queryDelete);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    void shouldReturnFalseWhenChceckingIsAquariumFullOnNotFullAquarium() {
        try (Statement stmt = ConnectionDB.getStatement()) {
            stmt.execute("INSERT INTO aquarium() VALUES (100, 't1' ,1);");
        } catch (Exception e) {
            System.out.println(e);
        }
        Aquarium aquarium = new Aquarium(100, "t1", 1);
        AquariumService aquariumService = new AquariumService();

        Boolean isAquariumFullAnswer = aquariumService.isAquariumFull(aquarium);

        assertThat(isAquariumFullAnswer).isEqualTo(false);

        try (Statement stmt = ConnectionDB.getStatement()) {
            stmt.execute("DELETE FROM aquarium WHERE name = 't1';");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    void shouldReturnTrueWhenChceckingIsAquariumEmptyOnEmptyAquarium() {
        try (Statement stmt = ConnectionDB.getStatement()) {
            stmt.execute("INSERT INTO aquarium() VALUES (100, 't1' ,1);");
        } catch (Exception e) {
            System.out.println(e);
        }
        Aquarium aquarium = new Aquarium(100, "t1", 1);
        AquariumService aquariumService = new AquariumService();

        Boolean isAquariumEmptyAnswer = aquariumService.isAquariumEmpty(aquarium);

        assertThat(isAquariumEmptyAnswer).isEqualTo(true);

        try (Statement stmt = ConnectionDB.getStatement()) {
            stmt.execute("DELETE FROM aquarium WHERE name = 't1';");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    void shouldReturnFalseWhenCheckingIsAquariumEmptyOnNotEmptyAquarium() {
        String queryInsert = """
                INSERT INTO aquarium() VALUES (100, 't1' ,1);
                insert into fish() values (50, 'fishTest1', 'zloty', 23, 100); 
                """;
        try (Statement stmt = ConnectionDB.getStatement()) {
            stmt.execute(queryInsert);
        } catch (Exception e) {
            System.out.println(e);
        }
        Aquarium aquarium = new Aquarium(100, "t1", 1);
        AquariumService aquariumService = new AquariumService();

        Boolean isAquariumEmptyAnswer = aquariumService.isAquariumEmpty(aquarium);

        assertThat(isAquariumEmptyAnswer).isEqualTo(false);

        String queryDelete = """
                DELETE FROM aquarium WHERE name = 't1';
                DELETE FROM fish WHERE aquarium_id = 100; 
                """;
        try (Statement stmt = ConnectionDB.getStatement()) {
            stmt.execute(queryDelete);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    void ShouldCatchAquariumNotEmptyExceptionWhenTryingToDeleteNotEmptyAquarium() {
        String queryInsert = """
                INSERT INTO aquarium() VALUES (100, 't1' ,1);
                insert into fish() values (50, 'fishTest1', 'zloty', 23, 100); 
                """;
        try (Statement stmt = ConnectionDB.getStatement()) {
            stmt.execute(queryInsert);
        } catch (Exception e) {
            System.out.println(e);
        }
        AquariumService aquariumService = new AquariumService();
        String data = "t1" + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        aquariumService.deleteAquarium();


        assertThat("src.exception.AquariumNotEmptyException")
                .isSubstringOf(outContent.toString());

        String queryDelete = """
                DELETE FROM aquarium WHERE name = 'Atest1';
                DELETE FROM fish WHERE aquarium_id = 1000; 
                """;
        try (Statement stmt = ConnectionDB.getStatement()) {
            stmt.execute(queryDelete);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    void deleteAquariumSuccess() {
        try (Statement stmt = ConnectionDB.getStatement()) {
            stmt.execute("INSERT INTO aquarium() VALUES (100, 't1' ,1);");
        } catch (Exception e) {
            System.out.println(e);
        }
        AquariumService aquariumService = new AquariumService();
        String data = "t1" + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        aquariumService.deleteAquarium();

        assertThat("Aquarium deleted - Success")
                .isSubstringOf(outContent.toString());
    }
}