package agh.ics.oop.model;

//import agh.ics.oop.model.util.Boundary;
//import agh.ics.oop.model.util.MapVisualizer;

import agh.ics.oop.Statistics;

import java.util.*;
import java.util.stream.Collectors;

import static agh.ics.oop.model.Vector2d.*;
//import static jdk.internal.org.jline.utils.Colors.s;

public class Globe implements MoveValidator {
    private final int grassPerDay;
    private final int energyFromGrass;
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private final Set<Vector2d> occupiedSpots = new HashSet<>();
    private final List<MapChangeListener> observers = new ArrayList<>();
    private final UUID id = UUID.randomUUID();
    private int width;
    private int height;
    private Vector2d lowerLeft = new Vector2d(0,0);
    private Vector2d upperRight;
    private int deadAnimalCount = 0;
    private static final Random random = new Random();
    private final Set<Vector2d> notPrefferedSpots = new HashSet<>();
    private final Set<Vector2d> prefferedSpot = new HashSet<>();
    private int deadAnimalsAge = 0;
    private int animalsChildrenCount = 0;
    private Map<Genomes,Integer> genCount = new HashMap<>();
    private Genomes mostCommonGenom;
    private int animalsAge = 0;
    private Statistics stats = new Statistics();
    private int dayCount = 0;
    public Map<Genomes, Integer> getGenCount() {
        return genCount;
    }
    public void nextDay(){
        dayCount+=1;
    }

    public void addGenCount(Genomes genome) {
        genCount.put(genome,genCount.getOrDefault(genome,0) + 1);
        if(mostCommonGenom == null || genCount.get(genome)>genCount.get(mostCommonGenom)){
            mostCommonGenom = genome;
        }
    }

