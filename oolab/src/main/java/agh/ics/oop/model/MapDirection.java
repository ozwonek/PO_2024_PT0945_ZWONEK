package agh.ics.oop.model;

import agh.ics.oop.World;
import agh.ics.oop.model.Vector2d;

import java.util.Random;


public enum MapDirection {
    NORTH,
    SOUTH,
    WEST,
    EAST;

    private static final Random random = new Random();

    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "^";
            case SOUTH -> "v";
            case WEST -> "<";
            case EAST -> ">";
        };
    }

    public MapDirection next() {
        return switch (this) {
            case NORTH -> EAST;
            case SOUTH -> WEST;
            case WEST -> NORTH;
            case EAST -> SOUTH;
        };
    }

    public MapDirection opposite(){
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case WEST -> EAST;
            case EAST -> WEST;
        };
    }

    public MapDirection previous() {
        return switch (this) {
            case NORTH -> WEST;
            case SOUTH -> EAST;
            case WEST -> SOUTH;
            case EAST -> NORTH;
        };
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> Vector2d.UP;
            case SOUTH -> Vector2d.DOWN;
            case WEST -> Vector2d.LEFT;
            case EAST -> Vector2d.RIGHT;
        };
    }

    public static MapDirection getRandomDirection() {
        MapDirection[] directions = values();
        int index = random.nextInt(directions.length);
        return directions[index];
    }
}
