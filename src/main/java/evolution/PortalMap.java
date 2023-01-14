package evolution;

import java.util.Random;

public class PortalMap extends AbstractWorldMap {
    public PortalMap(Parameters params) {
        super(params);
    }


    public Vector2d correctPosition(Vector2d position, Animal animal) {
        if (!canMoveTo(position)) {
            animal.lowerEnergy(params.breedEnergy);
            return new Vector2d(rng.nextInt(width), rng.nextInt(height));
        }
        return position;
    }
}
