package src.service;

import lombok.Getter;
import src.connection.ConnectionDB;
import src.model.Aquarium;
import src.model.Fish;
import src.validate.DataValidate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

@Getter
public class FishService {
    AquariumService aquariumService = new AquariumService();
    DataValidate dataValidate = new DataValidate();

    public void addFish() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter fish name:");
        String name = scanner.nextLine();
        System.out.println("Enter fish type name:");
        String type = scanner.nextLine();
        System.out.println("Enter fish price:");
        String price = scanner.nextLine();
        System.out.println("Enter aquarium name for fish:");
        String aquariumName = scanner.nextLine();
        if (dataValidate.isNameValidate(name)
                && dataValidate.isNameValidate(type)
                && dataValidate.isCapacityOrPriceValidate(price)
                && dataValidate.isNameValidate(aquariumName)) {
            Aquarium aquarium = aquariumService.getAquarium(aquariumName);
            if (dataValidate.isNameValidate(aquarium.getName()) && !aquariumService.isAquariumFull(aquarium)) {
                try (Statement stmt = ConnectionDB.getStatement()) {
                    stmt.execute("INSERT INTO fish(name, type, price, aquarium_id) VALUES ("
                            + "'" + name + "','" + type + "'," + price + "," + aquarium.getId() + ");");
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        }
    }

    public void printAllFishFromAquarium() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter aquarium name:");
        String aquariumName = scanner.nextLine();

        Aquarium aquarium = aquariumService.getAquarium(aquariumName);
        if (aquarium.getId() == 0) {
            System.out.println("Wrong name for aquarium: " + aquariumName);
            return;
        }
        try (ResultSet rs = ConnectionDB.getStatement()
                .executeQuery("SELECT name, type, price FROM fish WHERE aquarium_id = "
                        + aquarium.getId() + ";")) {
            while (rs.next()) {
                System.out.println(
                        rs.getString(1) + " "
                                + rs.getString(2) + " "
                                + rs.getInt(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveFish() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter fish name:");
        String fishName = scanner.nextLine();
        Fish fish = getFish(fishName);
        if (fish.getId() == 0) {
            System.out.println("Wrong name for fish: " + fishName);
            return;
        }
        System.out.println("Enter aquarium name:");
        String aquariumName = scanner.nextLine();
        Aquarium aquarium = aquariumService.getAquarium(aquariumName);
        System.out.println(aquarium);
        if (fish.getAquarium() == aquarium.getId()) {
            System.out.println(fishName + " is already in this aquarium " + aquariumName);
            return;
        }
        if (aquarium.getId() == 0) {
            System.out.println("Wrong name for aquarium: " + aquariumName);
            return;
        }
        if (aquariumService.isAquariumFull(aquarium)) {
            System.out.println("Aquarium is full: " + aquariumName);
            return;
        }
        try (Statement stmt = ConnectionDB.getStatement()) {
            stmt.execute("UPDATE fish SET aquarium_id = " + aquarium.getId() + " WHERE id =" + fish.getId() + ";");
            System.out.println("Fish: " + fish.getName() + " - moved to Aquarium: " + aquarium.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Fish getFish(String fishName) {
        Fish fish = new Fish();
        try (ResultSet resultSet = ConnectionDB.getStatement()
                .executeQuery("SELECT * FROM fish WHERE name='" + fishName + "';")) {
            while (resultSet.next()) {
                fish.setId(resultSet.getInt(1));
                fish.setName(resultSet.getString(2));
                fish.setType(resultSet.getString(3));
                fish.setPrice(resultSet.getInt(4));
                fish.setAquarium(resultSet.getInt(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fish;
    }
}