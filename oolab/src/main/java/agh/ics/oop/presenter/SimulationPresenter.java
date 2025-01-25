package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationApp;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import agh.ics.oop.model.util.Boundary;
import com.sun.javafx.scene.control.IntegerField;
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
import javafx.stage.Stage;
import agh.ics.oop.model.AbstractWorldMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import static agh.ics.oop.OptionsParser.parse;

public class SimulationPresenter implements MapChangeListener {
    private WorldMap map;

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

    private final HashMap<String,ArrayList<Integer>> savedConfigurations = new HashMap<>();

    private final ArrayList<Integer> currentConfiguration = new ArrayList<>();

    private TextField mapWidthTextField;
    private TextField mapHeigthTextField;
    private final static int MAP_HEIGHT = 450;
    private final static int MAP_WIGHT = 450;


    public void setWorldMap(WorldMap map){
        this.map = map;
    }
//    private Boundary boundary = map.getCurrentBounds();


    @FXML
    private GridPane mapGrid;
    private void drawMap(){

        clearGrid(); // czyszczenie
        Boundary boundary = map.getCurrentBounds();
        int mapWidth = boundary.topRightCorner().getX() - boundary.bottomLeftCorner().getX() + 1; //szerokość
        int mapHeight = boundary.topRightCorner().getY() - boundary.bottomLeftCorner().getY() + 1; // wysokość
        int width= MAP_WIGHT/mapWidth;
        int height = MAP_HEIGHT/mapHeight;
        int squareSize = Math.min(height, width); //ile na jeden kwadracik
        mapGrid.getColumnConstraints().add(new ColumnConstraints(width));
        mapGrid.getRowConstraints().add(new RowConstraints(height));
        Label label = new Label("y/x");
        mapGrid.add(label, 0, 0);
//        GridPane.setHalignment(label, HPos.CENTER);
        for(int i=0; i<mapWidth; i++){
            label = new Label(Integer.toString(i+boundary.bottomLeftCorner().getX()));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.getColumnConstraints().add(new ColumnConstraints(squareSize));
            mapGrid.add(label, i+1, 0);
        }
        for(int i=0; i<mapHeight; i++){
            label = new Label(Integer.toString(boundary.topRightCorner().getY()-i));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.getRowConstraints().add(new RowConstraints(squareSize));
            mapGrid.add(label, 0, i+1);
        }
        for(int i = boundary.bottomLeftCorner().getX();i<=boundary.topRightCorner().getX();i++) // dodawanie na każdej pozycji i,j objektu,jeżeli istnieje
        {
            for(int j = boundary.bottomLeftCorner().getY();j<=boundary.topRightCorner().getY();j++)
            {
                Vector2d pos = new Vector2d(i,j);
                if (map.isOccupied(pos)) {
                    if(map.objectAt(pos) instanceof Grass){
                        Label grassLabel = new Label("");
                        grassLabel.setStyle("-fx-background-color: #499d49; -fx-text-fill: white;"); // Zielone tło, biały tekst
                        grassLabel.setPrefSize(width, height); // Ustaw rozmiar komórki (opcjonalne)

                        // Dodajemy etykietę do mapGrid w odpowiedniej pozycji
                        mapGrid.add(grassLabel,
                                i - boundary.bottomLeftCorner().getX() + 1,
                                boundary.topRightCorner().getY() - j + 1);
                    }
                    mapGrid.add(new Label(map.objectAt(pos).toString()), i - boundary.bottomLeftCorner().getX() + 1, boundary.topRightCorner().getY() - j + 1);
                }
                else {
                    mapGrid.add(new Label(" "), i - boundary.bottomLeftCorner().getX() + 1, boundary.topRightCorner().getY() - j + 1);
                }
                GridPane.setHalignment(mapGrid.getChildren().getLast(), HPos.CENTER);
            }
        }



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
        int energyFromGrass = Integer.parseInt(grassesPerDayTextField.getText());
        int grassPerDay = Integer.parseInt(energyFromGrassTextField.getText());
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
    public void mapChanged(WorldMap worldMap, String message){
        setWorldMap(worldMap);
        Platform.runLater(() -> {
            drawMap();
//            movesDescriptionLabel.setText(message);
        });

    }

    public void onSimulationStartClicked(){
        int width = getConfiguration().get(0);
        int height = getConfiguration().get(1);
        int startEnegry = getConfiguration().get(2);
        int energyFromGrass = getConfiguration().get(3);
        int grassPerDay = getConfiguration().get(4);
        int genesLength = getConfiguration().get(5);

        int startGrassNumber = Integer.parseInt(startGrassNumberTextField.getText());

        try {
            List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,2), new Vector2d(1,3),new Vector2d(2,3),new Vector2d(4,5),new Vector2d(3,2));

            int minimumToBeFull = 4;
            int giveToChild = 10;


            GrassField map = new GrassField(startGrassNumber,grassPerDay, 0.9,energyFromGrass,width,height);
            map.addObserver(this);
            Simulation simulation = new Simulation(positions,map,startEnegry,genesLength,minimumToBeFull,giveToChild);
            SimulationEngine engine = new SimulationEngine(List.of(simulation));
            new Thread(engine :: runAsync).start();
        }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }


    }
    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

}

