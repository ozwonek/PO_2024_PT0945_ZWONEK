package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OptionsParserTest {
    @Test
    void oneWrongMove() {
        String[] moves = {"x"};
        List<MoveDirection> good = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> {
            OptionsParser.parse(moves);
        });
    }

    @Test
    void emptyArrayMoves() {
        String[] moves = {};
        List<MoveDirection>  good = new ArrayList<>();

        assertEquals(good, OptionsParser.parse(moves));
    }


    @Test
    void moveLeft() {
        String[] moves = {"l"};

        List<MoveDirection> good = List.of(MoveDirection.LEFT);

        assertEquals(good, OptionsParser.parse(moves));

    }

    @Test
    void moveRight() {
        String[] moves = {"r"};
        List<MoveDirection> good = List.of(MoveDirection.RIGHT);

        assertEquals(good, OptionsParser.parse(moves));
    }

    @Test
    void moveForward() {
        String[] moves = {"f"};
        List<MoveDirection> good = List.of(MoveDirection.FORWARD);

        assertEquals(good, OptionsParser.parse(moves));
    }

    @Test
    void moveBackward() {
        String[] moves = {"b"};
        List<MoveDirection> good = List.of(MoveDirection.BACKWARD);

        assertEquals(good, OptionsParser.parse(moves));;
    }
}