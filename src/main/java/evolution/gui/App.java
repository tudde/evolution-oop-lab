package evolution.gui;

import evolution.*;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class App extends Application {


    private ArrayList<SimulationInstance> simulations = new ArrayList<>();
    private AbstractWorldMap map;
    private Scene scene;
    private VBox container = new VBox();


    public void start(Stage primaryStage) {


        primaryStage.show();
        scene = new Scene(container, 600, 1000);
        primaryStage.setScene(scene);
        Label defaults = new Label("(leave fields empty for default values)");
        container.getChildren().add(defaults);

        //add all simulation options

        TextField mapHeight = new TextField();
        addTextInput(new Label("Map height"), mapHeight);

        TextField mapWidth = new TextField();
        addTextInput(new Label("Map width"), mapWidth);

        TextField startPlants = new TextField();
        addTextInput(new Label("Start plants"), startPlants);

        TextField plantEnergy = new TextField();
        addTextInput(new Label("Energy per plant eaten"), plantEnergy);

        TextField plantGrowth = new TextField();
        addTextInput(new Label("Plant growth per day"), plantGrowth);

        TextField startAnimals = new TextField();
        addTextInput(new Label("Start animals"), startAnimals);

        TextField saturationEnergy = new TextField();
        addTextInput(new Label("Minimum energy to breed"), saturationEnergy);

        TextField breedEnergy = new TextField();
        addTextInput(new Label("Energy used by breeding"), breedEnergy);

        TextField minMutations = new TextField();
        addTextInput(new Label("Minimum number of mutations in child animal's genome"), minMutations);

        TextField maxMutations = new TextField();
        addTextInput(new Label("Maximum number of mutations in child animal's genome"), maxMutations);

        TextField genomeLength = new TextField();
        addTextInput(new Label("Length of an animal's genome"), genomeLength);


        //map type
        Label mapLabel = new Label("Map type:");
        container.getChildren().add(mapLabel);
        ToggleGroup mapType = new ToggleGroup();
        RadioButton globe = new RadioButton("Globe");
        globe.setToggleGroup(mapType);
        globe.setSelected(true);
        container.getChildren().add(globe);
        RadioButton portal = new RadioButton("Portal");
        portal.setToggleGroup(mapType);
        container.getChildren().add(portal);

        //plant growth type
        Label plantLabel = new Label("Plant growth type:");
        container.getChildren().add(plantLabel);
        ToggleGroup plantType = new ToggleGroup();
        RadioButton equator = new RadioButton("Favor equator squares");
        equator.setToggleGroup(plantType);
        equator.setSelected(true);
        container.getChildren().add(equator);
        RadioButton dead = new RadioButton("Favor least died on squares");
        dead.setToggleGroup(plantType);
        container.getChildren().add(dead);

        //mutation type
        Label mutationLabel = new Label("Mutation type:");
        container.getChildren().add(mutationLabel);
        ToggleGroup mutationType = new ToggleGroup();
        RadioButton random = new RadioButton("Each mutation completely random");
        random.setToggleGroup(mutationType);
        random.setSelected(true);
        container.getChildren().add(random);
        RadioButton shift = new RadioButton("Genes mutate either one up or one down");
        shift.setToggleGroup(mutationType);
        container.getChildren().add(shift);

        //gene relisation type
        Label geneLabel = new Label("Gene realisation type:");
        container.getChildren().add(geneLabel);
        ToggleGroup geneType = new ToggleGroup();
        RadioButton sequential = new RadioButton("Sequential gene realisation");
        sequential.setToggleGroup(geneType);
        sequential.setSelected(true);
        container.getChildren().add(sequential);
        RadioButton partRandom = new RadioButton("20% chance of activating a random gene");
        partRandom.setToggleGroup(geneType);
        container.getChildren().add(partRandom);


        Button button = new Button("Start simulation");
        container.getChildren().add(button);


        button.setOnAction((event) -> {
            try {
                evolution.Parameters params = new evolution.Parameters(
                        !mapHeight.getText().equals("") ? Integer.parseInt(mapHeight.getText()) : 10,
                        !mapWidth.getText().equals("") ? Integer.parseInt(mapWidth.getText()) : 10,
                        !startPlants.getText().equals("") ? Integer.parseInt(startPlants.getText()) : 10,
                        !plantEnergy.getText().equals("") ? Integer.parseInt(plantEnergy.getText()) : 10,
                        !plantGrowth.getText().equals("") ? Integer.parseInt(plantGrowth.getText()) : 10,
                        !startAnimals.getText().equals("") ? Integer.parseInt(startAnimals.getText()) : 10,
                        !saturationEnergy.getText().equals("") ? Integer.parseInt(saturationEnergy.getText()) : 25,
                        !breedEnergy.getText().equals("") ? Integer.parseInt(breedEnergy.getText()) : 10,
                        !minMutations.getText().equals("") ? Integer.parseInt(minMutations.getText()) : 1,
                        !maxMutations.getText().equals("") ? Integer.parseInt(maxMutations.getText()) : 3,
                        !genomeLength.getText().equals("") ? Integer.parseInt(genomeLength.getText()) : 10,
                        globe.isSelected() ? 0 : 1,
                        equator.isSelected() ? 0 : 1,
                        random.isSelected() ? 0 : 1,
                        sequential.isSelected() ? 0 : 1);
                startNewSimulation(params);
            } catch (Exception NumberFormatException) {
                System.out.println("Wrong input format");
            }

        });


    }

    private void startNewSimulation(evolution.Parameters params) {
        SimulationInstance newSimulation = new SimulationInstance(params);
        simulations.add(newSimulation);
    }

    private void addTextInput(Label label, TextField textField) {
        container.getChildren().add(label);
        container.getChildren().add(textField);
    }


}