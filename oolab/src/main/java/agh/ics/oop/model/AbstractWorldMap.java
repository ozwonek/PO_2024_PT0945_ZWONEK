package agh.ics.oop.model;

import agh.ics.oop.World;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.Boundary;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    protected final Map<Vector2d, Animal> animals = new HashMap<>();
    protected final MapVisualizer visualizer = new MapVisualizer(this);
    protected final List<MapChangeListener> observers = new ArrayList<>();
    protected final UUID id = UUID.randomUUID();
    protected int width;
    protected int height;
    protected Vector2d lowerLeft = new Vector2d(0,0);
    protected Vector2d upperRight;

    public AbstractWorldMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.upperRight = new Vector2d(width-1, height-1);
    }

    public int getWidth(){
        return this.width;
    }
    @Override
    public boolean canMoveTo(Vector2d position){
        return position.follows(lowerLeft) && position.proceeds(upperRight) && !(objectAt(position) instanceof Animal) ;
    }

    @Override
    public boolean canMoveUpOrDown(Vector2d position) {
        return position.correctHeight(lowerLeft,upperRight);
    }

    @Override
    public boolean canMoveRightOrLeft(Vector2d position){
      return position.correctWidth(lowerLeft,upperRight);
    };
    public void addObserver(MapChangeListener listener){
        observers.add(listener);
    }
    public void removeObserver(MapChangeListener listener){
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
    public void move(Animal animal, MoveDirection direction,GrassField map){
        if(animal.equals(animals.get(animal.getPosition()))) {
            Vector2d oldPosition = animal.getPosition();
            animals.remove(animal.getPosition());
            animal.move(direction,this,map);
            animals.put(animal.getPosition(),animal);
            int grassEnergy = eatingGrass(animal.getPosition());
            animal.setAge(animal.getAge()+1);
            if (grassEnergy > 0) {
                animal.setEnergy(animal.getEnergy() + grassEnergy);
            }
            if(animal.getEnergy()==0){
                animals.remove(animal.getPosition());
                mapChange("zwierzak zmarł");
            }
            animal.setEnergy(animal.getEnergy() - 1);
//            if (isOccupied(animal.getPosition())) {
//                animals.put(animal.getPosition(), new Animal(animal.getPosition(),3));
//            }
            mapChange("zwierze zmienilo pozycje z: " + oldPosition + " na: " + animal.getPosition() + "a jego energia wynosi: "+ animal.getEnergy()+" zwierzak ma: "+ animal.getAge()+"lat");
//            mapChange("width: "+ width +" upper right: "+ upperRight + "lower left: " + lowerLeft);
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

    public abstract int eatingGrass(Vector2d position);


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
        return visualizer.draw(boundary.bottomLeftCorner(),boundary.topRightCorner());
    }
    @Override
    public UUID getID(){
        return id;
    }

}
