package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {

    @Test
    void next(){
        assertEquals(MapDirection.EAST.next(),MapDirection.SOUTH);
        assertEquals(MapDirection.SOUTH.next(),MapDirection.WEST);
        assertEquals(MapDirection.WEST.next(),MapDirection.NORTH);
        assertEquals(MapDirection.NORTH.next(),MapDirection.EAST);
    }
    @Test
    void previous(){
        assertEquals(MapDirection.EAST,MapDirection.SOUTH.previous());
        assertEquals(MapDirection.SOUTH,MapDirection.WEST.previous());
        assertEquals(MapDirection.WEST,MapDirection.NORTH.previous());
        assertEquals(MapDirection.NORTH,MapDirection.EAST.previous());
    }


}