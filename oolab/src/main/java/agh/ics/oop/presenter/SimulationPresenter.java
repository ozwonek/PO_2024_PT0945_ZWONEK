package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationApp;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import agh.ics.oop.model.util.Boundary;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import agh.ics.oop.model.AbstractWorldMap;

import java.io.IOException;
import java.util.List;

import static agh.ics.oop.OptionsParser.parse;

public class SimulationPresenter implements MapChangeListener {
    private WorldMap map;
    @FXML
    private Label movesDescriptionLabel;
    @FXML
    private TextField movesTextField;
    private final static int MAP_HEIGHT = 450;
    private final static int MAP_WIGHT = 450;
    public void setWorldMap(WorldMap map){
        this.map = map;
    }
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
        GridPane.setHalignment(label, HPos.CENTER);
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
                    mapGrid.add(new Label(map.objectAt(pos).toString()), i - boundary.bottomLeftCorner().getX() + 1, boundary.topRightCorner().getY() - j + 1);
                }
                else {
                    mapGrid.add(new Label(" "), i - boundary.bottomLeftCorner().getX() + 1, boundary.topRightCorner().getY() - j + 1);
                }
                GridPane.setHalignment(mapGrid.getChildren().getLast(), HPos.CENTER);
            }
        }



    }
    @Override
    public void mapChanged(WorldMap worldMap, String message){
        setWorldMap(worldMap);
        Platform.runLater(() -> {
            drawMap();
            movesDescriptionLabel.setText(message);
        });

    }
    public void onSimulationStartClicked(){
        String moveList = movesTextField.getText();
        try {
            List<MoveDirection> directions = parse(moveList.split(" "));
            List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,4));
            int energy = 3;
            AbstractWorldMap map = new GrassField(10, 3,6,6);
            map.addObserver(this);
            Simulation simulation = new Simulation(positions,directions,map,energy);
            SimulationEngine engine = new SimulationEngine(List.of(simulation));
            movesDescriptionLabel.setText("simulation started with:" + moveList);
            new Thread(engine :: runAsync).start();
//            System.out.println("System zakonczył dzialanie");
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
