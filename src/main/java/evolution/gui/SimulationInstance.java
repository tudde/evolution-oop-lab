package evolution.gui;

import evolution.*;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class SimulationInstance {
    private final AbstractWorldMap map;
    private final SimulationEngine engine;
    private GridPane grid = new GridPane();
    private Scene scene = new Scene(grid);
    private Stage stage = new Stage();
    private evolution.Parameters params;
    private boolean running = false;
    private Animal followed= null;
    private VBox selectedAnimalStats = new VBox();
    private VBox simulationStats = new VBox();
    private int squareSize;

    public SimulationInstance(Parameters params){

        this.params=params;
        squareSize = 800/(Math.max(params.mapHeight, params.mapWidth));
        if(params.mapType==0){
            this.map = new GlobeMap(params);
        }
        else this.map = new PortalMap(params);
        engine = new SimulationEngine(map,params,this);
        engine.initialize();


        HBox container = new HBox();
        HBox allStats = new HBox();
        VBox simulation = new VBox();
        Button button = new Button("Pause/Resume");



        simulation.getChildren().add(grid);
        simulation.getChildren().add(button);

        container.getChildren().add(simulation);

        Separator line = new Separator();
        line.setOrientation(Orientation.VERTICAL);

        allStats.getChildren().addAll(simulationStats,line,selectedAnimalStats);
        container.getChildren().add(allStats);

        scene = new Scene(container, 1300, 950);
        stage.setScene(scene);


        Thread engineThread = new Thread(engine);
        engineThread.start();
        running = true;

        stage.setOnCloseRequest(event -> {
            System.out.println("Simulation closing");
            engineThread.stop();
        });

        grid.setOnMouseClicked((javafx.scene.input.MouseEvent event) -> {
                    clickGrid(event);
                });



        button.setOnAction((event) -> {
            if(running){
                engineThread.suspend();
                running=false;

            }
            else{
                engineThread.resume();
                running=true;

            }
        });

        stage.show();




    }


    public void clickGrid(MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (clickedNode != grid) {
            Node parent = clickedNode.getParent();
            while (parent != grid) {
                clickedNode = parent;
                parent = clickedNode.getParent();
            }
       
            int xCoord = GridPane.getColumnIndex(clickedNode)-1;
            int yCoord = params.mapHeight-GridPane.getRowIndex(clickedNode);

            if(map.animalsAt(new Vector2d(xCoord,yCoord))!=null){
                followed = map.animalsAt(new Vector2d(xCoord,yCoord)).get(0);
                displayStats();
            }
        }
    }

    public void render(){
        grid.getChildren().clear();
        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();
        grid.setGridLinesVisible(false);
        displayStats();
        drawGrid();
    }
    private void displayStats(){
        simulationStats.getChildren().clear();

        Label days = new Label("Days passed: "+engine.getDays());
        simulationStats.getChildren().add(days);

        Label animals = new Label("Total animals: "+engine.getTotalAnimals());
        simulationStats.getChildren().add(animals);

        Label plants = new Label("Total plants: "+map.getTotalPlants());
        simulationStats.getChildren().add(plants);

        Label avgEnergy = new Label("Average energy : "+engine.getAverageEnergy());
        simulationStats.getChildren().add(avgEnergy);

        Label avgLifespan = new Label("Average lifespan of dead animals : "+engine.getAverageLifespan());
        simulationStats.getChildren().add(avgLifespan);


        String type="";
        if (params.mapType==0) type="Globe";
        else type="Portal";
        Label mapType = new Label("Map type: " +type);
        simulationStats.getChildren().add(mapType);

        if (params.plantGrowthType==0) type="Equator";
        else type="Least corpses";
        Label plantType = new Label("Plant growth type: " +type);
        simulationStats.getChildren().add(plantType);

        if (params.geneRealisationType==0) type="Sequential";
        else type="Partially random";
        Label geneType = new Label("Gene realisation type: " +type);
        simulationStats.getChildren().add(geneType);





        selectedAnimalStats.getChildren().clear();
        if(followed!=null){
            Label selectedAnimal = new Label("Selected animal stats: ");
            selectedAnimalStats.getChildren().add(selectedAnimal);
            Label lived = new Label("Lived: "+ followed.getDaysAlive() + " days");
            selectedAnimalStats.getChildren().add(lived);
            Label energy = new Label("Energy: "+ followed.getEnergy());
            selectedAnimalStats.getChildren().add(energy);
            Label children = new Label("Children: "+ followed.getChildren());
            selectedAnimalStats.getChildren().add(children);

            String genomeStr = "";
            int[] genomeArr= followed.getGenome();
            int currentGene= followed.getCurrentGene();
            for(int i = 0;i < genomeArr.length;i++){
                if (i == currentGene) genomeStr += "<"+genomeArr[i]+"> ";
                else genomeStr+=genomeArr[i]+" ";
            }

            Label genome = new Label("Genome: "+ genomeStr);
            selectedAnimalStats.getChildren().add(genome);
            Label plantsEaten = new Label("Plants eaten: "+ followed.getPlantsEaten());
            selectedAnimalStats.getChildren().add(plantsEaten);

            Label status = new Label("Status : "+ followed.getStatus());
            selectedAnimalStats.getChildren().add(status);

            if(followed.getStatus()=="Dead"){
                Label diedOn = new Label("Died on day "+followed.getDayDied());
                selectedAnimalStats.getChildren().add(diedOn);
            }


        }
        else{
            Label selectedAnimal = new Label("No selected animal");
            selectedAnimalStats.getChildren().add(selectedAnimal);
        }



    }

    private void drawGrid(){
        grid.setGridLinesVisible(true);
        grid.setAlignment(Pos.CENTER);

        Vector2d lowerLeft = new Vector2d(0,0);
        Vector2d upperRight= new Vector2d(params.mapWidth-1,params.mapHeight-1);



        javafx.scene.control.Label yxLabel = new javafx.scene.control.Label("y/x");
        grid.add(yxLabel, 0, 0, 1, 1);
        GridPane.setHalignment(yxLabel, HPos.CENTER);

        for(int i = upperRight.y; i>= lowerLeft.y-1; i--){
            grid.getRowConstraints().add(new RowConstraints(squareSize));
        }
        for(int i = lowerLeft.x; i<=upperRight.x+1; i++){
            grid.getColumnConstraints().add(new ColumnConstraints(squareSize));
        }
        for(int i = upperRight.y; i>= lowerLeft.y; i--){
            javafx.scene.control.Label yLabel = new javafx.scene.control.Label(Integer.toString(i));
            grid.add(yLabel, 0, upperRight.y + 1 - i, 1, 1);
            GridPane.setHalignment(yLabel, HPos.CENTER);
        }
        for(int i = lowerLeft.x; i<=upperRight.x; i++){
            javafx.scene.control.Label xLabel = new Label(Integer.toString(i));
            grid.add(xLabel, i + 1 - lowerLeft.x,0, 1, 1);
            GridPane.setHalignment(xLabel, HPos.CENTER);
        }

        for(int i = lowerLeft.x; i<=upperRight.x; i++){
            for(int j = upperRight.y; j>= lowerLeft.y; j--){
                //if(map.isOccupied(new Vector2d(i, j))) {
                    VBox vbox = new GuiElementBox(map,new Vector2d(i, j),squareSize).getVBox();
                    grid.add(vbox, 1 + i - lowerLeft.x, 1 + upperRight.y - j, 1, 1);
                    GridPane.setHalignment(vbox, HPos.CENTER);

               // }

            }
        }

    }
}
