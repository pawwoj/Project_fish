package src.service;

import lombok.Getter;
import src.connection.ConnectionDB;
import src.model.Aquarium;
import src.model.Fish;
import src.validate.DataValidate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

@Getter
public class FishService {
    String env;

    DataValidate dataValidate = new DataValidate();

    public FishService(String env) {
        this.env = env;
    }


  //  AquariumService aquariumService = new AquariumService(getEnv());
//    DataValidate dataValidate = new DataValidate();

    private List fishDataEnteredByUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter fish name:");
        String name = scanner.nextLine();
        System.out.println("Enter fish type name:");
        String type = scanner.nextLine();
        System.out.println("Enter fish price:");
        String price = scanner.nextLine();
        System.out.println("Enter aquarium name for fish:");
        String aquariumName = scanner.nextLine();

        return List.of(name, type, price, aquariumName);
    }

    private boolean validateIfFishExists(Fish fish) {
        return fish != null;
    }

    private Fish getFishFromNameTypedByUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter fish name:");
        String fishName = scanner.nextLine();
        return getFish(fishName);
    }

    public Fish getFishByName(String fishName) {
        return dataValidate.isNameValidate(fishName) ? getFish(fishName) : null;
    }

    private boolean validateEnteredDataForFish(List fishParams) {
        return dataValidate.isNameValidate(String.valueOf(fishParams.get(0)))
                && dataValidate.isNameValidate(String.valueOf(fishParams.get(1)))
                && dataValidate.isCapacityOrPriceValidate(String.valueOf(fishParams.get(2)))
                && dataValidate.isNameValidate(String.valueOf(fishParams.get(3)));
    }

    public boolean addFish() {
        AquariumService aquariumService = new AquariumService(getEnv());
        List fishDataList = fishDataEnteredByUser();
        if (!validateEnteredDataForFish(fishDataList)) {
            System.out.println("Wrong data for new fish");
            return false;
        }
        Aquarium aquarium = aquariumService.getAquariumByName(String.valueOf(fishDataList.get(3)));
        if (aquariumService.validateIfAquariumExists(aquarium) && !aquariumService.isAquariumFull(aquarium)) {
            try (Statement stmt = ConnectionDB.getStatement(getEnv())) {
                stmt.execute("INSERT INTO fish(name, type, price, aquarium_id) VALUES ("
                        + "'" + fishDataList.get(0) + "','" + fishDataList.get(1) + "'," + fishDataList.get(2) + "," + aquarium.getId() + ");");
                return true;
            } catch (SQLException e) {
                System.out.println(e);
                return false;
            }
        }
        return false;
    }

    public boolean moveFish() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter aquarium name:");
        String fishName = scanner.nextLine();
        System.out.println("Enter aquarium name:");
        String aquariumName = scanner.nextLine();

        AquariumService aquariumService = new AquariumService(getEnv());
        Fish fish = getFishByName(fishName);
        if (!validateIfFishExists(fish)) {
            return false;
        }
        Aquarium aquarium = aquariumService.getAquariumByName(aquariumName);
        if (!aquariumService.validateAquariumForNewFish(fish.getAquariumID(), aquarium)) {
            return false;
        }
        String moveFishQuery =
                "UPDATE fish SET aquarium_id = " + aquarium.getId() + " WHERE id =" + fish.getId() + ";";
        try (Statement stmt = ConnectionDB.getStatement(getEnv())) {
            stmt.execute(moveFishQuery);
            System.out.println("Fish: " + fish.getName() + " - moved to Aquarium: " + aquarium.getName());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Fish getFish(String fishName) {
        String getFishQuery = "SELECT * FROM fish WHERE name='" + fishName + "';";
        try (ResultSet resultSet = ConnectionDB.getStatement(getEnv())
                .executeQuery(getFishQuery)) {
            while (resultSet.next()) {
                Fish fish = new Fish();
                fish.setId(resultSet.getInt(1));
                fish.setName(resultSet.getString(2));
                fish.setType(resultSet.getString(3));
                fish.setPrice(resultSet.getInt(4));
                fish.setAquariumID(resultSet.getInt(5));
                return fish;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}