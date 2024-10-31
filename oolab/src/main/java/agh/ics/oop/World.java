package agh.ics.oop;

import agh.ics.oop.OptionsParser;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;


public class World {

    private static void run(MoveDirection[] commands) {
        for(MoveDirection argument : commands) {
            switch(argument){
                case FORWARD:
                    System.out.println("Zwierzak idzie do przodu!");
                    break;
                case BACKWARD:
                    System.out.println("Zwierzak idzie do tyłu!");
                    break;
                case LEFT:
                    System.out.println("Zwierzak idzie w lewo!");
                    break;
                case RIGHT:
                    System.out.println("Zwierzak idzie w prawo!");
            }

        }

    }
    public static void main(String[] args) {
        Vector2d position1 = new Vector2d(1,2);
        System.out.println(position1);
        Vector2d position2 = new Vector2d(-2,1);
        System.out.println(position2);
        System.out.println(position1.add(position2));
        MapDirection direction = MapDirection.EAST;
        System.out.println(direction);
        direction = direction.next();
        System.out.println(direction);
        direction = direction.previous();
        System.out.println(direction);
        Vector2d coordinate = direction.toUnitVector();
        System.out.println(coordinate);

    }
}
