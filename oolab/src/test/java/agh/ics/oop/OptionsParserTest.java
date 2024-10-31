package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionsParserTest {
    @Test
    void oneWrongMove() {
        String[] moves = {"x"};
        MoveDirection[] good = {};

        assertArrayEquals(good, OptionsParser.parse(moves));
    }

    @Test
    void emptyArrayMoves() {
        String[] moves = {};
        MoveDirection[] good = {};

        assertArrayEquals(good, OptionsParser.parse(moves));
    }

    @Test
    void wrongMoveInside() {
        String[] moves = {"f", "b", " x", "f"};
        MoveDirection[] good = {MoveDirection.FORWARD, MoveDirection.BACKWARD, MoveDirection.FORWARD};

        assertArrayEquals(good, OptionsParser.parse(moves));
    }

    @Test
    void moveLeft() {
        String[] moves = {"l"};
        MoveDirection[] good = {MoveDirection.LEFT};

        assertArrayEquals(good, OptionsParser.parse(moves));
    }

    @Test
    void moveRight() {
        String[] moves = {"r"};
        MoveDirection[] good = {MoveDirection.RIGHT};

        assertArrayEquals(good, OptionsParser.parse(moves));
    }

    @Test
    void moveForward() {
        String[] moves = {"f"};
        MoveDirection[] good = {MoveDirection.FORWARD};

        assertArrayEquals(good, OptionsParser.parse(moves));
    }

    @Test
    void moveBackward() {
        String[] moves = {"b"};
        MoveDirection[] good = {MoveDirection.BACKWARD};

        assertArrayEquals(good, OptionsParser.parse(moves));
    }
}