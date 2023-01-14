package evolution;

public class Parameters {

    public Parameters(int mapHeight, int mapWidth, int startPlants, int plantEnergy, int plantGrowth,
                      int startAnimals, int saturationEnergy, int breedEnergy, int minMutations,
                      int maxMutations, int genomeLength, int mapType, int plantGrowthType,
                      int mutationType, int geneRealisationType) {
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        this.startPlants = startPlants;
        this.plantEnergy = plantEnergy;
        this.plantGrowth = plantGrowth;
        this.startAnimals = startAnimals;
        this.saturationEnergy = saturationEnergy;
        this.breedEnergy = breedEnergy;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.genomeLength = genomeLength;
        this.mapType = mapType;
        this.plantGrowthType = plantGrowthType;
        this.mutationType = mutationType;
        this.geneRealisationType = geneRealisationType;

    }

    public final int mapHeight;
    public final int mapWidth;
    public final int startPlants;
    public final int plantEnergy;
    public final int plantGrowth;

    public final int startAnimals;
    public final int saturationEnergy;
    public final int breedEnergy;
    public final int minMutations;
    public final int maxMutations;
    public final int genomeLength;

    public final int mapType;  //0 = kula, 1 = portal
    public final int plantGrowthType; // 0 = równik, 1 = toksyczne trupy
    public final int mutationType; // 0 = losowe, 1 = przesunięcie
    public final int geneRealisationType;// 0 = predestynacja, 1 = losowe


}
