package agh.ics.oop.model;

import java.util.Map;

public class Animal {
    private MapDirection orientation;
    private Vector2d position;
    private static final Vector2d LEFTEDGE = new Vector2d(0,0);
    private static final Vector2d RIGHTEDGE = new Vector2d(4,4);
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
        return position  +" "+  orientation;
    }
    public MapDirection getOrientation(){
        return this.orientation;
    }
    public Vector2d getPosition(){
        return this.position;
    }
    public boolean isAt(Vector2d position){
        return position.equals(position);
    }
    public void move(MoveDirection direction){
        Vector2d actualPosition = this.position;
        switch(direction){
            case RIGHT -> this.orientation = orientation.next();
            case LEFT -> this.orientation = orientation.previous();
            case FORWARD -> actualPosition = orientation.toUnitVector().add(position);
            case BACKWARD -> actualPosition = position.substract(orientation.toUnitVector());
        }
        if (LEFTEDGE.equals(LEFTEDGE.lowerLeft(actualPosition)) && RIGHTEDGE.equals(RIGHTEDGE.upperRight(actualPosition))){
            this.position = actualPosition;
        }
    }


}
