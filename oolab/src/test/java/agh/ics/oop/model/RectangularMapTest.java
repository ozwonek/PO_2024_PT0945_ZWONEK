package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectangularMapTest {

    @Test
    void mapWorks() throws IncorrectPositionException {
        WorldMap map = new RectangularMap(5, 5);
        Animal animal = new Animal();
        map.place(animal);
        map.move(animal, MoveDirection.FORWARD);
        assertEquals(new Vector2d(2, 3), animal.getPosition());
    }
    @Test
    void canMoveToValid() {
        WorldMap map = new RectangularMap(5, 5);
        assertTrue(map.canMoveTo(new Vector2d(2,2)));
    }
    @Test
    void canMoveToOccupied() throws IncorrectPositionException{
        WorldMap map = new RectangularMap(5, 5);
        Animal animal1 = new Animal(new Vector2d(2,2));
        map.place(animal1);
        assertFalse(map.canMoveTo(new Vector2d(2,2)));
    }
    @Test
    void canMoveToOutOf(){
        WorldMap map = new RectangularMap(5, 5);
        assertFalse(map.canMoveTo(new Vector2d(6,5)));

    }
    @Test
    void place() throws IncorrectPositionException {
        WorldMap map = new RectangularMap(5, 5);
        Animal animal1 = new Animal();
        Animal animal2 = new Animal();
        Animal animal3 = new Animal(new Vector2d(6,6));
        assertTrue(map.place(animal1));
        assertThrows(IncorrectPositionException.class, ()->{map.place(animal2);});
        assertThrows(IncorrectPositionException.class, ()->{map.place(animal3);});

    }

    @Test
    void moveTo() throws IncorrectPositionException {
        WorldMap map = new RectangularMap(5, 5);
        Animal animal1 = new Animal(new Vector2d(1,1));
        Animal animal2 = new Animal(new Vector2d(1, 3));
        map.place(animal1);
        map.place(animal2);
        map.move(animal1,MoveDirection.FORWARD);
        assertEquals(new Vector2d(1,2),animal1.getPosition());
        map.move(animal2,MoveDirection.BACKWARD);
        assertEquals(new Vector2d(1,3),animal2.getPosition());
        assertFalse(map.isOccupied(new Vector2d(1,1)));

    }

    @Test
    void isOccupied() throws IncorrectPositionException{
        WorldMap map = new RectangularMap(5, 5);
        Animal animal1 = new Animal(new Vector2d(1,1));
        Animal animal2 = new Animal(new Vector2d(1, 1));
        assertFalse(map.isOccupied(new Vector2d(1,1)));
        map.place(animal1);
        assertTrue(map.isOccupied(new Vector2d(1,1)));
    }

    @Test
    void objectAt() throws IncorrectPositionException{
        WorldMap map = new RectangularMap(5, 5);
        Animal animal1 = new Animal();
        Animal animal2 = new Animal(new Vector2d(2, 3));
        map.place(animal1);
        map.place(animal2);
        assertEquals(animal1, map.objectAt(new Vector2d(2, 2)));
        assertEquals(animal2, map.objectAt(new Vector2d(2, 3)));
        assertNull(map.objectAt(new Vector2d(3, 3)));
    }

    @Test
    void testToString()throws IncorrectPositionException {
        WorldMap map = new RectangularMap(5, 5);
        Animal animal1 = new Animal();
        Animal animal2 = new Animal(new Vector2d(2, 4));
        map.place(animal1);
        map.place(animal2);
        String mapVisualisation = " y\\x  0 1 2 3 4\r\n" +
                "  5: -----------\r\n" +
                "  4: | | |^| | |\r\n" +
                "  3: | | | | | |\r\n" +
                "  2: | | |^| | |\r\n" +
                "  1: | | | | | |\r\n" +
                "  0: | | | | | |\r\n" +
                " -1: -----------\r\n";
        assertEquals(mapVisualisation,map.toString());

    }
}