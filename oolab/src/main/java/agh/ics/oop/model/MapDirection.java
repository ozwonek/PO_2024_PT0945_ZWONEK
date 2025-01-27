package agh.ics.oop.model;

import agh.ics.oop.World;
import agh.ics.oop.model.Vector2d;

import java.util.Random;


public enum MapDirection {
    NORTH,
    SOUTH,
    WEST,
    EAST,
    NORTHWEST,
    SOUTHWEST,
    SOUTHEAST,
    NORTHEAST;

    private static final Random random = new Random();

    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "^";
            case SOUTH -> "v";
            case WEST -> "<";
            case EAST -> ">";
            case NORTHEAST -> "NE";
            case SOUTHEAST -> "SE";
            case NORTHWEST -> "NW";
            case SOUTHWEST -> "SW";
        };
    }

    public MapDirection next() {
        return switch (this) {
            case NORTH -> NORTHEAST;
            case NORTHEAST -> EAST;
            case EAST -> SOUTHEAST;
            case SOUTHEAST -> SOUTH;
            case SOUTH -> SOUTHWEST;
            case SOUTHWEST -> WEST;
            case WEST -> NORTHWEST;
            case NORTHWEST -> NORTH;
        };
    }



    public MapDirection opposite(){
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case WEST -> EAST;
            case EAST -> WEST;
            case NORTHWEST -> SOUTHEAST;
            case SOUTHEAST -> NORTHWEST;
            case NORTHEAST -> SOUTHWEST;
            case SOUTHWEST -> NORTHEAST;
        };
    }

    public MapDirection previous() {
        return switch (this) {
            case NORTH -> NORTHWEST;
            case SOUTH -> SOUTHEAST;
            case WEST -> SOUTHWEST;
            case EAST -> NORTHEAST;
            case SOUTHWEST ->SOUTH;
            case NORTHEAST -> NORTH;
            case SOUTHEAST -> EAST;
            case NORTHWEST -> WEST;
        };
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> Vector2d.UP;
            case SOUTH -> Vector2d.DOWN;
            case WEST -> Vector2d.LEFT;
            case EAST -> Vector2d.RIGHT;
            case NORTHWEST -> Vector2d.UP.add(Vector2d.LEFT);
            case SOUTHEAST -> Vector2d.DOWN.add(Vector2d.RIGHT);
            case NORTHEAST -> Vector2d.UP.add(Vector2d.RIGHT);
            case SOUTHWEST -> Vector2d.DOWN.add(Vector2d.LEFT);
        };
    }

    public static MapDirection getRandomDirection() {
        MapDirection[] directions = values();
        int index = random.nextInt(directions.length);
        return directions[index];
    }
}
