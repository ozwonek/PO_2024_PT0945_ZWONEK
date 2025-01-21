package agh.ics.oop.model;

import agh.ics.oop.World;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.Boundary;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractWorldMap implements WorldMap {
    protected final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    protected final MapVisualizer visualizer = new MapVisualizer(this);
    protected final List<MapChangeListener> observers = new ArrayList<>();
    protected final UUID id = UUID.randomUUID();
    protected int width;
    protected int height;
    protected Vector2d lowerLeft = new Vector2d(0,0);
    protected Vector2d upperRight;
    protected int deadAnimalCount = 0;
    public AbstractWorldMap(int width, int height)
    {
        this.width = width;
        this.height = height;
        this.upperRight = new Vector2d(width-1, height-1);
    }

    @Override
    public boolean canMoveUpOrDown(Vector2d position) {
        return position.correctHeight(lowerLeft,upperRight);
    }

    @Override
    public boolean canMoveRightOrLeft(Vector2d position){
      return position.correctWidth(lowerLeft,upperRight);
    }

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
    public void place(Animal animal){
        if(animals.get(animal.getPosition()) == null){
            List<Animal> onThisSpot = new ArrayList<>();
            onThisSpot.add(animal);
            animals.put(animal.getPosition(),onThisSpot);
        }
        else {
            animals.get(animal.getPosition()).add(animal);
        }

    }


    @Override
    public void move(Animal animal,MapDirection direction,GrassField map){
            Vector2d oldPosition = animal.getPosition();
            animals.get(oldPosition).remove(animal);
            if(animals.get(oldPosition).isEmpty()){
                animals.remove(oldPosition);
            }
            animal.move(this,direction,map);
            place(animal);
//            mapChange("zwierze zmienilo pozycje z: " + oldPosition + " na: " + animal.getPosition() + "a jego energia wynosi: "+ animal.getEnergy()+" zwierzak ma: "+ animal.getAge()+"lat");
//            mapChange("geny zwierzaka to: " + animal.getGenomes().toString());

        }

    @Override
    public void clean() {
        for (List<Animal> onOneSpot : animals.values()) {
            List<Animal> toRemove = new ArrayList<>();
            Vector2d position = onOneSpot.getFirst().getPosition();
            for (Animal animal : onOneSpot) {
                if (animal.getEnergy() == 0) {
                    toRemove.add(animal);
                    deadAnimalCount -= 1;
                }
            }
            for (Animal animalToClean : toRemove ){
                onOneSpot.remove(animalToClean);
            }
            if (onOneSpot.isEmpty()) {
                animals.remove(position);
            }
        }
    }
    public void allReproduce(){
        int counter = 0;
        for(List<Animal> onOneSpot: animals.values()){
            Vector2d position = onOneSpot.getFirst().getPosition();
            if(onOneSpot.size()<2){
                continue;
            }
            int onOneSpotSize = onOneSpot.size();
            onOneSpot.sort((a,b) -> Integer.compare(a.getEnergy(), b.getEnergy()));
            Animal child = onOneSpot.get(onOneSpotSize-1).reproduce(onOneSpot.get(onOneSpotSize-2));
            place(child);
            counter+=1;
        }
        mapChange("Urodzono dzieci:" + counter);
    }

//    public void meal(GrassField map){
//        for (List<Animal> onOneSpot: animals.values()){
//            if(map.getElements())
//        }
//    }



    @Override
    public boolean isOccupied(Vector2d position){
        return objectAt(position) != null;
    }

    @Override
    public WorldElement objectAt(Vector2d position){
        if(animals.get(position) == null){
            return null;
        }
        return animals.get(position).getFirst();

    }


    public abstract int eatingGrass(Vector2d position);


    @Override
    public List<WorldElement> getElements(){
        List<WorldElement> all = new ArrayList<>();
        for(List<Animal> animals: animals.values())
        {
            all.addAll(animals);
        }
        return all;
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
