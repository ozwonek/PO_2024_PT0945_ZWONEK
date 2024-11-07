package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {

    @Test
    void orientationAndPositionAfterLeft(){
        List<Vector2d> positions = List.of(new Vector2d(2,2));
        List<MoveDirection> moves = new ArrayList<>();
        moves.add(MoveDirection.LEFT);
        Simulation s1 = new Simulation(positions,moves);
        s1.run();
        Animal animal = s1.getAnimals().get(0);
        assertEquals(MapDirection.WEST,animal.getOrientation());
        assertEquals(new Vector2d(2,2),animal.getPosition());
    }
    @Test
    void orientationAndPositionAfterRight(){
        List<Vector2d> positions = List.of(new Vector2d(2,2));
        List<MoveDirection> moves = new ArrayList<>();
        moves.add(MoveDirection.RIGHT);
        Simulation s1 = new Simulation(positions,moves);
        s1.run();
        Animal animal = s1.getAnimals().get(0);
        assertEquals(MapDirection.EAST,animal.getOrientation());
        assertEquals(new Vector2d(2,2),animal.getPosition());
    }
    @Test
    void orientationAndPositionAfterForward(){
        List<Vector2d> positions = List.of(new Vector2d(2,2));
        List<MoveDirection> moves = new ArrayList<>();
        moves.add(MoveDirection.FORWARD);
        Simulation s1 = new Simulation(positions,moves);
        s1.run();
        Animal animal = s1.getAnimals().get(0);
        assertEquals(MapDirection.NORTH,animal.getOrientation());
        assertEquals(new Vector2d(2,3),animal.getPosition());
    }
    @Test
    void orientationAndPositionAfterBackward(){
        List<Vector2d> positions = List.of(new Vector2d(2,2));
        List<MoveDirection> moves = new ArrayList<>();
        moves.add(MoveDirection.BACKWARD);
        Simulation s1 = new Simulation(positions,moves);
        s1.run();
        Animal animal = s1.getAnimals().get(0);
        assertEquals(MapDirection.NORTH,animal.getOrientation());
        assertEquals(new Vector2d(2,1),animal.getPosition());
    }
    @Test
    void orientationAndPositionAfterMoveOutOfMapRightUpperCorner(){
        List<Vector2d> positions = List.of(new Vector2d(4,4));
        List<MoveDirection> moves = new ArrayList<>();
        moves.add(MoveDirection.FORWARD);
        Simulation s1 = new Simulation(positions,moves);
        s1.run();
        Animal animal = s1.getAnimals().get(0);
        assertEquals(MapDirection.NORTH,animal.getOrientation());
        assertEquals(new Vector2d(4,4),animal.getPosition());

    }
    @Test
    void orientationAndPositionAfterMoveOutOfMapRightDownCorner(){
        List<Vector2d> positions = List.of(new Vector2d(4,0));
        List<MoveDirection> moves = new ArrayList<>();
        moves.add(MoveDirection.BACKWARD);
        Simulation s1 = new Simulation(positions,moves);
        s1.run();
        Animal animal = s1.getAnimals().get(0);
        assertEquals(MapDirection.NORTH,animal.getOrientation());
        assertEquals(new Vector2d(4,0),animal.getPosition());
    }
    @Test
    void orientationAndPositionAfterMoveOutOfMapLeftDownCorner(){
        List<Vector2d> positions = List.of(new Vector2d(0,0));
        List<MoveDirection> moves = new ArrayList<>();
        moves.add(MoveDirection.BACKWARD);
        Simulation s1 = new Simulation(positions,moves);
        s1.run();
        Animal animal = s1.getAnimals().get(0);
        assertEquals(MapDirection.NORTH,animal.getOrientation());
        assertEquals(new Vector2d(0,0),animal.getPosition());
    }
    @Test
    void orientationAndPositionAfterMoveOutOfMapLeftUpperCorner(){
        List<Vector2d> positions = List.of(new Vector2d(0,4));
        List<MoveDirection> moves = new ArrayList<>();
        moves.add(MoveDirection.FORWARD);
        Simulation s1 = new Simulation(positions,moves);
        s1.run();
        Animal animal = s1.getAnimals().get(0);
        assertEquals(MapDirection.NORTH,animal.getOrientation());
        assertEquals(new Vector2d(0,4),animal.getPosition());
    }
    @Test
    void orientationAndPositionAfterMoveOutOfMapNotInCorner(){
        List<Vector2d> positions = List.of(new Vector2d(3,4));
        List<MoveDirection> moves = new ArrayList<>();
        moves.add(MoveDirection.FORWARD);
        Simulation s1 = new Simulation(positions,moves);
        s1.run();
        Animal animal = s1.getAnimals().get(0);
        assertEquals(MapDirection.NORTH,animal.getOrientation());
        assertEquals(new Vector2d(3,4),animal.getPosition());
    }
    @Test
    void orienationAndPositionAfterSequencesMovesForOneAnimal(){
        List<Vector2d> positions = List.of(new Vector2d(2,2));
        List<MoveDirection> moves = new ArrayList<>();
        moves.add(MoveDirection.BACKWARD);
        moves.add(MoveDirection.FORWARD);
        moves.add(MoveDirection.FORWARD);
        moves.add(MoveDirection.LEFT);
        Simulation s1 = new Simulation(positions,moves);
        s1.run();
        Animal animal = s1.getAnimals().get(0);
        assertEquals(MapDirection.WEST,animal.getOrientation());
        assertEquals(new Vector2d(2,3),animal.getPosition());

    }
    @Test
    void orienationAndPositionAfterSequencesMovesForMoreAnimals(){
        List<Vector2d> positions = List.of(new Vector2d(2,2),new Vector2d(0,0));
        List<MoveDirection> moves = new ArrayList<>();
        moves.add(MoveDirection.BACKWARD);
        moves.add(MoveDirection.FORWARD);
        moves.add(MoveDirection.FORWARD);
        moves.add(MoveDirection.LEFT);
        Simulation s1 = new Simulation(positions,moves);
        s1.run();
        Animal animal1 = s1.getAnimals().get(0);
        Animal animal2 = s1.getAnimals().get(1);
        assertEquals(MapDirection.NORTH,animal1.getOrientation());
        assertEquals(new Vector2d(2,2),animal1.getPosition());
        assertEquals(MapDirection.WEST,animal2.getOrientation());
        assertEquals(new Vector2d(0,1),animal2.getPosition());
    }
    @Test
    void orienationAndPositionAfterSequencesMovesWithParsingMovesOfInput(){
        List<Vector2d> positions = List.of(new Vector2d(2,2),new Vector2d(3,4));
        List<MoveDirection> moves = new ArrayList<>();
        String[] move = {"f", "b", "r","l","f", "f", "r","r" ,"f", "f" ,"f", "f" ,"f" ,"f" ,"f" ,"f"};
        List<MoveDirection> movesOfAnimals = OptionsParser.parse(move);
        Simulation s1 = new Simulation(positions,movesOfAnimals);
        s1.run();
        Animal animal1 = s1.getAnimals().get(0);
        Animal animal2 = s1.getAnimals().get(1);
        assertEquals(MapDirection.SOUTH,animal1.getOrientation());
        assertEquals(new Vector2d(3,0),animal1.getPosition());
        assertEquals(MapDirection.NORTH,animal2.getOrientation());
        assertEquals(new Vector2d(2,4),animal2.getPosition());
    }



}