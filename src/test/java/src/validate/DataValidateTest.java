package src.validate;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

class DataValidateTest {

    @Test
    void shouldReturnTrueAfterGiveCorrectString() {
        DataValidate dataValidate = new DataValidate();
        assertThat(dataValidate.isNameValidate("Test")).isEqualTo(true);
    }

    @Test
    void shouldCatchWrongDataFormatException() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        DataValidate dataValidate = new DataValidate();

        assertThat(dataValidate.isNameValidate("  ")).isEqualTo(false);
        assertThat("WrongDataFormatException")
                .isSubstringOf(outContent.toString());
    }

    @Test
    void shouldCatchNullPointerException() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        DataValidate dataValidate = new DataValidate();
        String s = null;

        assertThat(dataValidate.isNameValidate(s)).isEqualTo(false);
        assertThat("NullPointerException")
                .isSubstringOf(outContent.toString());
    }

    @Test
    void shouldReturnTrueAfterGiveIntAsStringHigherThen0() {
        DataValidate dataValidate = new DataValidate();
        assertThat(dataValidate.isCapacityOrPriceValidate("5")).isEqualTo(true);
    }

    @Test
    void shouldCatchCapacityNumberException() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        DataValidate dataValidate = new DataValidate();


        assertThat(dataValidate.isCapacityOrPriceValidate("-2")).isEqualTo(false);
        assertThat("CapacityNumberException")
                .isSubstringOf(outContent.toString());
    }

    @Test
    void shouldCatchNumberFormatException() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        DataValidate dataValidate = new DataValidate();

        assertThat(dataValidate.isCapacityOrPriceValidate("xxx")).isEqualTo(false);
        assertThat("NumberFormatException")
                .isSubstringOf(outContent.toString());
    }
}