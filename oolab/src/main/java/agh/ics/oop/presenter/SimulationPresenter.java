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


//import static agh.ics.oop.OptionsParser.parse;

public class SimulationPresenter implements MapChangeListener  {
    public VBox mainContainer;
    public Label animalOnObservation;
    public VBox animalStatistics;
    private Globe map;
    private Animal animal;
    private static final String CONFIG_FILE = "configurations.json";
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
    @FXML
    private TextField configNameTextField;


    private TextField mapWidthTextField;
    private TextField mapHeigthTextField;
    private final static int MAP_HEIGHT = 450;
    private final static int MAP_WIGHT = 450;

    private final static int STATS_WIDTH = 200;
    private final static int STATS_HEIGHT = 600;
    private int simulationDay = 0;

    private Config config;
    public SimulationEngine simulationEngine;
    private Simulation simulation;
    private boolean save = false;
    public void setSave(boolean checkBox) {
        this.save = checkBox;
    }

    public void setWorldMap(Globe map){
        this.map = map;
    }
    public void setConfig(Config config){
        this.config = config;
    }

    private void setAnimal(Animal animal){
        this.animal = animal;
        onAnimalClicked(animal);
        animalStatistics.setVisible(true);
    }
    
    @FXML
    private void deleteAnimalFromObservation(){
        this.animal=null;
        animalStatistics.setVisible(false);
    }



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

    private void followAnimal(Animal animal){
        if(!simulation.isRunning()){
            setAnimal(animal);
        }
    }
    private void drawMap(boolean guard){

        clearGrid(mapGrid); // czyszczenie
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
                    Label animalLabel=new Label();
                    boolean isDominant = false;
                    if(map.objectAt(pos).getGenomes().equals(map.getStats().getMostPopularGenom())){
                        isDominant = true;
                    }
                    animalLabel.setStyle(map.objectAt(pos).toImage(width,height,guard&&isDominant));
                    mapGrid.add(animalLabel , i + 1, mapHeight - j);
                    animalLabel.setOnMouseClicked(event -> {
                        followAnimal(map.objectAt(pos));
                    });
                }
                else if (map.isGrass(pos)) {
                    Label grassLabel = new Label();
                    grassLabel.setStyle(map.toImage(width,height));
                    grassLabel.setPrefSize(width, height);
                    mapGrid.add(grassLabel, i + 1, mapHeight - j);
                }
                else {
                    Label dirt = new Label(" ");
                    if(guard&&map.isPreferred(pos)){
                        dirt.setStyle("-fx-background-color:#f3d217 ");

                    }
                    else{
                        dirt.setStyle("-fx-background-color:#b7b4a1 ");
                    }
                    dirt.setPrefSize(width, height);
                    mapGrid.add(dirt, i + 1, mapHeight - j);


                }
                GridPane.setHalignment(mapGrid.getChildren().getLast(), HPos.CENTER);
            }
        }



    }

    private void preferedSpotsAndDominantGenomes(){
        drawMap(true);
    }


    @FXML
    private void onAnimalClicked(Animal animal){
        clearGrid(animalGrid);

        animalOnObservation.setText("Obserwujesz zwierzaka na pozycji: " + animal.getPosition());

        Label label = new Label("genotyp zwierzka: " + animal.getGenomes());
        animalGrid.add(label,0,1);
        label.getStyleClass().add("animal-label");
        label = new Label("aktywny gen: " + animal.getGenomes().get(animal.getActive()));
        animalGrid.add(label,1,1);
        label.getStyleClass().add("animal-label");
        label = new Label("ilość energii: " + animal.getEnergy());
        animalGrid.add(label, 0,2);
        label.getStyleClass().add("animal-label");
        label = new Label("ilość zjedzonych roślin: " + animal.getEatenGrass());
        animalGrid.add(label, 1, 2);
        label.getStyleClass().add("animal-label");
        label = new Label("ilość dzieci: " + animal.getChildrenSize());
        animalGrid.add(label,0,3);
        label.getStyleClass().add("animal-label");
        label = new Label("ile dni żyje: " + animal.getAge() );
        animalGrid.add(label,1,3);
        label.getStyleClass().add("animal-label");
        label = new Label("ilość potomków: " + animal.getOffspringCount());
        animalGrid.add(label,0,4);
        label.getStyleClass().add("animal-label");
        if(animal.getDeathDay()!=-1){
            label = new Label("dzień śmierci: "+animal.getDeathDay());

        }
        else{
            label = new Label("zwierzę żyje");
        }
        animalGrid.add(label,1,4);
        label.getStyleClass().add("animal-label");
    }

    @Override
    public void mapChanged(Globe worldMap, String message){
        setWorldMap(worldMap);
        Platform.runLater(() -> {
            this.simulationDay+=1;
            drawMap(false);
            actualiseStatistics();
            if(this.animal!=null){
                onAnimalClicked(animal);
            }

        });

    }

    @FXML
    public void startOrStopButton() {
        if (simulation == null) {
            simulation = new Simulation(config, map, save);
            simulationEngine.runNewSimulation(simulation);
            map.addObserver(this);
        } else {
            if (simulation.isRunning()) {
                simulation.pause();
                preferedSpotsAndDominantGenomes();
            } else {
                System.out.println("Trying to resume simulation");
                simulation.resume();;
            }
        }
    }


        private void clearGrid (GridPane grid){
            grid.getChildren().retainAll(grid.getChildren().get(0)); // hack to retain visible grid lines
            grid.getColumnConstraints().clear();
            grid.getRowConstraints().clear();
        }


    }
