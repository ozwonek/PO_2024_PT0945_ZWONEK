package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.Statistics;
import agh.ics.oop.model.*;
//import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.Config;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import static agh.ics.oop.OptionsParser.parse;

public class SimulationPresenter implements MapChangeListener {
    private Globe map;
    private Animal animal;

    @FXML
    private VBox mapDisplay;
    @FXML
    private VBox settingConfigurations;
    @FXML
    private VBox dailyStats;
    @FXML
    private VBox configurationForm;
    @FXML
    private ComboBox<String> configurations;
    @FXML
    private Label widthLabel;
    @FXML
    private TextField widthTextField;
    @FXML
    private Label heightLabel;
    @FXML
    private TextField heightTextField;
    @FXML
    private Label startEnergyLabel;
    @FXML
    private TextField startEnergyTextField;
    @FXML
    private TextField startGrassNumberTextField;
    @FXML
    private Label startGrassNumberLabel;
    @FXML
    private TextField energyFromGrassTextField;
    @FXML
    private Label energyFromGrassLabel;
    @FXML
    private TextField grassesPerDayTextField;
    @FXML
    private Label grassesPerDayLabel;
    @FXML
    private TextField genesLengthTextField;
    @FXML
    private TextField grassStart;
    @FXML
    private GridPane mapGrid;
    @FXML
    private GridPane statisticsGrid;
    @FXML
    private Label dayCount;
    @FXML
    private Label simulationDayLabel;
    @FXML
    private GridPane animalGrid;
    @FXML
    private TextField grassStartTextField;
    @FXML
    private TextField animalStartTextField;
    @FXML
    private TextField energyToReproduceTextField;
    @FXML
    private TextField energyToChildTextField;
    @FXML
    private TextField energyToReproduce;

    private final HashMap<String,Config> savedConfigurations = new HashMap<>();

    private final ArrayList<Integer> currentConfiguration = new ArrayList<>();

    private TextField mapWidthTextField;
    private TextField mapHeigthTextField;
    private final static int MAP_HEIGHT = 450;
    private final static int MAP_WIGHT = 450;

    private final static int STATS_WIDTH = 200;
    private final static int STATS_HEIGHT = 600;
    private int simulationDay = 0;


    public void setWorldMap(Globe map){
        this.map = map;
    }

    private void setAnimal(Animal animal){
        this.animal = animal;
    }
//    private Boundary boundary = map.getCurrentBounds();



    private void actualiseStatistics(){
        clearGrid(statisticsGrid);
        Statistics statistics= map.getStats();
        Label label = new Label("liczba zwierzaków: " + statistics.getAnimalCount());
        statisticsGrid.add(label,0,0);
        label = new Label("liczba roślin: " + statistics.getGrassCount());
        statisticsGrid.add(label,1,0);
        label = new Label("liczba wolnych pól: " + statistics.getFreeSpots());
        statisticsGrid.add(label, 0,1);
        label = new Label("najpopularniejszy genotyp: " + statistics.getMostPopularGenom());
        statisticsGrid.add(label, 1, 1);
        label = new Label("średniej długości życia zwierzaków: " + statistics.getMeanLifeForLiving());
        statisticsGrid.add(label,0,2);
        label = new Label("średniej liczby dzieci dla żyjących zwierzaków: " +statistics.getMeanChildrenCount());
        statisticsGrid.add(label,1,2);
        label = new Label("średniej liczba długość życia nieżyjącego: " +statistics.getMeanLifeForDead());
        statisticsGrid.add(label,0,3);
        label = new Label("Średnia energia: " +statistics.getMeanEnergy());
        statisticsGrid.add(label,1,3);
        }


    private void drawMap(){

        clearGrid(mapGrid); // czyszczenie
//        Boundary boundary = map.getCurrentBounds();
        int mapWidth = map.getWidth(); //szerokość
        int mapHeight = map.getHeight(); // wysokość
        int width= MAP_WIGHT/mapWidth;
        int height = MAP_HEIGHT/mapHeight;
        int squareSize = Math.min(height, width); //ile na jeden kwadracik
        mapGrid.getColumnConstraints().add(new ColumnConstraints(width));
        mapGrid.getRowConstraints().add(new RowConstraints(height));
        Label label = new Label("y/x");
        mapGrid.add(label, 0, 0);
        GridPane.setHalignment(label, HPos.CENTER);
        for(int i=0; i<mapWidth; i++){
            label = new Label(Integer.toString(i));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.getColumnConstraints().add(new ColumnConstraints(squareSize));
            mapGrid.add(label, i+1, 0);
        }
        for(int i=0; i<mapHeight; i++){
            label = new Label(Integer.toString(mapHeight-i-1));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.getRowConstraints().add(new RowConstraints(squareSize));
            mapGrid.add(label, 0, i+1);
        }
        for(int i =0;i<mapWidth;i++) // dodawanie na każdej pozycji i,j objektu,jeżeli istnieje
        {
            for (int j = 0; j < mapHeight; j++) {
                Vector2d pos = new Vector2d(i, j);
                if (map.isAnimal(pos)) {
                    Label animalLabel=new Label(map.objectAt(pos).toString());
                    mapGrid.add(animalLabel , i + 1, mapHeight - j);
                    animalLabel.setOnMouseClicked(event -> {
                        System.out.println("Kliknięto na komórkę: " + pos);
                        setAnimal(map.objectAt(pos));
                    });
                }
                else if (map.isGrass(pos)) {
                    Label grassLabel = new Label(" * ");
                    grassLabel.setStyle("-fx-background-color: #499d49; -fx-text-fill: white;"); // Zielone tło, biały tekst
                    grassLabel.setPrefSize(width, height); // Ustaw rozmiar komórki (opcjonalne)

                    // Dodajemy etykietę do mapGrid w odpowiedniej pozycji
                    mapGrid.add(grassLabel, i + 1, mapHeight - j);
                }
                else {
                    mapGrid.add(new Label(" "), i + 1, mapHeight - j);
                }
                GridPane.setHalignment(mapGrid.getChildren().getLast(), HPos.CENTER);
            }
        }



    }

