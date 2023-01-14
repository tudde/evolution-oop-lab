package evolution;

import java.util.*;

public abstract class AbstractWorldMap implements IPositionChangeObserver {

    protected final int width;
    protected final int height;
    protected final Parameters params;
    protected final static Random rng = new Random();
    protected ArrayList<Vector2d> biasedPositions = new ArrayList<>();
    protected Map<Vector2d, ArrayList<Animal>> animals = new HashMap<>();
    protected Map<Vector2d, Plant> plants = new HashMap<>();
    protected MapSquare[] squares;

    public AbstractWorldMap(Parameters params) {
        this.width = params.mapWidth;
        this.height = params.mapHeight;
        this.params = params;

        //equator map type
        if (params.plantGrowthType == 0) {
            biasEquator();
        }
        //least died on map type
        else {
            squares = new MapSquare[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    squares[i * width + j] = new MapSquare(new Vector2d(j, i));
                }
            }
            biasCorpseless();
        }
        spawnNPlants(params.startPlants);
    }

    public int getTotalPlants() {
        return plants.size();
    }

    public boolean isBiased(Vector2d position) {
        return biasedPositions.contains(position);
    }

    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    public ArrayList<Animal> animalsAt(Vector2d position) {
        return animals.get(position); // dehermetyzacja
    }

    public boolean canMoveTo(Vector2d position) {
        return position.precedes(new Vector2d(width - 1, height - 1)) // nowe obiekty co wywołanie
                && position.follows(new Vector2d(0, 0));
    }


    public abstract Vector2d correctPosition(Vector2d position, Animal animal);


    public boolean moveAnimal(Vector2d position, Animal animal) {

        if (animalsAt(position) == null) {
            ArrayList<Animal> list = new ArrayList<>();
            list.add(animal);
            animals.put(position, list);

        } else {
            animals.get(position).add(animal);
        }

        Plant p = plantAt(position);
        if (p != null) {
            plants.remove(position);
            animal.eatPlant(); // trawę miało jeść najsilniejsze zwierzę, a nie pierwsze wchodzące
        }


        return true;
    }

    public boolean place(Animal animal) {
        Vector2d pos = animal.getPosition();
        if (moveAnimal(pos, animal)) {
            animal.addObserver(this);
            return true;
        }
        return false;
    }


    public Animal[] twoBestAnimalsAt(Vector2d position) {
        ArrayList<Animal> list = animals.get(position);
        Animal best;
        Animal second;
        if (list != null && list.size() >= 2) {
            if (list.get(0).getEnergy() > list.get(1).getEnergy()) {
                best = list.get(0);
                second = list.get(1);
            } else {
                best = list.get(1);
                second = list.get(0);
            }
            for (int i = 2; i < list.size(); i++) {
                Animal el = list.get(i);
                if (el.getEnergy() >= best.getEnergy()) {
                    second = best;
                    best = el;
                } else if (el.getEnergy() >= second.getEnergy()) {
                    second = el;
                }
            }
            return new Animal[]{best, second};
        }
        return null;
    }

    public Object objectAt(Vector2d position) {
        if (animalsAt(position) != null) {
            return animals.get(position).get(0);
        }
        return plantAt(position);
    }

    public Plant plantAt(Vector2d position) {
        return plants.get(position);
    }

    private void spawnPlant(Vector2d position) {
        if (plantAt(position) == null) {
            plants.put(position, new Plant(position));
        }
    }

    public void removeAnimal(Animal animal) {
        animalsAt(animal.getPosition()).remove(animal);
        if (animalsAt(animal.getPosition()).size() == 0) {
            animals.remove(animal.getPosition());
        }
        //least dead map type
        if (params.plantGrowthType == 1) { // 1? if nie jest najlepszym rozwiązaniem
            addCorpseAt(animal.getPosition());
            biasCorpseless();
        }

    }

    public void spawnNPlants(int n) { // czemu N?
        int biasedSpawns = (int) (n * 0.8);

        // spawn 80% of the plants on biased positions
        int unsuccessful = 0;
        int i = 0;
        while (i < biasedSpawns) {
            int index = rng.nextInt(biasedPositions.size());
            Vector2d newPosition = biasedPositions.get(index);
            if (plantAt(newPosition) == null) {
                Plant p = new Plant(newPosition);
                plants.put(newPosition, p);
                i++;
            } else unsuccessful += 1;
            if (unsuccessful > 100) break;
        }
        // spawn the rest elsewhere
        unsuccessful = 0;
        while (i < n) {
            Vector2d newPosition = new Vector2d(rng.nextInt((int) width), rng.nextInt((int) height));
            if (plantAt(newPosition) == null && !biasedPositions.contains(newPosition)) {
                Plant p = new Plant(newPosition);
                plants.put(newPosition, p);
                i++;
            } else unsuccessful += 1;
            if (unsuccessful > 100) break;
        }

    }

    public void positionChanged(Animal animal, Vector2d newPosition) {
        Vector2d oldPosition = animal.getPosition();
        animalsAt(oldPosition).remove(animal);
        if (animalsAt(oldPosition).size() == 0) {
            animals.remove(oldPosition);
        }
        moveAnimal(newPosition, animal);
    }

    //sets the middle 20% of the map as biased for plant growth
    public void biasEquator() {
        int totalArea = width * height;
        double equatorArea = totalArea * 0.2;
        double equatorRadius = equatorArea / (width * 2);
        double equatorTop = (height / 2) + equatorRadius;
        double equatorBottom = (height / 2) - equatorRadius;

        for (int i = 0; i < height; i++) {
            if (i >= equatorBottom && i < equatorTop) {
                for (int j = 0; j < width; j++) {
                    biasedPositions.add(new Vector2d(j, i));
                }
            }
        }
    }


    public void addCorpseAt(Vector2d position) {
        squares[position.y * width + position.x].addDead();
        Arrays.sort(squares, Comparator.comparing(MapSquare::getDead));

    }


    public void biasCorpseless() {
        biasedPositions.clear();
        int totalArea = width * height;
        double biasedArea = totalArea * 0.2;
        for (int i = 0; i < biasedArea; i++) {
            biasedPositions.add(squares[i].getPosition());
        }
    }


}
