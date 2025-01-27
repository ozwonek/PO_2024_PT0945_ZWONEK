package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.Config;

import java.io.BufferedWriter;
import java.io.FileWriter;

import static agh.ics.oop.OptionsParser.parse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class Simulation implements Runnable {
    private final Config worldConfig;
    private Globe map;
    private int days=0;
    private Random rand = new Random();
    public boolean isRunning() {
        return running;
    }
    String randomPath = "stats" + rand.nextInt(10) + ".csv";
    private boolean running = true;
    Object lock = new Object();
    boolean csvSave;
    public Simulation(Config worldConfig, Globe map, boolean csvSafe) {
        this.csvSave = csvSafe;
        this.worldConfig = worldConfig;
        for (int i = 0;i<worldConfig.animalStart();i++){
            Vector2d randomPosition = new Vector2d(rand.nextInt(worldConfig.mapWidth()-1), rand.nextInt(worldConfig.mapHeight()-1));
            Animal animal = new Animal(randomPosition,worldConfig.animalStartEnergy(), worldConfig.animalGenotypeLength(), worldConfig.animalEnergyToReproduce(),worldConfig.animalEnergyToChild(),new Genomes(worldConfig.animalGenotypeLength()));
            map.place(animal);
            map.addGenCount(animal.getGenomes());
        }
        this.map = map;
        System.out.println(csvSafe);
        if(csvSafe){
            try {
                Path path = Paths.get(randomPath);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(randomPath, true))) {
                        writer.write("Day Count,Animal Count,Grass Count,Free Spots,Most Popular Genom,Mean Children Count,Mean Life For Living,Mean Life For Dead,Mean Energy");
                    }
            } catch (IOException e) {
                System.err.println("Error initializing statistics file: " + e.getMessage());
            }

        }
    }
    public void saveStats(Statistics statistic){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(randomPath, true))) {
            String line = String.format("%d,%d,%d,%d,%s,%.2f,%.2f,%.2f,%.2f\n",
                    statistic.getDayCount(), statistic.getAnimalCount(), statistic.getGrassCount(),
                    statistic.getFreeSpots(), statistic.getMostPopularGenom() == null ? "N/A" : statistic.getMostPopularGenom(),
                    statistic.getMeanChildrenCount(), statistic.getMeanLifeForLiving(),
                    statistic.getMeanLifeForDead(), statistic.getMeanEnergy());
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving statistics: " + e.getMessage());
        }
    }
    public void resume(){
        System.out.println("Setting running to true");
        running = true;
        synchronized (lock) {
            System.out.println("Notifinyg");
            lock.notify();
        }
    }

    public void pause(){
        running = false;
    }


    private void runDay() {
        this.days += 1;
        map.clean();
        for (Animal animal : map.getAnimals()) {
            int gen = animal.getGenomes().get(animal.getActive());
            MapDirection direction = parse(animal.getOrientation(), gen);
            map.move(animal, direction, map);
            animal.nextGene();
        }
        map.allEat();
        map.allReproduce();
        map.growGrass(map.getGrassPerDay());
        map.setStatistics();
        if(csvSave){
            saveStats(map.getStats());
        }
        map.nextDay();

    }

    @Override
    public void run() {
        while (true) {
            try {
                synchronized (lock) {
                    while (!running) {
                        this.lock.wait();
                        System.out.println("After wait" + running);
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Wątek został przerwany na czekaniu na wznowienie: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
            this.runDay();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println("Wątek został przerwany: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }
    }


