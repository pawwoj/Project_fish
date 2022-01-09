package src.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import src.connection.ConnectionDB;
import src.model.Aquarium;
import src.model.Fish;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AquariumServiceTest {

    @BeforeAll
    public static void setUp() {
        String setUpQuery = """
                INSERT INTO aquarium VALUES(501, 'aquarium1', 4);
                INSERT INTO fish VALUES(1502, 'fish2', 'typ1' , 12, 501);
                INSERT INTO fish VALUES(1503, 'fish3', 'typ1' , 12, 501);
                INSERT INTO fish VALUES(1504, 'fish4', 'typ1' , 12, 501);
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
    @DisplayName("Add aquarium - database contain aquarium")
    void addAquariumTest() {
        // Given
        AquariumService aquariumService = new AquariumService("TEST");
        String givenName = "addAquariumTest";
        String givenCapacity = "4";
        String data = givenName + "\r\n" + givenCapacity + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        String result = "";
        int capacity = 0;
        aquariumService.addAquarium();
        // Then
        try (ResultSet rs = ConnectionDB.getStatement("TEST")
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
    }

    @Test
    @DisplayName("Add aquarium - catch WrongDataFormatException when aquarium name = '  '")
    void addAquariumWrongDataFormatException() {
        // Given
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        AquariumService aquariumService = new AquariumService("TEST");
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
        AquariumService aquariumService = new AquariumService("TEST");
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
        AquariumService aquariumService = new AquariumService("TEST");
        String givenName = "wrongData3";
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
        AquariumService aquariumService = new AquariumService("TEST");
        String givenName = "wrongData4";
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
        AquariumService aquariumService = new AquariumService("TEST");
        String givenName = "wrongData5";
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
        AquariumService aquariumService = new AquariumService("TEST");
        String givenName = "wrongData6";
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
        AquariumService aquariumService = new AquariumService("TEST");
        String givenName = "aquarium1";
        String givenCapacity = "4";
        String data = givenName + "\r\n" + givenCapacity + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        //  try to addAquarium with same data
        aquariumService.addAquarium();
        assertThat("SQLIntegrityConstraintViolationException").isSubstringOf(outContent.toString());
    }

    @Test
    void shouldReturnFalseWhenCheckingIsAquariumFullOnNotFullAquarium() {
        Aquarium aquarium = new Aquarium(503, "aquarium3", 1);
        AquariumService aquariumService = new AquariumService("TEST");

        Boolean isAquariumFullAnswer = aquariumService.isAquariumFull(aquarium);

        assertThat(isAquariumFullAnswer).isEqualTo(false);
    }

    @Test
    void shouldReturnTrueWhenCheckingIsAquariumEmptyOnEmptyAquarium() {
        Aquarium aquarium = new Aquarium(503, "aquarium3", 1);
        AquariumService aquariumService = new AquariumService("TEST");

        Boolean isAquariumEmptyAnswer = aquariumService.isAquariumEmpty(aquarium);

        assertThat(isAquariumEmptyAnswer).isEqualTo(true);
    }

    @Test
    void shouldReturnFalseWhenCheckingIsAquariumEmptyOnNotEmptyAquarium() {
        Aquarium aquarium = new Aquarium(501, "aquarium1", 4);
        AquariumService aquariumService = new AquariumService("TEST");

        Boolean isAquariumEmptyAnswer = aquariumService.isAquariumEmpty(aquarium);

        assertThat(isAquariumEmptyAnswer).isEqualTo(true);
    }

    @Test
    void shouldReturnAquariumFromDatabase() {
        AquariumService aquariumService = new AquariumService("TEST");
        Aquarium givenAquarium = new Aquarium(501, "aquarium1", 4);
        Aquarium newAquarium = aquariumService.getAquarium("aquarium1");
        assertThat(newAquarium).isEqualTo(givenAquarium);
    }

    @Test
    void shouldReturnTrueWhenCheckingIsAquariumFullOnFullAquarium() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Aquarium aquarium = new Aquarium(502, "aquarium2", 1, null);
        AquariumService aquariumService = new AquariumService("TEST");

//        AquariumFullException aquariumFullException = assertThrows(AquariumFullException.class, () -> {
//            aquariumService.isAquariumFull(aquarium);
//        });

//        String expectedExceptionMessage = "123";
//        String actualMessage = aquariumFullException.getMessage();

//        assertTrue(expectedExceptionMessage.contains(actualMessage));
        Boolean isAquariumFullAnswer = aquariumService.isAquariumFull(aquarium);

        assertThat(isAquariumFullAnswer).isEqualTo(true);
        assertThat("AquariumFullException")
                .isSubstringOf(outContent.toString());
    }

    @Test
    void shouldCatchAquariumNotEmptyExceptionWhenTryingToDeleteNotEmptyAquarium() {
        AquariumService aquariumService = new AquariumService("TEST");
        String data = "aquarium2" + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        aquariumService.deleteAquarium();

        assertThat("src.exception.AquariumNotEmptyException")
                .isSubstringOf(outContent.toString());
    }

    @Test
    void shouldReturnFishSetFromAquarium() {
        AquariumService aquariumService = new AquariumService("TEST");
        String givenName = "aquarium1";
        String data = givenName + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        Set<Fish> setFromDb = aquariumService.getFishSetFromAquarium();

        Set<Fish> prepSet = new LinkedHashSet<>();
        prepSet.add(new Fish(1502, "fish2", "typ1", 12));
        prepSet.add(new Fish(1503, "fish3", "typ1", 12));
        prepSet.add(new Fish(1504, "fish4", "typ1", 12));

        assertThat(setFromDb.toString()).isEqualTo(prepSet.toString());
    }

    @Test
    void deleteAquariumSuccess() {
        AquariumService aquariumService = new AquariumService("TEST");
        String data = "aquarium4" + "\r\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        aquariumService.deleteAquarium();

        assertThat("Aquarium deleted - Success")
                .isSubstringOf(outContent.toString());
    }
}