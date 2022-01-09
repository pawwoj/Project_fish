package src.model;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class Fish {
    @NonNull
    private int id;
    @NonNull
    private String name;
    @NonNull
    private String type;
    @NonNull
    private int price;

    private int aquariumID;

    private Aquarium aquarium;

    @Override
    public String toString() {
        return this.id + " " + this.name + " " + this.type + " " + this.price;
    }
}