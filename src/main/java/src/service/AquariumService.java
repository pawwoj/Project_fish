package src.service;

import lombok.Getter;
import src.connection.ConnectionDB;
import src.exception.AquariumFullException;
import src.exception.AquariumNotEmptyException;
import src.model.Aquarium;
import src.model.Fish;
import src.validate.DataValidate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

@Getter
public class AquariumService {
    String env;
    DataValidate dataValidate = new DataValidate();

    public AquariumService(String env) {
        this.env = env;
    }

    public boolean validateIfAquariumExists(Aquarium aquarium) {
        if (aquarium == null) {
            System.out.println("Wrong name for aquarium - there is no aquarium in the database");
            return false;
        }
        return true;
    }

    public void addAquarium() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter aquarium name:");
        String name = scanner.nextLine();
        System.out.println("Enter aquarium capacity:");
        String capacity = scanner.nextLine();
        if (dataValidate.isCapacityOrPriceValidate(capacity) && dataValidate.isNameValidate(name)) {
            String insertQuery = "INSERT INTO aquarium(name, capacity) VALUES ('" + name + "', " + capacity + ");";
            try (Statement stmt = ConnectionDB.getStatement(getEnv())) {
                stmt.execute(insertQuery);
            } catch (SQLException e) {
                System.out.println(e);
                System.out.println("ERROR: For name: " + name + " and capacity : " + capacity);
            }
        }
    }

    protected Aquarium getAquarium(String aquariumName) {
        System.out.println(getEnv());
        String getAquariumQuery = "SELECT * FROM aquarium WHERE name='" + aquariumName + "';";
        try (ResultSet resultSet = ConnectionDB.getStatement(getEnv()).executeQuery(getAquariumQuery)) {
            while (resultSet.next()) {
                Aquarium aquarium = new Aquarium();
                aquarium.setId(resultSet.getInt(1));
                aquarium.setName(resultSet.getString(2));
                aquarium.setCapacity(resultSet.getInt(3));
                return aquarium;
            }
        } catch (NullPointerException e) {
            System.out.println(e);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public boolean isAquariumFull(Aquarium aquarium) {
        boolean aquariumFull = true;
        String countFishQuery = "SELECT COUNT(id) FROM fish WHERE aquarium_id =" + aquarium.getId() + ";";
        try (ResultSet resultSet = ConnectionDB.getStatement(getEnv()).executeQuery(countFishQuery)) {
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
        String countFishQuery = "SELECT COUNT(id) FROM fish WHERE aquarium_id =" + aquarium.getId() + ";";
        try (ResultSet resultSet = ConnectionDB.getStatement(getEnv()).executeQuery(countFishQuery)) {
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

    public boolean deleteAquarium() {
        Aquarium aquarium = getAquariumFromNameEnteredByUser();
        if (isAquariumEmpty(aquarium)) {
            String deleteAquariumQuery = "DELETE FROM aquarium WHERE id = " + aquarium.getId() + ";";
            try (Statement stmt = ConnectionDB.getStatement(getEnv())) {
                stmt.execute(deleteAquariumQuery);
                System.out.println("Aquarium deleted - Success");
                return true;
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        return false;
    }

    public Set<Fish> getFishSetFromAquarium() {
        Aquarium aq = getAquariumFromNameEnteredByUser();
        Aquarium aquarium = new Aquarium(aq.getId(), aq.getName(), aq.getCapacity(), new LinkedHashSet<>());
        if (!validateIfAquariumExists(aquarium)) {
            System.out.println("Wrong name for aquarium");
            return null;
        }
        String getFishSetQuery = "SELECT * FROM fish WHERE aquarium_id = " + aquarium.getId() + ";";
        try (ResultSet rs = ConnectionDB.getStatement(getEnv()).executeQuery(getFishSetQuery)) {
            while (rs.next()) {
                aquarium.getFishSet()
                        .add(new Fish(rs.getInt(1)
                                , rs.getString(2)
                                , rs.getString(3)
                                , rs.getInt(4)
                                , aquarium.getId()
                                , aq));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aquarium.getFishSet();
    }

    public Aquarium getAquariumFromNameEnteredByUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter aquarium name:");
        String aquariumName = scanner.nextLine();
        return dataValidate.isNameValidate(aquariumName) ? getAquarium(aquariumName) : null;
    }

    public Aquarium getAquariumByName(String aquariumName) {
        return dataValidate.isNameValidate(aquariumName) ? getAquarium(aquariumName) : null;
    }

    public boolean validateAquariumForNewFish(int idAquariumWhereIsFish, Aquarium aquarium) {
        if (idAquariumWhereIsFish == aquarium.getId()) {
            System.out.println("Fish is already in this aquarium");
            return false;
        }

        if (aquarium == null) {
            System.out.println("Wrong name for aquarium");
            return false;
        }

        if (isAquariumFull(aquarium)) {
            System.out.println("Aquarium is full");
            return false;
        }
        return true;
    }
}