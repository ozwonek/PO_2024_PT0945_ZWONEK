package agh.ics.oop.model;

public class AnimalStatistics {

    private Genomes genome;

    public AnimalStatistics(Animal animal,int daysFromSimulationStarted){
        this.genome = animal.getGenomes();
    }
}
