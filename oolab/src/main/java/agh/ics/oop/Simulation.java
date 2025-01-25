package agh.ics.oop;

import agh.ics.oop.model.*;
import static agh.ics.oop.OptionsParser.parse;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class Simulation implements Runnable {
    private List<Animal> animals;
    private GrassField map;
    public Simulation(List<Vector2d> animalOnPosition, GrassField map, int energy, int genesLength, int minimumToBefull, int giveToChild) {
        this.animals = new ArrayList<>();
        for (Vector2d position : animalOnPosition) {
            Animal animal = new Animal(position,energy,genesLength,minimumToBefull,giveToChild,new Genomes(genesLength));
            map.place(animal);
            animals.add(animal);
        }
        this.map = map;
    }

    public List<Animal> getAnimals() {
        return new ArrayList<>(this.animals);
    }

    @Override
    public void run() {
        for (int i = 0; i<1000; i++) {
                map.clean();
                for(Animal animal : map.getAnimals()){
                    int gen = animal.getGenomes().get(animal.getActive());
                    MapDirection direction = parse(animal.getOrientation(),gen);
                    map.move(animal,direction,(GrassField) map);
                    animal.nextGene();
                }
                map.allEat(map);
                map.allReproduce();
                map.growGrass(map.getGrassPerDay());
                try{
                    Thread.sleep(3000);
                }
                catch (InterruptedException e) {
                    System.out.println("Wątek został przerwany: " + e.getMessage());
                    Thread.currentThread().interrupt();
                }
        }

    }


}

