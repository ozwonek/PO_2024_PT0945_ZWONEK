package agh.ics.oop.model.util;

import java.util.HashMap;
import java.util.Map;

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
    public Map<String, Integer> toMap() {
        Map<String, Integer> map = new HashMap<>();

        map.put("mapHeight", mapHeight);
        map.put("mapWidth", mapWidth);
        map.put("grassStart", grassStart);
        map.put("grassDaily", grassDaily);
        map.put("grassEnergy", grassEnergy);
        map.put("animalStart", animalStart);
        map.put("animalStartEnergy", animalStartEnergy);
        map.put("animalEnergyToReproduce", animalEnergyToReproduce);
        map.put("animalEnergyToChild", animalEnergyToChild);
        map.put("animalGenotypeLength", animalGenotypeLength);

        return map;
    }
}
