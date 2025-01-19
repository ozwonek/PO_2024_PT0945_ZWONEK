//package agh.ics.oop;
//
//import agh.ics.oop.model.*;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class SimulationTest {
//
//    @Test
//    void orientationAndPositionAfterLeft(){
//        List<Vector2d> positions = List.of(new Vector2d(2,2));
//        List<MoveDirection> moves = List.of(MoveDirection.LEFT);
//        WorldMap map = new RectangularMap(5,5);
//        Simulation s1 = new Simulation(positions,moves,map);
//        s1.run();
//        Animal animal = s1.getAnimals().getFirst();
//        assertEquals(MapDirection.WEST,animal.getOrientation());
//        assertEquals(new Vector2d(2,2),animal.getPosition());
//    }
//    @Test
//    void orientationAndPositionAfterRight(){
//        List<Vector2d> positions = List.of(new Vector2d(2,2));
//        List<MoveDirection> moves = List.of(MoveDirection.RIGHT);
//        RectangularMap map = new RectangularMap(5,5);
//        Simulation s1 = new Simulation(positions,moves,map);
//        s1.run();
//        Animal animal = s1.getAnimals().get(0);
//        assertEquals(MapDirection.EAST,animal.getOrientation());
//        assertEquals(new Vector2d(2,2),animal.getPosition());
//    }
//    @Test
//    void orientationAndPositionAfterForward(){
//        List<Vector2d> positions = List.of(new Vector2d(2,2));
//        List<MoveDirection> moves = List.of(MoveDirection.FORWARD);
//        RectangularMap map = new RectangularMap(5,5);
//        Simulation s1 = new Simulation(positions,moves,map);
//        s1.run();
//        Animal animal = s1.getAnimals().get(0);
//        assertEquals(MapDirection.NORTH,animal.getOrientation());
//        assertEquals(new Vector2d(2,3),animal.getPosition());
//    }
//    @Test
//    void orientationAndPositionAfterBackward(){
//        List<Vector2d> positions = List.of(new Vector2d(2,2));
//        List<MoveDirection> moves = List.of(MoveDirection.BACKWARD);
//        RectangularMap map = new RectangularMap(5,5);
//        Simulation s1 = new Simulation(positions,moves,map);
//        s1.run();
//        Animal animal = s1.getAnimals().get(0);
//        assertEquals(MapDirection.NORTH,animal.getOrientation());
//        assertEquals(new Vector2d(2,1),animal.getPosition());
//    }
//    @Test
//    void orientationAndPositionAfterMoveOutOfMapRightUpperCorner(){
//        List<Vector2d> positions = List.of(new Vector2d(4,4));
//        List<MoveDirection> moves = List.of(MoveDirection.FORWARD);
//        RectangularMap map = new RectangularMap(5,5);
//        Simulation s1 = new Simulation(positions,moves,map);
//        s1.run();
//        Animal animal = s1.getAnimals().get(0);
//        assertEquals(MapDirection.NORTH,animal.getOrientation());
//        assertEquals(new Vector2d(4,4),animal.getPosition());
//
//    }
//    @Test
//    void orientationAndPositionAfterMoveOutOfMapRightDownCorner(){
//        List<Vector2d> positions = List.of(new Vector2d(4,0));
//        List<MoveDirection> moves = List.of(MoveDirection.BACKWARD);
//        RectangularMap map = new RectangularMap(5,5);
//        Simulation s1 = new Simulation(positions,moves,map);
//        s1.run();
//        Animal animal = s1.getAnimals().get(0);
//        assertEquals(MapDirection.NORTH,animal.getOrientation());
//        assertEquals(new Vector2d(4,0),animal.getPosition());
//    }
//    @Test
//    void orientationAndPositionAfterMoveOutOfMapLeftDownCorner(){
//        List<Vector2d> positions = List.of(new Vector2d(0,0));
//        List<MoveDirection> moves = List.of(MoveDirection.BACKWARD);
//        RectangularMap map = new RectangularMap(5,5);
//        Simulation s1 = new Simulation(positions,moves,map);
//        s1.run();
//        Animal animal = s1.getAnimals().get(0);
//        assertEquals(MapDirection.NORTH,animal.getOrientation());
//        assertEquals(new Vector2d(0,0),animal.getPosition());
//    }
//    @Test
//    void orientationAndPositionAfterMoveOutOfMapLeftUpperCorner(){
//        List<Vector2d> positions = List.of(new Vector2d(0,4));
//        List<MoveDirection> moves = List.of(MoveDirection.FORWARD);
//        RectangularMap map = new RectangularMap(5,5);
//        Simulation s1 = new Simulation(positions,moves,map);
//        s1.run();
//        Animal animal = s1.getAnimals().get(0);
//        assertEquals(MapDirection.NORTH,animal.getOrientation());
//        assertEquals(new Vector2d(0,4),animal.getPosition());
//    }
//    @Test
//    void orientationAndPositionAfterMoveOutOfMapNotInCorner(){
//        List<Vector2d> positions = List.of(new Vector2d(3,4));
//        List<MoveDirection> moves = List.of(MoveDirection.FORWARD);
//        RectangularMap map = new RectangularMap(5,5);
//        Simulation s1 = new Simulation(positions,moves,map);
//        s1.run();
//        Animal animal = s1.getAnimals().get(0);
//        assertEquals(MapDirection.NORTH,animal.getOrientation());
//        assertEquals(new Vector2d(3,4),animal.getPosition());
//    }
//    @Test
//    void orienationAndPositionAfterSequencesMovesForOneAnimal(){
//        List<Vector2d> positions = List.of(new Vector2d(2,2));
//        List<MoveDirection> moves = List.of(MoveDirection.BACKWARD,MoveDirection.FORWARD,MoveDirection.FORWARD,MoveDirection.LEFT);
//        RectangularMap map = new RectangularMap(5,5);
//        Simulation s1 = new Simulation(positions,moves,map);
//        s1.run();
//        Animal animal = s1.getAnimals().get(0);
//        assertEquals(MapDirection.WEST,animal.getOrientation());
//        assertEquals(new Vector2d(2,3),animal.getPosition());
//
//    }
//    @Test
//    void orienationAndPositionAfterSequencesMovesForMoreAnimals(){
//        List<Vector2d> positions = List.of(new Vector2d(2,2),new Vector2d(0,0));
//        List<MoveDirection> moves = List.of(MoveDirection.BACKWARD,MoveDirection.FORWARD,MoveDirection.FORWARD,MoveDirection.LEFT,MoveDirection.RIGHT);
//        RectangularMap map = new RectangularMap(5,5);
//        Simulation s1 = new Simulation(positions,moves,map);
//        s1.run();
//        Animal animal1 = s1.getAnimals().get(0);
//        Animal animal2 = s1.getAnimals().get(1);
//        assertEquals(MapDirection.EAST,animal1.getOrientation());
//        assertEquals(new Vector2d(2,2),animal1.getPosition());
//        assertEquals(MapDirection.WEST,animal2.getOrientation());
//        assertEquals(new Vector2d(0,1),animal2.getPosition());
//    }
//    @Test
//    void twoAnimalsOnOnePlace(){
//        List<Vector2d> positions = List.of(new Vector2d(2,2),new Vector2d(2,4));
//        List<MoveDirection> moves = List.of(MoveDirection.FORWARD,MoveDirection.BACKWARD);
//        RectangularMap map = new RectangularMap(5,5);
//        Simulation s1 = new Simulation(positions,moves,map);
//        s1.run();
//        Animal animal1 = s1.getAnimals().get(0);
//        Animal animal2 = s1.getAnimals().get(1);
//        assertEquals(MapDirection.NORTH,animal1.getOrientation());
//        assertEquals(new Vector2d(2,3),animal1.getPosition());
//        assertEquals(MapDirection.NORTH,animal2.getOrientation());
//        assertEquals(new Vector2d(2,4),animal2.getPosition());
//    }
//    @Test
//    void orienationAndPositionAfterSequencesMovesWithParsingMovesOfInput(){
//        List<Vector2d> positions = List.of(new Vector2d(2,2),new Vector2d(3,4));
//        String[] move = {"f", "b", "r","l","f", "f", "r","r" ,"f", "f" ,"f", "f" ,"f" ,"f" ,"f" ,"f"};
//        List<MoveDirection> movesOfAnimals = OptionsParser.parse(move);
//        RectangularMap map = new RectangularMap(5,5);
//        Simulation s1 = new Simulation(positions,movesOfAnimals,map);
//        s1.run();
//        Animal animal1 = s1.getAnimals().get(0);
//        Animal animal2 = s1.getAnimals().get(1);
//
//        assertEquals(MapDirection.SOUTH,animal1.getOrientation());
//        assertEquals(new Vector2d(2,0),animal1.getPosition());
//        assertEquals(MapDirection.NORTH,animal2.getOrientation());
//        assertEquals(new Vector2d(3,4),animal2.getPosition());
//    }
//
//
//
//
//
//}