package evolution;

//import javafx.application.Platform;

import evolution.gui.App;
import evolution.gui.SimulationInstance;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class SimulationEngine implements Runnable {
    private AbstractWorldMap worldMap;
    private SimulationInstance app;
    private Parameters params;
    private int days = 0;
    private List<Animal> animals = new ArrayList<>();
    private List<Animal> deadAnimals = new ArrayList<>();

    public SimulationEngine(AbstractWorldMap map,Parameters params, SimulationInstance app){
        this.app=app;
        this.worldMap=map;
        this.params=params;
    }


    public int getTotalAnimals(){
        return animals.size();
    }
    public int getDays(){
        return days;
    }

    public int getAverageEnergy(){

        if(animals.size()==0) return -1;
        int sum = 0;
        for(Animal a : animals){
            sum+=a.getEnergy();
        }
        return sum/animals.size();
    }

    public  int getAverageLifespan(){
        if(deadAnimals.size()==0) return -1;
        int sum = 0;
        for(Animal a : deadAnimals){
            sum+=a.getDaysAlive();
        }
        return sum/deadAnimals.size();

    }


    public void initialize(){
        for (int i = 0; i < params.startAnimals; i++){
            Animal newAnimal = new Animal(worldMap, params);
            worldMap.place(newAnimal);
            animals.add(newAnimal);
        }
    }


    public void nextDay(){
        days++;
        clearDead();
        moveAnimals();
        breedAnimals();
        spawnPlants();
    }

    private void clearDead(){
        ArrayList<Animal> toDelete = new ArrayList<>();
        for(Animal a : animals){
            if (a.getEnergy()<=0){
                toDelete.add(a);
            }
        }
        for(Animal a: toDelete){
            animals.remove(a);
            deadAnimals.add(a);
            a.unalive(days);
            worldMap.removeAnimal(a);
        }

    }

    private void moveAnimals(){
        for (int i=0; i<animals.size(); i++) {

            animals.get(i%animals.size()).move();
        }
        for(Animal a : animals){
            a.lowerEnergy(1);
            a.incrementDaysAlive();
        }

    }



    private void breedAnimals(){
        ArrayList<Animal> newAnimals = new ArrayList<>();
        for(Animal a : animals){
            Animal[] toBreed = worldMap.twoBestAnimalsAt(a.getPosition());
            if(toBreed!=null&& a==toBreed[0]&&
                    toBreed[0].getEnergy()>=params.saturationEnergy&&toBreed[1].getEnergy()>=params.saturationEnergy){
                Animal newAnimal = toBreed[0].breed(toBreed[1]);

                newAnimals.add(newAnimal);

            }
        }
        for(Animal a : newAnimals){
            animals.add(a);
            worldMap.place(a);
        }
    }

    private void spawnPlants(){
        worldMap.spawnNPlants(params.plantGrowth);

    }




    public void run(){
        int delay=500;
        try {
            Platform.runLater(() -> {
                this.app.render();
            });
            Thread.sleep(delay);

            for (int i=0; i<10000; i++){
                nextDay();
                Platform.runLater(() -> {
                    this.app.render();
                });
                Thread.sleep(delay);

            }

            Thread.sleep(delay);
        } catch (InterruptedException e){
            System.out.println(e.getMessage());
        }
    }
}
