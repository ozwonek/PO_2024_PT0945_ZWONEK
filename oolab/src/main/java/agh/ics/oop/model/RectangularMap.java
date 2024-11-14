package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class RectangularMap implements WorldMap {
    private final Map<Vector2d, Animal> animals = new HashMap<>();
    private final MapVisualizer visualizer = new MapVisualizer(this);
    private final Vector2d lowerLeft;
    private final Vector2d upperRight;
    public RectangularMap(int width, int height){
        lowerLeft = new Vector2d(0,0);
        upperRight = new Vector2d(width-1,height-1);
    }
    @Override
    public boolean canMoveTo(Vector2d position){
        if(position.follows(lowerLeft) && position.proceeds(upperRight) ){
            return !isOccupied(position);
        }
        return false;
    }

    @Override
    public boolean place(Animal animal){
        if(canMoveTo(animal.getPosition())){
            animals.put(animal.getPosition(),animal);
            return true;
        }
        return false;
    }
    @Override
    public void move(Animal animal, MoveDirection direction){
            if(animal.equals(animals.get(animal.getPosition()))) {
                animals.remove(animal.getPosition());
                animal.move(direction,this);
                animals.put(animal.getPosition(),animal);

            }

    }
    @Override
    public boolean isOccupied(Vector2d position){
        return objectAt(position) != null;

    }
    @Override
    public Animal objectAt(Vector2d position){
        if (animals.get(position)==null){
            return null;
        }
        return animals.get(position);

    }
    @Override
    public String toString(){
       return visualizer.draw(lowerLeft,upperRight);
    }

}

