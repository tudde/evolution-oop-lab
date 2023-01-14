package evolution;


public class MapSquare {
    private int deadAnimals = 0;
    public final Vector2d position;

    public MapSquare(Vector2d position) {
        this.position = position;
    }

    public Vector2d getPosition() {
        return position;
    }

    public int getDead() {
        return deadAnimals;
    }

    public void addDead() {
        deadAnimals += 1;
    }

}
