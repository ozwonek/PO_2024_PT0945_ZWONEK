package agh.ics.oop.model;

import agh.ics.oop.World;

import java.util.Map;

public class Animal implements WorldElement {
    private MapDirection orientation;
    private Vector2d position;
    private int energy;
    private WorldMap map;

    public Animal(){
        this.orientation = MapDirection.NORTH;
        this.position = new Vector2d(2,2);
    }

    public Animal(Vector2d position, int energy) {
        this.position = position;
        this.orientation = MapDirection.NORTH;
        this.energy = energy;
    }
    @Override
    public String toString(){
        return  orientation.toString();
    }

    public int getEnergy(){
        return this.energy;
    }
    public void setEnergy(int newEnergy){
        this.energy = newEnergy;
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
        if (validator.canMoveUpOrDown(currentPosition)){
            this.position = currentPosition;
        }
        else {
            this.orientation= this.orientation.opposite();
        }
        if(validator.canMoveRightOrLeft(currentPosition)){
            this.position = currentPosition;
        }
        else {
            this.position = this.position.switchWidth(map.width);
        }
    }
}
