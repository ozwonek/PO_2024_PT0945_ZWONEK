package agh.ics.oop.model;

import agh.ics.oop.Statistics;
import agh.ics.oop.model.util.Config;

import java.util.*;


public class Population implements MoveValidator {
    protected final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    protected final List<MapChangeListener> observers = new ArrayList<>();
    protected Vector2d lowerLeft = new Vector2d(0,0);
    protected Vector2d upperRight;
    protected int deadAnimalCount = 0;
    protected static final Random random = new Random();
    protected int deadAnimalsAge = 0;
    protected int animalsChildrenCount = 0;
    protected Map<Genomes,Integer> genCount = new HashMap<>();
    protected Genomes mostCommonGenom;
    protected int animalsAge = 0;
    protected Statistics stats = new Statistics();
    protected int dayCount = 0;
    protected Config worldConfig;



    public Population(Config worldConfig) {
        this.worldConfig = worldConfig;
        this.upperRight = new Vector2d(worldConfig.mapWidth()-1, worldConfig.mapHeight()-1);


    }
    public void addGenCount(Genomes genome) {
        genCount.put(genome,genCount.getOrDefault(genome,0) + 1);
        if(mostCommonGenom == null || genCount.get(genome)>genCount.get(mostCommonGenom)){
            mostCommonGenom = genome;
        }
    }
    public void nextDay(){
        dayCount+=1;
    }


    public int getWidth(){
        return this.worldConfig.mapWidth();
    }
    public int getHeight(){
        return this.worldConfig.mapHeight();
    }
    public List<Animal> getAnimals() {
        List<Animal> all = new ArrayList<>();
        for(List<Animal> animals: animals.values())
        {
            all.addAll(animals);
        }
        return all;
    }

    public boolean canMoveUpOrDown(Vector2d position) {
        return position.correctHeight(lowerLeft,upperRight);
    }
    public boolean canMoveRightOrLeft(Vector2d position){
        return position.correctWidth(lowerLeft,upperRight);
    }


    public void place(Animal animal){
        if(animals.get(animal.getPosition()) == null){
            List<Animal> onThisSpot = new ArrayList<>();
            onThisSpot.add(animal);
            animals.put(animal.getPosition(),onThisSpot);
        }
        else {
            animals.get(animal.getPosition()).add(animal);
        }

    }

    public int getAnimalsSize(){
        int countAnimals = 0;
        for(List<Animal> animals: animals.values()){
            for(Animal animal : animals){
                countAnimals +=1;
            }
        }
        return countAnimals;
    }



    private void animalDied(Animal animal){
        deadAnimalCount+=1;
        deadAnimalsAge+=animal.getAge();
        animalsAge +=animal.getAge();
        animalsChildrenCount-=animal.getChildrenSize();
        animal.setDeathDay(this.dayCount);
    }

    public void clean() {
        List<Vector2d> toDelatePositions = new ArrayList<>();
        for (List<Animal> onOneSpot : animals.values()) {
            List<Animal> toRemove = new ArrayList<>();
            Vector2d position = onOneSpot.getFirst().getPosition();
            for (Animal animal : onOneSpot) {
                if (animal.getEnergy() <= 0) {
                    toRemove.add(animal);
                    animalDied(animal);
                }
            }
            for (Animal animalToClean : toRemove ){
                onOneSpot.remove(animalToClean);

            }
            if (onOneSpot.isEmpty()) {
                toDelatePositions.add(position);
            }
        }
        for(Vector2d position: toDelatePositions){
            animals.remove(position);
        }
    }
    public void allReproduce(){
        for(List<Animal> onOneSpot: animals.values()){
            Vector2d position = onOneSpot.getFirst().getPosition();
            if(onOneSpot.size()<2){
                continue;
            }
            onOneSpot.sort((a,b) -> Integer.compare(a.getEnergy(), b.getEnergy()));
            int onOneSpotSize = onOneSpot.size();
            if(onOneSpot.get(onOneSpotSize-2).getEnergy()<onOneSpot.get(onOneSpotSize-2).getMinimumEnergyToReproduce()){
                continue;
            }
            Animal child = onOneSpot.get(onOneSpotSize-1).reproduce(onOneSpot.get(onOneSpotSize-2));
            place(child);
            addGenCount(child.getGenomes());
            animalsChildrenCount+=2;
        }
    }



    public Animal objectAt(Vector2d position) {
        if(animals.get(position) == null){
            return null;
        }
        return animals.get(position).getFirst();
    }



    public boolean isAnimal(Vector2d position){
        return animals.get(position) != null;
    }

    public List<Animal> getStronger(List<Animal> onOneSpot) {

        if(onOneSpot.size()>1){
            onOneSpot.sort(
                    Comparator.comparingInt(Animal::getEnergy).reversed()
                            .thenComparing(Comparator.comparingInt(Animal::getAge).reversed())
                            .thenComparingInt(Animal::getChildrenSize).reversed());

            int topEnergy = onOneSpot.getFirst().getEnergy();
            int topAge = onOneSpot.getFirst().getAge();
            int topChildrenSize = onOneSpot.getFirst().getChildrenSize();

            List<Animal> competitors = new ArrayList<>();
            for (Animal animal : onOneSpot) {
                if (animal.getEnergy() == topEnergy && animal.getAge() == topAge && animal.getChildrenSize() == topChildrenSize) {
                    competitors.add(animal);
                }
                if(competitors.size()>=2){
                    return competitors;
                }
                else{
                    List<Animal> secondCompetitors = new ArrayList<>();
                    int secondTopEnergy = onOneSpot.get(1).getEnergy();
                    int secondTopAge = onOneSpot.get(1).getAge();
                    int secondTopChildrenSize = onOneSpot.get(1).getChildrenSize();

                    for(Animal secondsAnimal: onOneSpot) {
                        if (secondsAnimal.getEnergy() == secondTopEnergy && secondsAnimal.getAge() == secondTopAge && secondsAnimal.getChildrenSize() == secondTopChildrenSize) {
                            secondCompetitors.add(secondsAnimal);
                        }
                    }
                    if(secondCompetitors.size()<2){
                        competitors.add( secondCompetitors.getFirst());
                        return secondCompetitors;
                    }
                    else{
                        int index = random.nextInt(secondCompetitors.size());
                        competitors.add(secondCompetitors.get(index));
                    }
                }
            }
            return competitors;
        }
        return onOneSpot;
    }

    public double meanEnergy(){
        int sumOfEnergy = 0;
        for(List<Animal> animals: animals.values()){
            for(Animal animal: animals){
                sumOfEnergy += animal.getEnergy();
        }
        }
        return (double) sumOfEnergy /getAnimalsSize();
    }
    public void updateSumOfYears(){
        this.animalsAge = animalsAge + getAnimalsSize();
    }
    public double meanChildrenCount(){
        return ((double) animalsChildrenCount /(2*getAnimalsSize()));
    }
    public double meanLifeForLiving(){
        updateSumOfYears();
        return (double) this.animalsAge / getAnimalsSize();
    }

}
