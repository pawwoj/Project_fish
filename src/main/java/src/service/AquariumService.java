package src.service;

import src.connection.ConnectionDB;
import src.model.Aquarium;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AquariumService {

    public void addAquarium() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter aquarium name:");
        String name = scanner.nextLine();
        System.out.println("Enter aquarium capacity:");
        String capacity = scanner.nextLine();

        ConnectionDB connectionDB = new ConnectionDB();
        try {
            connectionDB.getStatement()
                    .execute("insert into aquarium(name, capacity) values ("
                            + "'" + name + "', " + capacity + ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Aquarium getAquarium(String aquariumName) {
        ConnectionDB connectionDB = new ConnectionDB();
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
            e.printStackTrace();
        }
        return aquarium;
    }

    public boolean isAquariumFull(Aquarium aquarium) {
        ConnectionDB connectionDB = new ConnectionDB();
        boolean isSpace = true;
        try {
            ResultSet resultSet = connectionDB.getStatement()
                    .executeQuery("SELECT COUNT(id) FROM fish WHERE aquarium_id =" + aquarium.getId() + ";");
            while (resultSet.next()) {
                isSpace = resultSet.getInt(1) >= aquarium.getCapacity();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isSpace;
    }

    public boolean isAquariumEmpty(Aquarium aquarium) {
        ConnectionDB connectionDB = new ConnectionDB();
        boolean isEmpty = false;
        try {
            ResultSet resultSet = connectionDB.getStatement()
                    .executeQuery("SELECT COUNT(id) FROM fish WHERE aquarium_id =" + aquarium.getId() + ";");
            while (resultSet.next()) {
                isEmpty = (resultSet.getInt(1) == 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isEmpty;
    }

    public void deleteAquarium() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter aquarium name:");
        String name = scanner.nextLine();
        Aquarium aquarium = getAquarium(name);

        if (aquarium.getId() == 0) {
            System.out.println("Wrong name for aquarium: " + name);
            return;
        }
        if (!isAquariumEmpty(aquarium)) {
            System.out.println(name + " aquarium isn't empty");
            return;
        }
        ConnectionDB connectionDB = new ConnectionDB();
        try {
            connectionDB.getStatement()
                    .execute("DELETE FROM aquarium WHERE id = " + aquarium.getId() + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}