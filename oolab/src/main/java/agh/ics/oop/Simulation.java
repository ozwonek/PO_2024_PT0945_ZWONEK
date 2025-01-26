package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.Config;

import static agh.ics.oop.OptionsParser.parse;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class Simulation implements Runnable {
    private final Config worldConfig;
    private Globe map;
    private int days=0;
    private Random rand = new Random();
    public Simulation(Config worldConfig, Globe map) {
        this.worldConfig = worldConfig;
        for (int i = 0;i<worldConfig.animalStart();i++){
            Vector2d randomPosition = new Vector2d(rand.nextInt(worldConfig.mapWidth()-1), rand.nextInt(worldConfig.mapHeight()-1));
            Animal animal = new Animal(randomPosition,worldConfig.animalStartEnergy(), worldConfig.animalGenotypeLength(), worldConfig.animalEnergyToReproduce(),worldConfig.animalEnergyToChild(),new Genomes(worldConfig.animalGenotypeLength()));
            map.place(animal);
            map.addGenCount(animal.getGenomes());
        }
        this.map = map;
    }


    @Override
    public void run() {
        while (true) {
                this.days+=1;
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
                map.setAllOffsprings();
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

