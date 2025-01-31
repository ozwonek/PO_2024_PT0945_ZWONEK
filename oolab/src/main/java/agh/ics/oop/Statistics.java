package agh.ics.oop; // czy to jest klasa na główny pakiet?

import agh.ics.oop.model.Genomes;

public class Statistics {
    private int dayCount = 0;
    private int animalCount = 0;
    private int grassCount = 0;
    private int freeSpots = 0;
    private Genomes mostPopularGenom;
    private double meanLifeForLiving = 0.0;
    private double meanEnergy = 0.0;
    private double meanLifeForDead = 0.0;
    private double meanChildrenCount = 0.0;


    public int getDayCount() {
        return dayCount;
    }

    public int getGrassCount() {
        return grassCount;
    }

    public int getAnimalCount() {
        return animalCount;
    }

    public int getFreeSpots() {
        return freeSpots;
    }

    public Genomes getMostPopularGenom() {
        return mostPopularGenom;
    }

    public double getMeanLifeForLiving() {
        return meanLifeForLiving;
    }

    public double getMeanEnergy() {
        return meanEnergy;
    }

    public double getMeanLifeForDead() {
        return meanLifeForDead;
    }

    public double getMeanChildrenCount() {
        return meanChildrenCount;
    }

    public Statistics() {
        this.dayCount = 0;
        this.animalCount = 0;
        this.grassCount = 0;
        this.freeSpots = 0;
        this.mostPopularGenom = null;
        this.meanChildrenCount = 0.0;
        this.meanLifeForLiving = 0.0;
        this.meanLifeForDead = 0.0;
        this.meanEnergy = 0.0;
    }

    public void setStatistics(int dayCount, int animalCount, int grassCount, int freeSpots, Genomes mostPopularGenom, double meanLifeForLiving, double meanChildrenCount, double meanEnergy, double meanLifeForDead) {
        this.dayCount = dayCount;
        this.animalCount = animalCount;
        this.grassCount = grassCount;
        this.freeSpots = freeSpots;
        this.mostPopularGenom = mostPopularGenom;
        this.meanChildrenCount = meanChildrenCount;
        this.meanLifeForLiving = meanLifeForLiving;
        this.meanLifeForDead = meanLifeForDead;
        this.meanEnergy = meanEnergy;
    }


}
