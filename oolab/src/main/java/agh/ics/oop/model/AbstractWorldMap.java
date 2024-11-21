package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.Limitations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractWorldMap implements WorldMap {
    protected Vector2d lowerLeft = new Vector2d(Integer.MIN_VALUE,Integer.MIN_VALUE);
    protected Vector2d upperRight = new Vector2d(Integer.MAX_VALUE,Integer.MAX_VALUE);
    protected final Map<Vector2d, Animal> animals = new HashMap<>();
    protected final MapVisualizer visualizer = new MapVisualizer(this);
    @Override
    public boolean canMoveTo(Vector2d position){
        return position.follows(lowerLeft) && position.proceeds(upperRight) && !(objectAt(position) instanceof Animal) ;
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
    public WorldElement objectAt(Vector2d position){
        return animals.get(position);

    }

    @Override
    public List<WorldElement> getElements(){
        return new ArrayList<>(animals.values());
    }

    public Limitations calculateLimits(){
        return new Limitations(lowerLeft,upperRight);
    }
    @Override
    public String toString(){
        Limitations limits = calculateLimits();
        return visualizer.draw(limits.getfirst(),limits.getsecond());
    }

}
