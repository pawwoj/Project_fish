package src.model;

import lombok.*;

import java.util.Set;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Aquarium {
    @NonNull
    private int id;
    @NonNull
    private String name;
    @NonNull
    private int capacity;
    private Set<Fish> fishSet;

    @Override
    public String toString() {
        return this.fishSet == null ?
                this.id + " " + this.name + " " + this.capacity :
                this.id + " " + this.name + " " + this.capacity + " " + this.fishSet;
    }
}