    @FXML
    private void onAnimalClicked(Animal animal){
        clearGrid(animalGrid);

        Label label = new Label("genotyp zwierzka: " + animal.getGenomes());
        animalGrid.add(label,0,0);
        label = new Label("aktywny gen: " + animal.getGenomes().get(animal.getActive()));
        animalGrid.add(label,1,0);
        label = new Label("ilość energii: " + animal.getEnergy());
        animalGrid.add(label, 0,1);
        label = new Label("ilość zjedzonych roślin: " + animal.getEatenGrass());
        animalGrid.add(label, 1, 1);
        label = new Label("ilość dzieci: " + animal.getChildrenSize());
        animalGrid.add(label,0,2);
        label = new Label("ile dni żyje: " + animal.getAge() );
        animalGrid.add(label,1,2);
        label = new Label("ilość potomków: " + animal.getOffspringCount());
        animalGrid.add(label,0,3);
        label = new Label("dzień śmierci: "+simulationDay);
        animalGrid.add(label,1,3);
    }


    @FXML
    private void showNewConfigurationForm(){
        configurationForm.setVisible(true);
    }

    @FXML
    private void addNewConfiguration(){
        int mapHeight = Integer.parseInt(heightTextField.getText());
        int mapWidth = Integer.parseInt(heightTextField.getText());
        int grassStart= Integer.parseInt(grassStartTextField.getText());
        int grassDaily = Integer.parseInt(grassesPerDayTextField.getText());
        int grassEnergy = Integer.parseInt(energyFromGrassTextField.getText());
        int animalStart = Integer.parseInt(animalStartTextField.getText());
        int animalStartEnergy =  Integer.parseInt(startEnergyTextField.getText());
        int animalEnergyToReproduce = Integer.parseInt(energyToReproduceTextField.getText());
        int animalEnergyToChild = Integer.parseInt(energyToChildTextField.getText());
        int animalGenotypeLength = Integer.parseInt(genesLengthTextField.getText());
        String newConfigurationText =
                "szerokość: " + mapWidth+
                "wysokość: " + mapHeight +
                "początkowa energia: " + animalStartEnergy +
                "początkowa ilość trawy: " + grassStart +
                "energia z trawy: " + grassEnergy +
                "ilość trawy wyrastającej każdego dnia: " + grassDaily +
                "długość genotypu: " + animalGenotypeLength;

        Config worldConfig =  new Config(mapHeight,mapWidth,grassStart,grassDaily,grassEnergy,animalStart,animalStartEnergy,animalEnergyToReproduce,animalEnergyToChild,animalGenotypeLength);
        savedConfigurations.put(newConfigurationText,worldConfig);
        configurations.getItems().add(newConfigurationText);
        configurationForm.setVisible(false);
    }
    


    @FXML
    private Config getConfiguration(){
        String selectedConfiguration = configurations.getSelectionModel().getSelectedItem();
        return savedConfigurations.get(selectedConfiguration);
    }

    @Override
    public void mapChanged(Globe worldMap, String message){
        setWorldMap(worldMap);
        Platform.runLater(() -> {
            this.simulationDay+=1;
            drawMap();
            actualiseStatistics();
            if(this.animal!=null){
                onAnimalClicked(animal);
            }

        });

    }


    public void onSimulationStartClicked(){

        dailyStats.setVisible(true);
        settingConfigurations.setVisible(false);



        try {
            Config worldConfig = getConfiguration();
            Globe map = new Globe(worldConfig);
            map.addObserver(this);
            Simulation simulation = new Simulation(worldConfig,map);
            SimulationEngine engine = new SimulationEngine(List.of(simulation));
            new Thread(engine :: runAsync).start();
        }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }


    }
    private void clearGrid(GridPane grid) {
        grid.getChildren().retainAll(grid.getChildren().get(0)); // hack to retain visible grid lines
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
    }

}

