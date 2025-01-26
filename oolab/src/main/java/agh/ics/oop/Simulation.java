package agh.ics.oop;

import agh.ics.oop.model.*;
import static agh.ics.oop.OptionsParser.parse;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class Simulation implements Runnable {
    private List<Animal> animals;
    private Globe map;

    public Simulation(List<Vector2d> animalOnPosition, Globe map, int energy, int genesLength, int minimumToBefull, int giveToChild) {
        this.animals = new ArrayList<>();
        for (Vector2d position : animalOnPosition) {
            Animal animal = new Animal(position,energy,genesLength,minimumToBefull,giveToChild,new Genomes(genesLength));
            map.place(animal);
            animals.add(animal);
            map.addGenCount(animal.getGenomes());
        }


        this.map = map;
    }

    public List<Animal> getAnimals() {
        return new ArrayList<>(this.animals);
    }

    @Override
    public void run() {
        while (true) {
                map.clean();
                map.setStatistics();
                map.nextDay();
                for (Animal animal : map.getAnimals()) {
                    int gen = animal.getGenomes().get(animal.getActive());
                    MapDirection direction = parse(animal.getOrientation(), gen);
                    map.move(animal, direction,  map);
                    animal.nextGene();
                }
                map.allEat();
                map.allReproduce();
                map.growGrass(map.getGrassPerDay());


                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    System.out.println("Wątek został przerwany: " + e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
    }

}

