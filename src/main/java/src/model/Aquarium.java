package src.model;

import lombok.*;

@Setter
@Getter

@AllArgsConstructor
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class Aquarium {
    private int id;
    private String name;
    private int capacity;

    @Override
    public String toString() {
        return this.id + " " + this.name + " " + this.capacity;
    }
}