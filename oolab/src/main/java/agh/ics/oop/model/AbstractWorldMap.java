package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.Boundary;

import java.util.*;

import static java.lang.Math.min;

public abstract class AbstractWorldMap implements WorldMap {
    protected final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    protected final MapVisualizer visualizer = new MapVisualizer(this);
    protected final List<MapChangeListener> observers = new ArrayList<>();
    protected final UUID id = UUID.randomUUID();
    protected int width;
    protected int height;
    protected Vector2d lowerLeft = new Vector2d(0,0);
    protected Vector2d upperRight;
    protected int deadAnimalCount = 0;
    protected static final Random random = new Random();
    protected final Set<Vector2d> notPrefferedSpots = new HashSet<>();
    protected final Set<Vector2d> prefferedSpot = new HashSet<>();
    protected int deadAnimalsAge = 0;
    protected int animalsChildrenCount = 0;

    public AbstractWorldMap(int width, int height)
    {
        this.width = width;
        this.height = height;
        this.upperRight = new Vector2d(width-1, height-1);
    }

    @Override
    public boolean canMoveUpOrDown(Vector2d position) {
        return position.correctHeight(lowerLeft,upperRight);
    }

    @Override
    public boolean canMoveRightOrLeft(Vector2d position){
      return position.correctWidth(lowerLeft,upperRight);
    }

    public void addObserver(MapChangeListener listener){
        observers.add(listener);
    }

    public void removeObserver(MapChangeListener listener){
        observers.remove(listener);
    }

    protected void mapChange(String message){
        for(MapChangeListener observer: observers){
            observer.mapChanged(this,message);
        }
    }

    public double getAverageAnimalAge(){
        return (double) deadAnimalsAge /deadAnimalCount;
    }

    public int getAnimalsChildrenCount(){
        return animalsChildrenCount;
    }

    @Override
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
        return animals.size();
    }


    @Override
    public void move(Animal animal,MapDirection direction,GrassField map){

            if (animal.toOldToMove()){
                Vector2d oldPosition = animal.getPosition();
                if(animals.get(oldPosition)!=null){
                    animals.get(oldPosition).remove(animal);
                    if(animals.get(oldPosition).isEmpty()){
                        animals.remove(oldPosition);
                    }
                }
                animal.move(this, direction, map);
                place(animal);
            }
            animal.setEnergy(animal.getEnergy() - 1);
            animal.getOlder();
            mapChange("energia : " + animal.getEnergy());

        }

    @Override
    public void clean() {
        List<Vector2d> toDelatePositions = new ArrayList<>();
        for (List<Animal> onOneSpot : animals.values()) {
            List<Animal> toRemove = new ArrayList<>();
            Vector2d position = onOneSpot.getFirst().getPosition();
            for (Animal animal : onOneSpot) {
                if (animal.getEnergy() <= 0) {
                    toRemove.add(animal);
                    deadAnimalCount += 1;
                    deadAnimalsAge+=animal.getAge();
                    animalsChildrenCount-=animal.getChildrenSize();
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
            animalsChildrenCount+=2;
        }
    }

    public void allEat(GrassField map) {
        for (List<Animal> onOneSpot : animals.values()) {
            Vector2d position = onOneSpot.getFirst().getPosition();
            if (map.isGrass(position)) {
                List<Animal> competitors = getStronger(onOneSpot);

                if(competitors.size()>1){
                    int bestAnimal = 0;
                    int topEnergy = competitors.getFirst().getEnergy();
                    int topAge = competitors.getFirst().getAge();
                    int topChildrenSize = competitors.getFirst().getChildrenSize();

                    if(competitors.getFirst().getEnergy()==competitors.get(1).getEnergy() && competitors.getFirst().getAge()==competitors.get(1).getAge() && competitors.getFirst().getChildrenSize()==competitors.get(1).getChildrenSize()){
                        int index = random.nextInt(competitors.size());
                        map.eatGrass(competitors.get(index));
                    }
                    else{
                        map.eatGrass(competitors.getFirst());
                    }
                }
                else{
                    map.eatGrass(onOneSpot.getFirst());
                }
            }
        }
    }




    @Override
    public boolean isOccupied(Vector2d position){
        return objectAt(position) != null;
    }

    @Override
    public WorldElement objectAt(Vector2d position){
        if(animals.get(position) == null){
            return null;
        }
        return animals.get(position).getFirst();

    }

    @Override
    public List<WorldElement> getElements(){
        List<WorldElement> all = new ArrayList<>();
        for(List<Animal> animals: animals.values())
        {
            all.addAll(animals);
        }
        return all;
    }

    public List<Animal> getAnimals() {
        List<Animal> all = new ArrayList<>();
        for(List<Animal> animals: animals.values())
        {
            all.addAll(animals);
        }
        return all;
    }



    @Override
    public Boundary getCurrentBounds(){
        return new Boundary(lowerLeft,upperRight);
    }

    @Override
    public String toString(){
        Boundary boundary = getCurrentBounds();
        return visualizer.draw(boundary.bottomLeftCorner(),boundary.topRightCorner());
    }
    @Override
    public UUID getID(){
        return id;
    }
    public int getFreeElements(){
        return 0;
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
}
