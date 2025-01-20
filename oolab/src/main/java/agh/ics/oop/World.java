package agh.ics.oop;

//import agh.ics.oop.OptionsParser;
import agh.ics.oop.model.*;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.List;


public class World {

    private static void run(MoveDirection[] commands) {
        for(MoveDirection argument : commands) {
            switch(argument){
                case FORWARD:
                    System.out.println("Zwierzak idzie do przodu!");
                    break;
                case BACKWARD:
                    System.out.println("Zwierzak idzie do tyłu!");
                    break;
                case LEFT:
                    System.out.println("Zwierzak idzie w lewo!");
                    break;
                case RIGHT:
                    System.out.println("Zwierzak idzie w prawo!");
            }

        }

    }
    public static void main(String[] args) {
        Application.launch(SimulationApp.class, args);
//        List<MoveDirection> directions = OptionsParser.parse(args);
//        List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,4),new Vector2d(2,2));
//        List<Simulation> simulations = new ArrayList<>();
//        for(int i = 0 ;i<1000;i++)
//        {
//            AbstractWorldMap map = new GrassField(i%200+10);
//            map.addObserver(new ConsoleMapDisplay());
//            Simulation simulation = new Simulation(positions,directions,map);
//            simulations.add(simulation);
//
//        }
//        SimulationEngine engine = new SimulationEngine(simulations);
//        engine.runAsyncInThreadPool();
//        System.out.println("System zakonczył dzialanie");


    }
}
