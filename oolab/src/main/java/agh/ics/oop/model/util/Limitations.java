package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

public class Limitations {
    private final Vector2d first;
    private final Vector2d second;
    public Limitations(Vector2d first, Vector2d second){
        this.first = first;
        this.second = second;
    }
    public Vector2d getfirst() {
        return first;
    }

    public Vector2d getsecond() {
        return second;
    }

}
