package agh.ics.oop.model;

import java.util.List;
import java.util.Objects;

public class Vector2d {

    private final int x;
    private final int y;
    public final static Vector2d UP = new Vector2d(0,1);
    public final static Vector2d DOWN = new Vector2d(0,-1);
    public final static Vector2d LEFT = new Vector2d(-1,0);
    public final static Vector2d RIGHT = new Vector2d(1,0);
    public final static List<Vector2d> MOVEMENT_VECTORS = List.of(UP,DOWN,LEFT,RIGHT);

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", x, y);
    }

    public boolean follows(Vector2d other) {
        return this.x >= other.x && this.y >= other.y;
    }

    public boolean proceeds(Vector2d other) {
        return this.x <= other.x && this.y <= other.y;
    }

    public boolean correctHeight(Vector2d minHeightVector,Vector2d maxHeightVector){
        return this.y<=maxHeightVector.y && this.y>=minHeightVector.y;
    }
    public boolean correctWidth(Vector2d minWidth,Vector2d maxWidth){
        return this.x>=minWidth.x && this.x <= maxWidth.x;
    }
    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d substract(Vector2d other) {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    public Vector2d switchWidth(int width){
        return new Vector2d(Math.abs(width-this.x),this.y);
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Vector2d vector2d = (Vector2d) other;
        return x == vector2d.x && y == vector2d.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
