package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.Boundary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractWorldMap implements WorldMap {
    protected Vector2d lowerLeft = new Vector2d(Integer.MIN_VALUE,Integer.MIN_VALUE);
    protected Vector2d upperRight = new Vector2d(Integer.MAX_VALUE,Integer.MAX_VALUE);
    protected final Map<Vector2d, Animal> animals = new HashMap<>();
    protected final MapVisualizer visualizer = new MapVisualizer(this);
    protected final List<MapChangeListener> observers = new ArrayList<>();
    @Override
    public boolean canMoveTo(Vector2d position){
        return position.follows(lowerLeft) && position.proceeds(upperRight) && !(objectAt(position) instanceof Animal) ;
    }
    public void addObserver(MapChangeListener listener){
        observers.add(listener);
    }
    public void abstractObserver(MapChangeListener listener){
        observers.remove(listener);
    }
    protected void mapChange(String message){
        for(MapChangeListener observer: observers){
            observer.mapChanged(this,message);
        }
    }

    @Override
    public boolean place(Animal animal) throws IncorrectPositionException {
        if(canMoveTo(animal.getPosition())){
            animals.put(animal.getPosition(),animal);
            mapChange("dodano zwierze na pozycji: " + animal.getPosition());
            return true;
        }
        throw new IncorrectPositionException(animal.getPosition());
    }
    @Override
    public void move(Animal animal, MoveDirection direction){
        if(animal.equals(animals.get(animal.getPosition()))) {
            Vector2d oldPosition = animal.getPosition();
            animals.remove(animal.getPosition());
            animal.move(direction,this);
            animals.put(animal.getPosition(),animal);
            mapChange("zwierze zmienilo pozycje z: " + oldPosition + " na: " + animal.getPosition());

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

    @Override
    public Boundary getCurrentBounds(){
        return new Boundary(lowerLeft,upperRight);
    }
    @Override
    public String toString(){
        Boundary boundary = getCurrentBounds();
        return visualizer.draw(boundary.getBottomLeftCorner(),boundary.getTopRightCorner());
    }

}
