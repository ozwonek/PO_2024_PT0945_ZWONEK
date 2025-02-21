package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {
    @Test
    void twoObjectsEquals(){
        Vector2d v1 = new Vector2d(0,0);
        Vector2d v2 = new Vector2d(1,2);
        Vector2d v3 = new Vector2d(1,2);

        assertNotEquals(1, v1);
        assertEquals(v1, v1);
        assertEquals(v2, v3);
        assertNotEquals(v1, v2);
    }
    @Test
    void convertToString(){
        Vector2d v1 = new Vector2d(1,1);
        assertEquals("(1,1)", v1.toString());
    }
    @Test
    void proceedes(){
        Vector2d v1 = new Vector2d(0,0);
        Vector2d v2 = new Vector2d(1,1);
        Vector2d v3 = new Vector2d(-1,1);

        assertTrue(v1.proceeds(v2));
        assertFalse(v2.proceeds(v1));
        assertFalse(v2.proceeds(v3));
        assertTrue(v1.proceeds(v1));
    }
    @Test
    void follows(){
        Vector2d v1 = new Vector2d(0,0);
        Vector2d v2 = new Vector2d(2,1);
        Vector2d v3 = new Vector2d(1,-2);

        assertTrue(v2.follows(v1));
        assertFalse(v1.follows(v2));
        assertTrue(v2.follows(v3));
        assertTrue(v1.follows(v1));
    }

    @Test
    void add(){
        Vector2d v1 = new Vector2d(0,0);
        Vector2d v2 = new Vector2d(1,1);
        Vector2d v3 = new Vector2d(-1,-1);

        assertEquals(v1,v3.add(v2));
        assertEquals(v1,v1.add(v1));
    }
    @Test
    void substract(){
        Vector2d v1 = new Vector2d(0,0);
        Vector2d v2 = new Vector2d(1,1);
        Vector2d v3 = new Vector2d(-1,-1);

        assertEquals(v3,v1.substract(v2));
        assertEquals(v1,v1.substract(v1));
    }

}