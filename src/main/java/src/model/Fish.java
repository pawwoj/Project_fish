package src.model;

import lombok.*;

@Setter
@Getter

@AllArgsConstructor
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class Fish {
    private int id;
    private String name;
    private String type;
    private int price;
    private int aquarium;

    @Override
    public String toString() {
        return this.id + " " + this.name + " " + this.type + " " + this.price;
    }
}