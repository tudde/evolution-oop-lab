package evolution;

import java.util.Random;

public enum MapDirection {
    NORTH(0),
    NORTH_EAST(1),
    EAST(2),
    SOUTH_EAST(3),
    SOUTH(4),
    SOUTH_WEST(5),
    WEST(6),
    NORTH_WEST(7);

    final static Random rand = new Random();
    private final int intValue;

   MapDirection(int val){
        this.intValue=val;
    }

    public int toInt(){
        return this.intValue;
    }


    public MapDirection rotate(int rotation){
       return values()[((this.intValue+rotation)%values().length)];
    }

    public Vector2d toUnitVector(){
        return switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case NORTH_EAST -> new Vector2d(1, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTH_EAST -> new Vector2d(1, -1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTH_WEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(-1, 0);
            case NORTH_WEST -> new Vector2d(-1, 1);
            default -> null;
        };

    }




    public static MapDirection RandomDirection(){
        return values()[rand.nextInt(values().length)];
    }
}


