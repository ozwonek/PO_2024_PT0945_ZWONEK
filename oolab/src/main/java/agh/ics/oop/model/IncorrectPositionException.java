package agh.ics.oop.model;

public class IncorrectPositionException extends Exception { // czy to jest używane?
    public IncorrectPositionException(Vector2d position) {
        super("position" + position + "is not correct");
    }
}
