package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
    private final List<Animal> animals;
    private final List<MoveDirection> moves;
    private final WorldMap map;

    public Simulation(List<Vector2d> animalOnPosition, List<MoveDirection> moves, WorldMap map,int energy) {
        this.moves = moves;
        this.animals = new ArrayList<>();
        for (Vector2d position : animalOnPosition) {
            Animal animal = new Animal(position,energy);
            try {
                if (map.place(animal)) {
                    animals.add(animal);
                }
            } catch (IncorrectPositionException e) {
                System.out.println("Warning" + e.getMessage());
            }

        }
        this.map = map;
    }

    public List<Animal> getAnimals() {
        return new ArrayList<>(this.animals);
    }

    @Override
    public void run() {

        int numberOfAnimal = 0;

        for (MoveDirection move : moves) {
                int sizeOfAnimals = animals.size();
                map.move(animals.get(numberOfAnimal), move, (GrassField) map);
                numberOfAnimal = (numberOfAnimal + 1) % sizeOfAnimals;
                try{
                    Thread.sleep(2000);
                }
                catch (InterruptedException e) {
                    System.out.println("Wątek został przerwany: " + e.getMessage());
                    Thread.currentThread().interrupt();
                }
        }

    }


}

