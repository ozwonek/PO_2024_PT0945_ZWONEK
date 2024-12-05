package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener{
    private int actualisations = 1;
    @Override
    public void mapChanged(WorldMap worldMap, String message){
        System.out.println(message);
        System.out.println(worldMap.toString());
        System.out.println("Map actualisation done: " + actualisations);
        actualisations+=1;
    }

}
