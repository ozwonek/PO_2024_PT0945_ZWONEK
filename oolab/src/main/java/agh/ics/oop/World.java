package agh.ics.oop;

import agh.ics.oop.OptionsParser;
import agh.ics.oop.model.MoveDirection;


public class World {

    public static void run(MoveDirection[] komendy) {
        for(MoveDirection argument : komendy) {
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
        System.out.print("Start\n");
        MoveDirection[] komendy =  OptionsParser.parsing(args);
        run(komendy);
        System.out.print("Stop");

    }
}
