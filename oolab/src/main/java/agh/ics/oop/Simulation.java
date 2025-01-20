package agh.ics.oop;

import agh.ics.oop.model.*;
import static agh.ics.oop.OptionsParser.parse;
import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
    private final List<Animal> animals;
    private final WorldMap map;

    public Simulation(List<Vector2d> animalOnPosition, WorldMap map,int energy, int genesLength) {
        this.animals = new ArrayList<>();
        for (Vector2d position : animalOnPosition) {
            Animal animal = new Animal(position,energy,genesLength);
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
        int sizeOfAnimals = animals.size();
        int sizeOfGenes = animals.getFirst().getGenomes().getGenesLength();
        int numberOfAnimal = 0;
        int numberOfGen = 0;
        for (int i = 0; i<1000; i++) {
                Animal animal = animals.get(numberOfAnimal);
                int gen = animal.getGenomes().get(numberOfGen);
                MapDirection direction = parse(animal.getOrientation(),gen);
                map.move(animals.get(numberOfAnimal), direction,(GrassField) map);
                numberOfAnimal = (numberOfAnimal + 1) % sizeOfAnimals;
                numberOfGen = (numberOfGen + 1 - numberOfAnimal) % sizeOfGenes;
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

