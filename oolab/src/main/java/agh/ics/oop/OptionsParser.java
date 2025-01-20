package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.MoveDirection;

public class OptionsParser {
    public static MapDirection parse(MapDirection direction, int gen) {
        switch (gen){
            case 0 -> {
                return direction;
            }
            case 1 ->{
                return direction.next();
            }
            case 2 ->{
                return direction.next().next();
            }
            case 3->{
                return direction.opposite().previous();
            }
            case 4 ->{
                return direction.opposite();
            }
            case 5 ->{
                return direction.opposite().next();
            }
            case 6->{
                return direction.opposite().next().next();
            }
            case 7 ->{
                return direction.previous();
            }
            default ->
                throw new IllegalArgumentException(gen + " is not legal move specification");
        }
    }
//    public static List<MoveDirection> parse(String[] moves) {
//        List<MoveDirection> moveDirections = new ArrayList<MoveDirection>();
//        for (String arg : moves) {
//
//            switch (arg) {
//                 case "f":
//                    moveDirections.add(MoveDirection.FORWARD);
//                    break;
//                 case "b":
//                     moveDirections.add(MoveDirection.BACKWARD);
//                     break;
//                 case "r":
//                     moveDirections.add(MoveDirection.RIGHT);
//                     break;
//                 case "l":
//                     moveDirections.add(MoveDirection.LEFT);
//                     break;
//                 default:
//                     throw new IllegalArgumentException(arg + " is not legal move specification");
//             }
//         }
//         return moveDirections;
//
//    }
}
