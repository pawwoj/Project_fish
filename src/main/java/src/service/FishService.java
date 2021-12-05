package src.service;

import src.connection.ConnectionDB;
import src.model.Aquarium;
import src.model.Fish;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class FishService {
    AquariumService aquariumService = new AquariumService();

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
        Aquarium aquarium = aquariumService.getAquarium(aquariumName);
        if (aquarium.getId() == 0) {
            System.out.println("Wrong name for aquarium: " + aquariumName);
            return;
        }
        if (aquariumService.isAquariumFull(aquarium)) {
            System.out.println("Aquarium is full: " + aquariumName);
            return;
        }
        ConnectionDB connectionDB = new ConnectionDB();
        try {
            connectionDB.getStatement()
                    .execute("INSERT INTO fish(name, type, price, aquarium_id) VALUES ("
                            + "'" + name + "', '" + type + "', " + price + ", " + aquarium.getId() + ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printAllFishesFromAquarium() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter aquarium name:");
        String aquariumName = scanner.nextLine();

        Aquarium aquarium = aquariumService.getAquarium(aquariumName);
        if (aquarium.getId() == 0) {
            System.out.println("Wrong name for aquarium: " + aquariumName);
            return;
        }
        ConnectionDB connectionDB = new ConnectionDB();
        try {
            ResultSet resultSet = connectionDB.getStatement()
                    .executeQuery("SELECT name, type, price FROM fish WHERE aquarium_id = "
                            + aquarium.getId() + ";");
            while (resultSet.next()) {
                System.out.println(
                        resultSet.getString(1) + " "
                                + resultSet.getString(2) + " "
                                + resultSet.getInt(3));
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
        updateAquariumForFish(aquarium.getId(), fish.getId());
    }

    public Fish getFish(String fishName) {
        ConnectionDB connectionDB = new ConnectionDB();
        Fish fish = new Fish();
        try {
            ResultSet resultSet = connectionDB.getStatement()
                    .executeQuery("SELECT * FROM fish WHERE name='" + fishName + "';");
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

    public void updateAquariumForFish(int aquariumId, int fishId) {
        ConnectionDB connectionDB = new ConnectionDB();
        try {
            connectionDB.getStatement()
                    .execute("UPDATE fish SET aquarium_id = " + aquariumId + " WHERE id =" + fishId + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
