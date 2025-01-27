package agh.ics.oop.model;

import agh.ics.oop.Statistics;
import agh.ics.oop.model.util.Config;

import java.util.*;

import static agh.ics.oop.model.Vector2d.MOVEMENT_VECTORS;

public class Globe extends Population{
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final Set<Vector2d> notPrefferedSpots = new HashSet<>();
    private final Set<Vector2d> prefferedSpots = new HashSet<>();
    public Globe(Config worldConfig){
        super(worldConfig);
        int lowerEquator = (int) (0.4 * worldConfig.mapHeight());
        int upperEquator = (int) (0.6 * worldConfig.mapHeight());
        for (int i = 0; i < worldConfig.mapHeight(); i++) {
            for (int j = 0; j < worldConfig.mapWidth(); j++) {
                if (lowerEquator<=j && j <= upperEquator){
                    prefferedSpots.add(new Vector2d(i,j));
                }
                else {
                    notPrefferedSpots.add(new Vector2d(i,j));
                }
            }
        }
        growGrass(worldConfig.grassStart());

    }
    public void addObserver(MapChangeListener listener){
        observers.add(listener);
    }
    protected void mapChange(String message){
        for(MapChangeListener observer: observers){
            observer.mapChanged(this,message);
        }
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
        mapChange("Halo");

    }
    public boolean isPreferred(Vector2d position){
        System.out.println(prefferedSpots);
        System.out.println(position);
        return prefferedSpots.contains(position);
    }
    public void allEat() {
        List<Vector2d> eatenGrassSpots = new ArrayList<>();
        for (List<Animal> onOneSpot : animals.values()) {
            Vector2d position = onOneSpot.getFirst().getPosition();
            if (this.isGrass(position)) {
                eatenGrassSpots.add(position);
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
        removeGrass(eatenGrassSpots);
    }
    public void growGrass(int numberOfGrasses){
        int placedGrasses = 0;
        int countFromPreffered = 0;
        int countFromNotPreffered = 0;
        while(placedGrasses <numberOfGrasses){
            if(notPrefferedSpots.size()-countFromNotPreffered==0){
                countFromPreffered += Math.min(prefferedSpots.size()-countFromPreffered,numberOfGrasses-placedGrasses);
                break;
            }
            if(prefferedSpots.size()-countFromPreffered==0){
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
        growChoosenSpots(prefferedSpots,countFromPreffered);
        growChoosenSpots(notPrefferedSpots,countFromNotPreffered);

    }
    private void growChoosenSpots(Set<Vector2d> placedSet, int numberToGrow){
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
                prefferedSpots.add(spot.add(direction));
            }
        }

        prefferedSpots.remove(spot);
        notPrefferedSpots.remove(spot);
    }
    public void removeGrass(List<Vector2d> spots){
        for(Vector2d spot: spots){
            boolean neighbour = false;
            for (Vector2d direction : MOVEMENT_VECTORS) {
                Vector2d neighbourSpot = direction.add(spot);
                if(grasses.containsKey(neighbourSpot)){
                    neighbour = true;
                }
            }
            if((spot.getY() <= (int) (0.6 * worldConfig.mapHeight()) && spot.getY() >= (int) (0.4 * worldConfig.mapHeight())) || neighbour){
                prefferedSpots.add(spot);
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
                    prefferedSpots.remove(neighbourSpot);
                }
            }
        }


    }
    public boolean isOccupied(Vector2d spot){
        return animals.containsKey(spot) || grasses.containsKey(spot);
    }
    public void eatGrass(Animal animal) {
        animal.setEnergy(animal.getEnergy() + worldConfig.grassEnergy());
        grasses.remove(animal.getPosition());

    }
    public int getGrassPerDay() {
        return this.worldConfig.grassDaily();
    }

    public boolean isGrass(Vector2d position) {
        return grasses.get(position) != null;
    }

    public String toImage(int width,int height){
        String color = "#b7b4a1";

        return "-fx-background-color: "+ color +";" +
                "-fx-pref-width: " + width + ";" +
                "-fx-pref-height: " + height + ";" +
                "-fx-background-image: url('images/6.png'); "
                + "-fx-background-size: contain; ";
    }
    public int getGrassSize(){
        return grasses.size();
    }
    public int freeSpotsLeft(){
        int freeSpots = 0;
        for(int i=0;i<worldConfig.mapWidth();i++)
        {
            for(int j=0;j<worldConfig.mapHeight();j++){

                Vector2d spot = new Vector2d(i,j);
                if(!isOccupied(spot)){
                    freeSpots++;
                }
            }
        }
        return freeSpots;
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
                meanForDead());
    }
    public Statistics getStats(){
        return stats;
    }

}
