package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

public class Boundary {
    private final Vector2d bottomLeftCorner;
    private final Vector2d topRightCorner;
    public Boundary(Vector2d bottomLeftCorner, Vector2d topRightCorner){
        this.bottomLeftCorner = bottomLeftCorner;
        this.topRightCorner = topRightCorner;
    }
    public Vector2d getBottomLeftCorner() {
        return bottomLeftCorner;
    }

    public Vector2d getTopRightCorner() {
        return topRightCorner;
    }

}
