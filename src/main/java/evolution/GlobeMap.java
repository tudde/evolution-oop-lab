package evolution;

public class GlobeMap extends AbstractWorldMap {
    public GlobeMap(Parameters params) {
        super(params);
    }

    public Vector2d correctPosition(Vector2d position, Animal animal) {
        if (position.y < 0 || position.y >= height) return null; // null?
        else if (position.x >= width) return new Vector2d(0, position.y);
        else if (position.x < 0) return new Vector2d(width - 1, position.y);
        return position;
    }
}
