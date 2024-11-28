package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptionsParser {
    public static List<MoveDirection> parse(String[] moves) {
        List<MoveDirection> moveDirections = new ArrayList<MoveDirection>();
        for (String arg : moves) {

            switch (arg) {
                 case "f":
                    moveDirections.add(MoveDirection.FORWARD);
                    break;
                 case "b":
                     moveDirections.add(MoveDirection.BACKWARD);
                     break;
                 case "r":
                     moveDirections.add(MoveDirection.RIGHT);
                     break;
                 case "l":
                     moveDirections.add(MoveDirection.LEFT);
                     break;
                 default:
                     throw new IllegalArgumentException(arg + " is not legal move specification");
             }
         }
         return moveDirections;

    }
}
