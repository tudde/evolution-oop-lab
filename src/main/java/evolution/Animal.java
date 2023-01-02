package evolution;
import java.util.*;

public class Animal implements IMapElement{


    //random genome and position constructor
    public Animal(AbstractWorldMap worldMap, Parameters params){
        this.rng = new Random();
        this.genome= new int[params.genomeLength];
        for(int i=0;i<params.genomeLength;i++){
            genome[i]=rng.nextInt(8);
        }
        this.position=new Vector2d(rng.nextInt( params.mapWidth), rng.nextInt(params.mapHeight));
        this.direction=MapDirection.RandomDirection();
        this.worldMap=worldMap;
        this.params=params;
    }

    //set genome and position constructor
    public Animal(AbstractWorldMap worldMap, Parameters params, int[] genome, Vector2d position){
        this.rng = new Random();
        this.genome=genome;
        this.position=position;
        this.direction=MapDirection.RandomDirection();
        this.worldMap=worldMap;
        this.params=params;
        this.energy= 2*params.breedEnergy;
    }

    private final AbstractWorldMap worldMap;
    private Vector2d position;
    private MapDirection direction;
    private final int[] genome;
    private int energy = 10;
    private int plantsEaten = 0;
    private int currentGene = 0;
    private int daysAlive = 0;
    private int children = 0;
    private int dayDied = -1;
    private String status = "Alive";
    private ArrayList<IPositionChangeObserver> observers = new ArrayList<>();
    private final Parameters params;
    private static Random rng;

    public void move(){
        direction = direction.rotate(genome[currentGene]);
        currentGene+=1;

        //if random gene realisation there is a 20% chance of random gene activating
        if(params.geneRealisationType==1&&rng.nextInt(100)<20){
            currentGene=rng.nextInt(params.genomeLength);
        }

        if (currentGene>=params.genomeLength) currentGene = 0;

        Vector2d newPosition = direction.toUnitVector().add(position);
        Vector2d correctedPosition = worldMap.correctPosition(newPosition, this);
        if (correctedPosition!=null){
            notifyPositionObservers(correctedPosition);
            this.position= correctedPosition;
        }
    }

    public void eatPlant(){
        energy+= params.plantEnergy;
        if(energy>100) energy=100;
        plantsEaten+=1;
    }

    public int getEnergy(){
        return energy;
    }

    public int getCurrentGene(){
        return currentGene;
    }

    public void lowerEnergy(int val){
        energy-=val;
    }

    public void incrementDaysAlive(){
        daysAlive+=1;
    }

    public int getDaysAlive(){
        return daysAlive;
    }

    public void incrementChildren(){
        children+=1;
    }
    public int getChildren(){
        return children;
    }

    public int[] getGenome(){
        return genome;
    }

    public int getPlantsEaten(){
        return plantsEaten;
    }
    public void unalive(int day){
        status="Dead";
        dayDied=day;
    }

    public int getDayDied(){
        return dayDied;
    }

    public String getStatus(){
        return status;
    }





    public Animal breed(Animal other){
        double ratio = (double) other.energy/(double)this.energy;
        int genesFromThis = (int)(ratio*params.genomeLength);
        int genesFromOther = params.genomeLength-genesFromThis;

        int[] newGenome = new int[params.genomeLength];

        int side = rng.nextInt(2);
        int j = 0;
        if (side == 0){
            for(int i=0;i<genesFromThis;i++){
                newGenome[j]=this.genome[i];
                j++;
            }
            for(int i=genesFromThis;i<params.genomeLength;i++){
                newGenome[j]=other.genome[i];
                j++;
            }
        }
        else{
            for(int i=0;i<genesFromOther;i++){
                newGenome[j]=other.genome[i];
                j++;
            }
            for(int i=genesFromOther;i<params.genomeLength;i++){
                newGenome[j]=this.genome[i];
                j++;
            }
        }

        int numberOfMutations=rng.nextInt(params.minMutations,params.maxMutations+1);

        for(int i=0;i<numberOfMutations;i++){
            int mutationIndex = rng.nextInt(params.genomeLength);

            //mutacja o 1 w gore lub w dol
            if(params.mutationType==1){
                int mutationDirection=rng.nextInt(2);
                if (mutationDirection == 0) {
                    if (newGenome[mutationIndex] == params.genomeLength -1 ) newGenome[mutationIndex]=0;
                    else newGenome[mutationIndex] = newGenome[mutationIndex] + 1;
                }
                else {
                    if (newGenome[mutationIndex] == 0 ) newGenome[mutationIndex]=params.genomeLength-1;
                    else newGenome[mutationIndex]=newGenome[mutationIndex]-1;
                }
            }
            //losowa mutacja
            else{
                int mutedGene=rng.nextInt(8);
                newGenome[mutationIndex]=mutedGene;
            }

        }

        this.energy-= params.breedEnergy;
        other.energy-= params.breedEnergy;
        this.incrementChildren();
        other.incrementChildren();

        return new Animal(worldMap,params,newGenome,position);
    }

    public void addObserver(IPositionChangeObserver o){
        observers.add(o);
    }

    private void notifyPositionObservers(Vector2d newPosition){
        for(IPositionChangeObserver o : observers){
            o.positionChanged(this, newPosition);
        }
    }



    @Override
    public String toString() {
        return position.toString()+" "+direction.toString()+" "+energy;

    }








    public Vector2d getPosition() {
        return position;
    }
}
