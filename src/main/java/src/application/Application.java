package src.application;

import src.service.AquariumService;
import src.service.FishService;

import java.util.Scanner;

public class Application {
    public void run() {
        Scanner scanner = new Scanner(System.in);
        FishService fishService = new FishService();
        AquariumService aquariumService = new AquariumService();

        label:
        while (true) {
            System.out.println(
                    """
                            | [1] Add fish  | [2]  Add aquarium    | [3]  Print fish form aquarium |
                            | [4] Move fish | [5] Remove aquarium  |
                            | [0] Exit""");
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    fishService.addFish();
                    break;
                case "2":
                    aquariumService.addAquarium();
                    break;
                case "3":
                    fishService.printAllFishFromAquarium();
                    break;
                case "4":
                    fishService.moveFish();
                    break;
                case "5":
                    aquariumService.deleteAquarium();
                    break;
                case "0":
                    break label;
                default:
                    System.out.println("Wrong command");
                    break;
            }
        }
    }
}