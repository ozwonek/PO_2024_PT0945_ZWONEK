package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {
    @Test
    void firstPositionDefault(){
        Animal animal = new Animal();

        assertEquals(new Vector2d(2,2),animal.getPosition());
        assertEquals(MapDirection.NORTH,animal.getOrientation());

    }
    @Test
    void firstPositionChanged(){
        Animal animal = new Animal(new Vector2d(1,2));

        assertEquals(new Vector2d(1,2),animal.getPosition());
        assertEquals(MapDirection.NORTH,animal.getOrientation());
    }
    @Test
    void changeToString(){
        Animal animal = new Animal();

        assertEquals("^",animal.toString());
    }
    @Test
    void samePosition(){
        Animal animal  = new Animal();

        assertEquals(animal.getPosition(), new Vector2d(2, 2));
        assertNotEquals(animal.getPosition(), new Vector2d(2, 1));

    }


}