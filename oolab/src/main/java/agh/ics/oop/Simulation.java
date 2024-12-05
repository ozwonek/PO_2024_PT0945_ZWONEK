package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private final List<Animal> animals;
    private final List<MoveDirection> moves;
    private final WorldMap map;

    public Simulation(List<Vector2d> animalOnPosition, List<MoveDirection> moves, WorldMap map) {
        this.moves = moves;
        this.animals = new ArrayList<>();
        for (Vector2d position : animalOnPosition) {
            Animal animal = new Animal(position);
            try {
                if(map.place(animal)){
                    animals.add(animal);
                }
            }
            catch(IncorrectPositionException e){
                System.out.println("Warning" + e.getMessage());
            }

        }
        this.map = map;
    }

    public List<Animal> getAnimals() {
        return new ArrayList<>(this.animals);
    }

    public void run() {

        int numberOfAnimal = 0;
        for (MoveDirection move : moves) {
            int sizeOfAnimals = animals.size();
            map.move(animals.get(numberOfAnimal), move);
            numberOfAnimal = (numberOfAnimal + 1) % sizeOfAnimals;
//            System.out.println(map.toString());
        }

    }
}