    public Globe(int numberOfGrasses, int grassPerDay, double probabilityToMakeJungle, int energyFromGrass, int width, int height) {
        this.width = width;
        this.height = height;
        this.energyFromGrass = energyFromGrass;
        this.grassPerDay = grassPerDay;
        this.upperRight = new Vector2d(width-1, height-1);
        int lowerEquator = (int) (0.4 * height);
        int upperEquator = (int) (0.6 * height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (lowerEquator<=j && j <= upperEquator){
                    prefferedSpot.add(new Vector2d(i,j));
                }
                else {
                    notPrefferedSpots.add(new Vector2d(i,j));
                }
            }
        }
        growGrass(numberOfGrasses);

    }

    public int getWidth(){
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }
    public boolean canMoveUpOrDown(Vector2d position) {
        return position.correctHeight(lowerLeft,upperRight);
    }
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

    public void place(Animal animal){
        occupiedSpots.add(animal.getPosition());
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


    public void move(Animal animal,MapDirection direction,Globe map){

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
                    animalsAge -= animal.getAge();
                    animalsChildrenCount-=animal.getChildrenSize();
                }
            }
            for (Animal animalToClean : toRemove ){
                onOneSpot.remove(animalToClean);


            }
            if (onOneSpot.isEmpty()) {
                if(!grasses.containsKey(position)){
                    occupiedSpots.remove(position);
                }
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


    public void allEat() {
        for (List<Animal> onOneSpot : animals.values()) {
            Vector2d position = onOneSpot.getFirst().getPosition();
            if (this.isGrass(position)) {
                List<Animal> competitors = getStronger(onOneSpot);

                if(competitors.size()>1){
                    int bestAnimal = 0;
                    int topEnergy = competitors.getFirst().getEnergy();
                    int topAge = competitors.getFirst().getAge();
                    int topChildrenSize = competitors.getFirst().getChildrenSize();

                    if(competitors.getFirst().getEnergy()==competitors.get(1).getEnergy() && competitors.getFirst().getAge()==competitors.get(1).getAge() && competitors.getFirst().getChildrenSize()==competitors.get(1).getChildrenSize()){
                        int index = random.nextInt(competitors.size());
                        this.eatGrass(competitors.get(index));
                        competitors.get(index).setEatenGrass();
                    }
                    else{
                        this.eatGrass(competitors.getFirst());
                        competitors.getFirst().setEatenGrass();
                    }
                }
                else{
                    this.eatGrass(onOneSpot.getFirst());
                    onOneSpot.getFirst().setEatenGrass();
                }
            }
        }
    }



    public void setAllOffsprings(){
        ArrayList<Animal> animalsArray = animals.values().stream()
                .flatMap(List::stream).sorted(Comparator.comparingInt(Animal::getOffspringCount)).collect(Collectors.toCollection(ArrayList::new));
        for (Animal animal : animalsArray ){
            int temporaryOffspringCount = 0;
            for(Animal child: animal.getChildrens())
                animal.setOffspringCount(temporaryOffspringCount+child.getChildrenSize());
            animal.setOffspringCount(animal.getOffspringCount()+animal.getChildrenSize());
        }
    }

    public void growGrass(int numberOfGrasses){
        int placedGrasses = 0;
        int countFromPreffered = 0;
        int countFromNotPreffered = 0;
        while(placedGrasses <numberOfGrasses){
            if(notPrefferedSpots.size()-countFromNotPreffered==0){
                countFromPreffered += Math.min(prefferedSpot.size()-countFromPreffered,numberOfGrasses-placedGrasses);
                break;
            }
            if(prefferedSpot.size()-countFromPreffered==0){
                countFromNotPreffered += Math.min(notPrefferedSpots.size()-countFromNotPreffered,numberOfGrasses-placedGrasses);
                break;
            }
            int randomNumber = random.nextInt(100);
            if(randomNumber < 80){
                countFromPreffered +=1;
            }
            else{
                countFromNotPreffered +=1;
            }
            placedGrasses +=1;
        }
        grawChoosenSpots(prefferedSpot,countFromPreffered);
        grawChoosenSpots(notPrefferedSpots,countFromNotPreffered);

    }

    private void grawChoosenSpots(Set<Vector2d> placedSet, int numberToGrow){
        List<Vector2d> choosen = new ArrayList<>(placedSet);
        Collections.shuffle(choosen);
        if(numberToGrow!=0){

            List<Vector2d> indicesChoosen  = choosen.subList(0,Math.min(numberToGrow,choosen.size()));
            for(Vector2d spot: indicesChoosen){
                addNewGrass(spot);
            }
        }

    }
    private void addNewGrass(Vector2d spot){
        this.grasses.put(spot,new Grass(spot));
        for (Vector2d direction : MOVEMENT_VECTORS) {
            if(spot.add(direction).follows(lowerLeft) && spot.add(direction).proceeds(upperRight)&& !grasses.containsKey(spot.add(direction))){
                prefferedSpot.add(spot.add(direction));

            }


        }
        occupiedSpots.add(spot);
        prefferedSpot.remove(spot);
        notPrefferedSpots.remove(spot);
    }
    public void removeGrass(Vector2d spot){
        grasses.remove(spot);

        if(spot.getY() <= (int) (0.6 * height) && spot.getY() >= (int) (0.4 * height)){
            prefferedSpot.add(spot);
        }
        else {
            notPrefferedSpots.add(spot);
        }
        for (Vector2d direction : MOVEMENT_VECTORS) {
            Vector2d neighbourSpot = direction.add(spot);
            boolean grassNeigbour = false;
            for(Vector2d nextDirection : MOVEMENT_VECTORS){
                if(grasses.containsKey(neighbourSpot.add(nextDirection))){
                    grassNeigbour = true;
                };

            }
            if(!grassNeigbour){
                prefferedSpot.remove(neighbourSpot);
            }
        }

    }

    public List<Animal> getAnimals() {
        List<Animal> all = new ArrayList<>();
        for(List<Animal> animals: animals.values())
        {
            all.addAll(animals);
        }
        return all;
    }
    public Animal objectAt(Vector2d position) {
        return animals.get(position).getFirst();
    }


    public int getGrassPerDay() {
        return this.grassPerDay;
    }

    public void eatGrass(Animal animal) {
        animal.setEnergy(animal.getEnergy() + energyFromGrass);
        removeGrass(animal.getPosition());

    }

//    public List<WorldElement> getElements() {
//        List<WorldElement> elements = super.getElements();
//        elements.addAll(grasses.values());
//        return elements;
//    }

    public boolean isGrass(Vector2d position) {
        return grasses.get(position) != null;
    }

    public boolean isAnimal(Vector2d position){
        return animals.get(position) != null;
    }

//    public String toString(){
//        return visualizer.draw(lowerLeft,upperRight);
//    }

    public UUID getID(){
        return id;
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
    public int getGrassSize(){
        return grasses.size();
    }
    public int freeSpotsLeft(){
        return height*width - occupiedSpots.size();
    }
    public double meanEnergy(){
        int sumOfEnergy = 0;
        for(List<Animal> animals: animals.values()){
            for(Animal animal: animals){
                sumOfEnergy += animal.getEnergy();
        }
        }
        System.out.println(sumOfEnergy + " " + getAnimalsSize() + " " + sumOfEnergy / getAnimalsSize());
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
    public void setStatistics(){

        stats.setStatistics(dayCount,
                getAnimalsSize(),
                getGrassSize(),
                freeSpotsLeft(),
                mostCommonGenom,
                meanLifeForLiving(),
                meanChildrenCount(),
                meanEnergy(),
                (double) deadAnimalsAge/deadAnimalCount);
    }
    public Statistics getStats(){
        return stats;
    }


//    @Override
//    public Boundary getCurrentBounds() {
//        List<WorldElement> elements = getElements();
//        Vector2d lowerLeftCorner = new Vector2d(0, 0);
//        Vector2d upperRightCorner = new Vector2d(this.width - 1, this.height - 1);
//        for (WorldElement element : elements) {
//            lowerLeftCorner = lowerLeftCorner.lowerLeft(element.getPosition());
//            upperRightCorner = upperRightCorner.upperRight(element.getPosition());
//        }
//        return new Boundary(lowerLeftCorner, upperRightCorner);
//    }
}
