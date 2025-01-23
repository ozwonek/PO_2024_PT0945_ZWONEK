package agh.ics.oop.model;

import agh.ics.oop.World;

import java.util.*;

import static java.lang.Math.min;

public class Animal implements WorldElement {
    private MapDirection orientation;
    private Vector2d position;
    private int energy;
    private WorldMap map;
    private int age;
    private final Genomes genomes;
    private int active;
    private UUID id = UUID.randomUUID();
    private final int minimumToBeFull;
    private final int giveToChild;
    private List<Animal> childrens = new ArrayList<>();
    private final int genesLength;
    private static final Random random = new Random();


    public Animal(Vector2d position, int energy, int genesLength, int minimumToBeFull, int giveToChild, Genomes genomes) {
        this.position = position;
        this.orientation = MapDirection.getRandomDirection();
        this.genomes = genomes;
        this.energy = energy;
        this.genesLength = genesLength;
        this.minimumToBeFull = minimumToBeFull;
        this.giveToChild = giveToChild;
        this.childrens = new ArrayList<>();
        this.active = random.nextInt(genesLength);
    }

    public Genomes getGenomes() {
        return this.genomes;
    }

    public Animal reproduce(Animal secondParent) {

        Animal child = new Animal(this.position, this.giveToChild * 2, genesLength, minimumToBeFull, giveToChild, new Genomes(this, secondParent));
        childrens.add(child);
        energy = energy - giveToChild;
        secondParent.setEnergy(secondParent.getEnergy() - giveToChild);
        secondParent.getChildrens().add(child);
        return child;
    }

    @Override
    public String toString() {
        return orientation.toString();
    }

    public UUID getId() {
        return this.id;
    }

    public int getEnergy() {
        return this.energy;
    }

    public void setEnergy(int newEnergy) {
        this.energy = newEnergy;
    }

    public int getAge() {
        return this.age;
    }

    public void getOlder(){
        this.age = this.age + 1;
    }
    public boolean toOldToMove(){
        int randomNumber = random.nextInt(99);// losujemy liczbe od 0 do 99
        return randomNumber > min(this.age, 79);
        }


    public void setAge(int newAge) {
        this.age = newAge;
    }

    public int getActive() {
        return this.active;
    }

    public int getMinimumToBeFull() {
        return this.minimumToBeFull;
    }

    public void nextGene() {
        this.active = (active + 1) % genesLength;
    }

    public List<Animal> getChildrens() {
        return this.childrens;
    }

    public int getChildrenSize() {
        return this.childrens.size();
    }

    public MapDirection getOrientation() {
        return this.orientation;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public void move(MoveValidator validator, MapDirection direction, GrassField map) {
        this.orientation = direction;
        Vector2d currentPosition = orientation.toUnitVector().add(position);
        if (!validator.canMoveUpOrDown(currentPosition)) {
            this.orientation = this.orientation.opposite();
        } else if (!validator.canMoveRightOrLeft(currentPosition)) {
            this.position = this.position.switchWidth(map.width - 1);
        } else {
            this.position = currentPosition;
        }
    }

    public boolean isFuller(Animal other) {
        return this.energy >= other.getEnergy();
    }

}