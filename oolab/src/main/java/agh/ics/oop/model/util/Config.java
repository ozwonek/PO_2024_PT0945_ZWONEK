package agh.ics.oop.model.util;

public record Config(int mapHeight,
                     int mapWidth,
                     int plantStart,
                     int plantDaily,
                     int plantEnergy,
                     int animalStart,
                     int animalStartEnergy,
                     int animalEnergyReproductionDepletion,
                     int animalEnergyDailyDepletion,
                     int animalEnergyToReproduce,
                     int animalMutationMinimum,
                     int animalMutationMaximum,
                     int animalGenotypeLength) {
}
