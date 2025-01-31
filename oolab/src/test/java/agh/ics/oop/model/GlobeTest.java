package agh.ics.oop.model;

import agh.ics.oop.Statistics;
import agh.ics.oop.model.util.Config;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GlobeTest {

    @Test
    void correctOld() {
        Config config = new Config(6, 6, 3, 5, 10, 3, 7, 10, 5, 2);
        Globe globe = new Globe(config);
        Genomes genes = new Genomes(10);
        Animal animal = new Animal(new Vector2d(0, 0), 10, 10, 10, 5, genes);
        globe.move(animal, MapDirection.NORTH, globe);
        assertEquals(1, animal.getAge());
    }

    @Test
    void cleanGlobe() {
        Config config = new Config(6, 6, 0, 0, 10, 3, 0, 10, 5, 2);
        Globe globe = new Globe(config);
        Genomes genes = new Genomes(10);
        Animal animal = new Animal(new Vector2d(0, 0), 0, 10, 10, 5, genes);
        globe.move(animal, MapDirection.WEST, globe);
        globe.clean();
        assertEquals(1, globe.deadAnimalCount);
    }

    @Test
    void eatingGrass() {
        Config config = new Config(6, 6, 36, 9, 10, 0, 10, 10, 5, 2);
        Globe globe = new Globe(config);
        Genomes genes = new Genomes(10);
        Animal animal = new Animal(new Vector2d(0, 0), 10, 10, 10, 5, genes);
        globe.move(animal, MapDirection.SOUTH, globe);
        globe.allEat();
        assertEquals(19, animal.getEnergy());
    }

    @Test
    void grassCount() {
        Config config = new Config(6, 6, 10, 9, 10, 0, 10, 10, 5, 2);
        Globe globe = new Globe(config);
        globe.growGrass(globe.getGrassPerDay());
        assertEquals(19, globe.getGrassSize());
    }

    @Test
    void correctNotOccupied() {
        Config config = new Config(6, 6, 16, 9, 10, 0, 10, 5, 5, 2);
        Globe globe = new Globe(config);
        assertEquals(20, globe.freeSpotsLeft());
    }

    @Test
    void validMoves() {

        Vector2d position = new Vector2d(5, 5);
        Vector2d lowerLeft = new Vector2d(0, 0);
        Vector2d upperRight1 = new Vector2d(5, 4);
        Vector2d upperRight2 = new Vector2d(4, 5);

        assertFalse(position.correctHeight(lowerLeft, upperRight1));
        assertFalse(position.correctWidth(lowerLeft, upperRight2));
        assertTrue(position.correctHeight(lowerLeft, upperRight2));
        assertTrue(position.correctWidth(lowerLeft, upperRight1));
    }
}