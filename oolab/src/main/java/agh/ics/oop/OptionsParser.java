package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;

import java.util.Arrays;

public class OptionsParser {
    public static MoveDirection[] parsing(String[] args) {
         MoveDirection[] moveDirections = new MoveDirection[args.length];
         int i = 0;
         for (String arg : args) {

             switch (arg) {
                 case "f":
                     moveDirections[i] = MoveDirection.FORWARD;
                     break;
                 case "b":
                     moveDirections[i] = MoveDirection.BACKWARD;
                     break;
                 case "r":
                     moveDirections[i] = MoveDirection.RIGHT;
                     break;
                 case "l":
                     moveDirections[i] = MoveDirection.LEFT;
                     break;
                 default:
                     i--;
             }
             i++;


         }
         return Arrays.copyOfRange(moveDirections,0,i);

    }
}
