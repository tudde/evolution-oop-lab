package evolution;

public class Plant implements IMapElement{

    private Vector2d position;

    public Plant(Vector2d position){
        this.position=position;
    }



    @Override
    public String toString() {
        return "*";
    }

    public Vector2d getPosition() {
        return position;
    }
}
