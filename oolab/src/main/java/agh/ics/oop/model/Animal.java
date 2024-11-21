package agh.ics.oop.model;

import java.util.Map;

public class Animal implements WorldElement {
    private MapDirection orientation;
    private Vector2d position;
    public Animal(){
        this.orientation = MapDirection.NORTH;
        this.position = new Vector2d(2,2);
    }

    public Animal(Vector2d position) {
        this.position = position;
        this.orientation = MapDirection.NORTH;
    }
    @Override
    public String toString(){
        return  orientation.toString();
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
    public void move(MoveDirection direction, MoveValidator validator){
        Vector2d currentPosition = this.position;
        switch(direction){
            case RIGHT -> this.orientation = orientation.next();
            case LEFT -> this.orientation = orientation.previous();
            case FORWARD -> currentPosition= orientation.toUnitVector().add(position);
            case BACKWARD -> currentPosition = position.substract(orientation.toUnitVector());
        }
        if (validator.canMoveTo(currentPosition)){
            this.position = currentPosition;
        }
    }


}
