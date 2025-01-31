package agh.ics.oop.model;

import java.util.*;

import static java.lang.Math.min;

public class Animal implements WorldElement {
    private MapDirection orientation;
    private Vector2d position;
    private int energy;
    //    private WorldMap map;
    private int age;
    private final Genomes genomes;
    private int active; // co active? czy to nie powinna być część genomu?
    private UUID id = UUID.randomUUID();
    private final int minimumEnergyToReproduce;
    private final int energyGivenToChild;
    private List<Animal> childrens = new ArrayList<>(); // liczba podwójnie mnoga
    private final int genesLength;
    private static final Random random = new Random();
    private int eatenGrass = 0;
    private int deathDay = -1; // wyjątkowo polecam Integer


    public Animal(Vector2d position, int energy, int genesLength, int minimumEnergyToReproduce, int energyGivenToChild, Genomes genomes) {
        this.position = position;
        this.orientation = MapDirection.getRandomDirection();
        this.genomes = genomes;
        this.energy = energy;
        this.genesLength = genesLength;
        this.minimumEnergyToReproduce = minimumEnergyToReproduce;
        this.energyGivenToChild = energyGivenToChild;
        this.childrens = new ArrayList<>();
        this.active = random.nextInt(genesLength);
    }

    public Genomes getGenomes() {
        return this.genomes;
    }

    public void onChildCreated(Animal child) {
        this.childrens.add(child);
        this.energy = this.energy - this.energyGivenToChild;
    }

    public int getEatenGrass() {
        return this.eatenGrass;
    }

    public void setEatenGrass() {
        this.eatenGrass += 1;
    }

    public void setDeathDay(int day) {
        this.deathDay = day;
    }

    public int getDeathDay() {
        return this.deathDay;
    }

    public Animal reproduce(Animal secondParent) {

        Animal child = new Animal(this.position, this.energyGivenToChild * 2, genesLength, minimumEnergyToReproduce, energyGivenToChild, new Genomes(this, secondParent));
        this.onChildCreated(child);
        secondParent.onChildCreated(child);
        return child;
    }

    @Override
    public String toString() {
        return orientation.toString();
    }

    public String toImage(int width, int height, boolean isDominant) {

        String color = "#b7b4a1";
        if (isDominant) {
            color = "#222222";
        }
        String configuration = "-fx-background-color: " + color + ";" +
                "-fx-pref-width: " + width + ";" +
                "-fx-pref-height: " + height + ";" +
                "-fx-background-size: contain; ";


        if (this.getEnergy() > 20) {
            return configuration + "-fx-background-image: url('images/1.png'); ";
        } else if (this.getEnergy() > 15 && this.getEnergy() <= 20) {
            return configuration + "-fx-background-image: url('images/2.png'); ";
        } else if (this.getEnergy() > 10 && this.getEnergy() <= 15) {
            return configuration + "-fx-background-image: url('images/3.png'); ";
        } else if (this.getEnergy() > 5 && this.getEnergy() <= 10) {
            return configuration + "-fx-background-image: url('images/4.png'); ";
        } else {
            return configuration + "-fx-background-image: url('images/5.png'); ";
        }
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

    public void getOlder() {
        this.age = this.age + 1;
    }

    public boolean toOldToMove() { // myląca nazwa
        int randomNumber = random.nextInt(99);// losujemy liczbe od 0 do 99
        return randomNumber > min(this.age, 79);
    }

    public int getActive() {
        return this.active;
    }

    public int getMinimumEnergyToReproduce() {
        return this.minimumEnergyToReproduce;
    }

    public void nextGene() {
        this.active = (active + 1) % genesLength;
    }

    public List<Animal> getChildrens() {
        return this.childrens; // dehermetyzacja
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

    public void move(MoveValidator validator, MapDirection direction, Globe map) {
        this.orientation = direction;
        Vector2d currentPosition = orientation.toUnitVector().add(position);
        if (!validator.canMoveUpOrDown(currentPosition)) {
            this.orientation = this.orientation.opposite();
        } else if (!validator.canMoveRightOrLeft(currentPosition)) {
            this.position = this.position.switchWidth(map.getWidth() - 1);
        } else {
            this.position = currentPosition;
        }
    }

    private void setOffspringCount(Animal animal, Set<Animal> offsprings) {
        for (Animal child : this.getChildrens()) {
            if (!offsprings.contains(child)) {
                offsprings.add(child);
                child.setOffspringCount(child, offsprings);
            }
        }
    }

    public int getOffspringCount() {
        Set<Animal> offsprings = new HashSet<>();
        setOffspringCount(this, offsprings);
        return offsprings.size();
    }

}
