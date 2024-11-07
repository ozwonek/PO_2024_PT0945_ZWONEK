package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private final List<Animal> animals;
    private final List<MoveDirection> moves;

    public Simulation(List<Vector2d> animalOnPosition,List<MoveDirection> moves){
            this.moves = moves;
            this.animals = new ArrayList<>();
            for(Vector2d position: animalOnPosition){
                animals.add(new Animal(position));
            }
    }
    public List<Animal> getAnimals(){
        return this.animals;
    }
    public void run(){
        int numberOfAnimal=0;
        for(MoveDirection move: moves){
            int sizeOfAnimals = animals.size();
            animals.get(numberOfAnimal).move(move);
            System.out.println("Zwierze " + numberOfAnimal +": "+animals.get(numberOfAnimal).toString());
            numberOfAnimal = (numberOfAnimal+1)%sizeOfAnimals;

            }
        }

    }

