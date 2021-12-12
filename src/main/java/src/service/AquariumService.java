package src.service;

import lombok.Getter;
import src.connection.ConnectionDB;
import src.exception.AquariumFullException;
import src.exception.AquariumNotEmptyException;
import src.model.Aquarium;
import src.validate.DataValidate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

@Getter
public class AquariumService {
    ConnectionDB connectionDB = new ConnectionDB();
    DataValidate dataValidate = new DataValidate();

    public void addAquarium() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter aquarium name:");
        String name = scanner.nextLine();
        System.out.println("Enter aquarium capacity:");
        String capacity = scanner.nextLine();
        if (dataValidate.isCapacityOrPriceValidate(capacity) && dataValidate.isNameValidate(name)) {
            try {
                connectionDB.getStatement()
                        .execute("INSERT INTO aquarium(name, capacity) VALUES ("
                                + "'" + name + "', " + capacity + ");");
            } catch (SQLException e) {
                System.out.println(e);
                System.out.println("For name: " + name + " and capacity : " + capacity);
            }
        }
    }

    public Aquarium getAquarium(String aquariumName) {
        Aquarium aquarium = new Aquarium();
        try {
            ResultSet resultSet = connectionDB.getStatement()
                    .executeQuery("SELECT * FROM aquarium WHERE name='" + aquariumName + "';");
            while (resultSet.next()) {
                aquarium.setId(resultSet.getInt(1));
                aquarium.setName(resultSet.getString(2));
                aquarium.setCapacity(resultSet.getInt(3));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return aquarium;
    }

    public boolean isAquariumFull(Aquarium aquarium) {
        boolean aquariumFull = true;
        try {
            ResultSet resultSet = connectionDB.getStatement()
                    .executeQuery("SELECT COUNT(id) FROM fish WHERE aquarium_id =" + aquarium.getId() + ";");
            while (resultSet.next()) {
                aquariumFull = resultSet.getInt(1) >= aquarium.getCapacity();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        try {
            if (aquariumFull) {
                throw new AquariumFullException("Aquarium if full");
            }
        } catch (AquariumFullException e) {
            System.out.println(e);
        }
        return aquariumFull;
    }

    public boolean isAquariumEmpty(Aquarium aquarium) {
        boolean isEmpty = false;
        try {
            ResultSet resultSet = connectionDB.getStatement()
                    .executeQuery("SELECT COUNT(id) FROM fish WHERE aquarium_id =" + aquarium.getId() + ";");
            while (resultSet.next()) {
                try {
                    if (!(isEmpty = (resultSet.getInt(1) == 0))) {
                        throw new AquariumNotEmptyException("Aquarium is not empty");
                    }
                } catch (AquariumNotEmptyException e) {
                    System.out.println(e);
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return isEmpty;
    }

    public void deleteAquarium() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter aquarium name:");
        String name = scanner.nextLine();
        if (dataValidate.isNameValidate(name)) {
            Aquarium aquarium = getAquarium(name);
            if (dataValidate.isNameValidate(aquarium.getName()) && isAquariumEmpty(aquarium)) {
                try {
                    connectionDB.getStatement()
                            .execute("DELETE FROM aquarium WHERE id = " + aquarium.getId() + ";");
                    System.out.println("Aquarium deleted - Success");
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        }
    }
}