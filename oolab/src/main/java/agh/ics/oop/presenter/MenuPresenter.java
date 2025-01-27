package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.Statistics;
import agh.ics.oop.model.*;
//import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.Config;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;


import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.stage.Stage;


//import static agh.ics.oop.OptionsParser.parse;

public class MenuPresenter {
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



    private final HashMap<String,Config> savedConfigurations = new HashMap<>();

    private final ArrayList<Integer> currentConfiguration = new ArrayList<>();

    private final SimulationEngine simulationEngine =  new SimulationEngine();

    @FXML
    private void showNewConfigurationForm(){
        configurationForm.setVisible(true);
    }

    @FXML
    private void addNewConfiguration(){
        String configName = configNameTextField.getText();
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

        Config worldConfig =  new Config(mapHeight,mapWidth,grassStart,grassDaily,grassEnergy,animalStart,animalStartEnergy,animalEnergyToReproduce,animalEnergyToChild,animalGenotypeLength);
        savedConfigurations.put(configName,worldConfig);
        configurations.getItems().add(configName);
        configurationForm.setVisible(false);

        appendConfigToJson(worldConfig.toMap(), configName);
    }
    public void loadConfiguration(){
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Map<String, Integer>>>(){}.getType();
        Map<String,Map<String,Integer>> allconfigs;
        try (Reader reader = new FileReader("configurations.json")) {
            allconfigs = gson.fromJson(reader, type);
            if (allconfigs == null) {
                return;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Plik nie istnieje. Tworzę nowy.");
            allconfigs = new HashMap<>();
        } catch (IOException e) {
            System.out.println("Błąd odczytu pliku: " + e.getMessage());
            return;
        }
        System.out.println(allconfigs);
        for(String name: allconfigs.keySet()){
            Map<String,Integer> configs = allconfigs.get(name);
            Config config = new Config(configs.get("mapHeight"),configs.get("mapWidth"),configs.get("grassStart"),configs.get("grassDaily"),configs.get("grassEnergy"),configs.get("animalStart"),configs.get("animalStartEnergy"),configs.get("animalEnergyToReproduce"),configs.get("animalEnergyToChild"),configs.get("animalGenotypeLength"));
            savedConfigurations.put(name,config);
            configurations.getItems().add(name);
        }
    }

    public void appendConfigToJson(Map<String,Integer> configs, String configName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String,Map<String,Integer>> allconfigs;
        Type type = new TypeToken<Map<String, Map<String, Integer>>>(){}.getType();

        try (Reader reader = new FileReader("configurations.json")) {
            allconfigs = gson.fromJson(reader, type);
            if (allconfigs == null) {
                allconfigs = new HashMap<>();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Plik nie istnieje. Tworzę nowy.");
            allconfigs = new HashMap<>();
        } catch (IOException e) {
            System.out.println("Błąd odczytu pliku: " + e.getMessage());
            return;
        }
        allconfigs.put(configName,configs);
        try (Writer writer = new FileWriter("configurations.json")) {
            gson.toJson(allconfigs, writer);
            System.out.println("Nowa konfiguracja została dopisana do pliku.");
        } catch (IOException e) {
            System.out.println("Błąd zapisu pliku: " + e.getMessage());
        }
    }


    @FXML
    private Config getConfiguration(){
        String selectedConfiguration = configurations.getSelectionModel().getSelectedItem();
        return savedConfigurations.get(selectedConfiguration);
    }

    public void onSimulationStartClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        Parent viewRoot = loader.load();
        Config worldConfig = getConfiguration();
        SimulationPresenter presenter = loader.getController();
        presenter.simulationEngine = this.simulationEngine;
        Globe map = new Globe(worldConfig);
        presenter.setWorldMap(map);
        presenter.setConfig(worldConfig);
        presenter.startOrStopButton();
        Stage simulationStage = new Stage();
        simulationStage.setScene(new Scene(viewRoot));
        simulationStage.show();

    }

}

