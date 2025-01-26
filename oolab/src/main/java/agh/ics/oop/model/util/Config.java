package agh.ics.oop.model.util;

public record Config(int mapHeight,
                     int mapWidth,
                     int grassStart,
                     int grassDaily,
                     int grassEnergy,
                     int animalStart,
                     int animalStartEnergy,
                     int animalEnergyToReproduce,
                     int animalEnergyToChild,
                     int animalGenotypeLength) {
}
