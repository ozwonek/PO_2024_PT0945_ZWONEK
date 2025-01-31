package agh.ics.oop;


import agh.ics.oop.model.MapDirection;


public class OptionsParser {
    public static MapDirection parse(MapDirection direction, int gen) { // nazwa klasy i metody mają się nijak, do tego, co one rzeczywiście robią; czemu to nie jest w klasie MapDirection?
        switch (gen) {
            case 0 -> {
                return direction;
            }
            case 1 -> {
                return direction.next();
            }
            case 2 -> {
                return direction.next().next();
            }
            case 3 -> {
                return direction.opposite().previous();
            }
            case 4 -> {
                return direction.opposite();
            }
            case 5 -> {
                return direction.opposite().next();
            }
            case 6 -> {
                return direction.opposite().next().next();
            }
            case 7 -> {
                return direction.previous();
            }
            default -> throw new IllegalArgumentException(gen + " is not legal move specification");
        }
    }

}
