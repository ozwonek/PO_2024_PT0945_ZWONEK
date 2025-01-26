package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
//import agh.ics.oop.model.util.Boundary;
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
    private GridPane mapGrid;
    @FXML
    private GridPane statisticsGrid;

    private final HashMap<String,ArrayList<Integer>> savedConfigurations = new HashMap<>();

    private final ArrayList<Integer> currentConfiguration = new ArrayList<>();

    private TextField mapWidthTextField;
    private TextField mapHeigthTextField;
    private final static int MAP_HEIGHT = 450;
    private final static int MAP_WIGHT = 450;

    private final static int STATS_WIDTH = 200;
    private final static int STATS_HEIGHT = 600;


    public void setWorldMap(Globe map){
        this.map = map;
    }
//    private Boundary boundary = map.getCurrentBounds();



    private void actualiseStatistics(){
        clearGrid(statisticsGrid);


        Label label = new Label("liczba zwierzaków: " + map.getAnimalsSize());
        statisticsGrid.add(label,0,0);
        label = new Label("liczba roślin: ");
        statisticsGrid.add(label,1,0);
        label = new Label("liczba wolnych pól: ");
        statisticsGrid.add(label, 0,1);
        label = new Label("najpopularniejszy genotyp: ");
        statisticsGrid.add(label, 1, 1);
        label = new Label("średniej długości życia zwierzaków: " + map.getAverageAnimalAge());
        statisticsGrid.add(label,0,2);
        label = new Label("średniej liczby dzieci dla żyjących zwierzaków: " +map.getAnimalsChildrenCount());
        statisticsGrid.add(label,1,2);
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
                    mapGrid.add(new Label(map.objectAt(pos).toString()), i + 1, mapHeight - j);
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
    private void onAnimalClicked(){

    }

    @FXML
    private void showNewConfigurationForm(){
        configurationForm.setVisible(true);
    }

    @FXML
    private void addNewConfiguration(){

        int width = Integer.parseInt(widthTextField.getText());
        int height = Integer.parseInt(heightTextField.getText());
        int startEnergy = Integer.parseInt(startEnergyTextField.getText());
        int energyFromGrass = Integer.parseInt(energyFromGrassTextField.getText());
        int grassPerDay = Integer.parseInt(grassesPerDayTextField.getText());
        int genesLength = Integer.parseInt(genesLengthTextField.getText());

        String newConfigurationText =
                "szerokość: " + width+
                "wysokość: " + height +
                "początkowa energia: " + startEnergy +
                "początkowa ilość trawy: " + startEnergy +
                "energia z trawy: " + energyFromGrass +
                "ilość trawy wyrastającej każdego dnia: " + grassPerDay +
                "długość genotypu: " + genesLength;

        ArrayList<Integer> newConfigurationData = new ArrayList<>();

        newConfigurationData.add(width);
        newConfigurationData.add(height);
        newConfigurationData.add(startEnergy);
        newConfigurationData.add(energyFromGrass);
        newConfigurationData.add(grassPerDay);
        newConfigurationData.add(genesLength);


        savedConfigurations.put(newConfigurationText,newConfigurationData);
        configurations.getItems().add(newConfigurationText);
        configurationForm.setVisible(false);
    }

    @FXML
    private ArrayList<Integer> getConfiguration(){
        String selectedConfiguration = configurations.getSelectionModel().getSelectedItem();
        return savedConfigurations.get(selectedConfiguration);
    }

    @Override
    public void mapChanged(Globe worldMap, String message){
        setWorldMap(worldMap);
        Platform.runLater(() -> {
            drawMap();
            actualiseStatistics();
        });

    }


    public void onSimulationStartClicked(){

        dailyStats.setVisible(true);
        settingConfigurations.setVisible(false);



        int width = getConfiguration().get(0);
        System.out.println(width);
        int height = getConfiguration().get(1);
        System.out.println(height);
        int startEnergy = getConfiguration().get(2);
        System.out.println(startEnergy);
        int energyFromGrass = getConfiguration().get(3);
        System.out.println(energyFromGrass);
        int grassPerDay = getConfiguration().get(4);
        System.out.println(grassPerDay);
        int genesLength = getConfiguration().get(5);
        System.out.println(genesLength);

        int startGrassNumber = Integer.parseInt(startGrassNumberTextField.getText());

        try {
            List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,2), new Vector2d(1,3),new Vector2d(2,3),new Vector2d(4,5),new Vector2d(3,2));

            int minimumToBeFull = 4;
            int giveToChild = 10;


            Globe map = new Globe(startGrassNumber,grassPerDay, 0.9,energyFromGrass,width,height);
            map.addObserver(this);
            Simulation simulation = new Simulation(positions,map,startEnergy,genesLength,minimumToBeFull,giveToChild);
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

