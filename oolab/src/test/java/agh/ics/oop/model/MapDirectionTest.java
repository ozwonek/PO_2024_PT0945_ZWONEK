package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {

    @Test
    void next(){
        assertEquals(MapDirection.EAST.next(),MapDirection.SOUTHEAST);
        assertEquals(MapDirection.SOUTH.next(),MapDirection.SOUTHWEST);

    }
    @Test
    void previous(){
        assertEquals(MapDirection.NORTHWEST,MapDirection.NORTH.previous());
        assertEquals(MapDirection.NORTHEAST,MapDirection.EAST.previous());
    }


}