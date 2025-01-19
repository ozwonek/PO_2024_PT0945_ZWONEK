package agh.ics.oop.model;

import agh.ics.oop.World;
import java.util.UUID;
import java.util.Map;

public class Animal implements WorldElement {
    private MapDirection orientation;
    private Vector2d position;
    private int energy;
    private WorldMap map;
    private int age;
    private UUID id = UUID.randomUUID();

    public Animal(){
        this.orientation = MapDirection.NORTH;
        this.position = new Vector2d(2,2);
    }

    public Animal(Vector2d position, int energy) {
        this.position = position;
//        this.orientation = MapDirection.getRandomDirection();
        this.orientation = MapDirection.NORTH;
        this.energy = energy;
    }

    @Override
    public String toString(){
        return  orientation.toString();
    }

    public UUID getId(){
        return this.id;
    }
    public int getEnergy(){
        return this.energy;
    }
    public void setEnergy(int newEnergy){
        this.energy = newEnergy;
    }
    public int getAge(){
        return this.age;
    }
    public void setAge(int newAge){
        this.age = newAge;
    }

    public MapDirection getOrientation(){
        return this.orientation;
    }
    @Override
    public Vector2d getPosition(){
        return this.position;
    }
    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }

    public void move(MoveDirection direction, MoveValidator validator,GrassField map){
        Vector2d currentPosition = this.position;
        switch(direction){
            case RIGHT -> this.orientation = orientation.next();
            case LEFT -> this.orientation = orientation.previous();
            case FORWARD -> currentPosition= orientation.toUnitVector().add(position);
            case BACKWARD -> currentPosition = position.substract(orientation.toUnitVector());
        }
        if (!validator.canMoveUpOrDown(currentPosition)){
            this.orientation= this.orientation.opposite();
        }
        else if(!validator.canMoveRightOrLeft(currentPosition)){
            this.position = this.position.switchWidth(map.width-1);
        }
        else {
            this.position=currentPosition;
        }
    }
}
