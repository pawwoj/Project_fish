package src.validate;

import src.exception.CapacityNumberException;
import src.exception.WrongDataFormatException;

public class DataValidate {

    public boolean isNameValidate(String name) {
        try {
            if (name.isBlank()) {
                throw new WrongDataFormatException("Name can't be unsubscribed");
            }
        } catch (WrongDataFormatException | NullPointerException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    public boolean isCapacityOrPriceValidate(String number) {
        try {
            if (Integer.parseInt(number) <= 0) {
                throw new CapacityNumberException("Capacity must be greater than 0");
            }
        } catch (CapacityNumberException | NumberFormatException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }
}