package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

import java.util.*;

import static agh.ics.oop.model.Vector2d.*;

public class GrassField extends AbstractWorldMap {
    private final int grassPerDay;
    private final int energyFromGrass;
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
//  private double probabilityToMakeJungle;

    public GrassField(int numberOfGrasses, int grassPerDay, double probabilityToMakeJungle, int energyFromGrass, int width, int height) {
        super(width, height);
        this.energyFromGrass = energyFromGrass;
        this.grassPerDay = grassPerDay;
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
                countFromNotPreffered += Math.min(prefferedSpot.size()-countFromNotPreffered,numberOfGrasses-placedGrasses);
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
        List<Vector2d> indicesChoosen  = choosen.subList(0,numberToGrow);
        for(Vector2d spot: indicesChoosen){
            addNewGrass(spot);
        }
    }
    private void addNewGrass(Vector2d spot){
        this.grasses.put(spot,new Grass(spot));
        for (Vector2d direction : MOVEMENT_VECTORS) {
            if(spot.add(direction).follows(lowerLeft) && spot.add(direction).proceeds(upperRight)){
                prefferedSpot.add(spot.add(direction));

            }
        }
        prefferedSpot.remove(spot);
    }
    public void removeGrass(Vector2d spot){
        grasses.remove(spot);
        if(spot.getY() <= (int) (0.6 * height) && spot.getY() >= (int) (0.4 * height)){
            prefferedSpot.add(spot);
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

    @Override
    public WorldElement objectAt(Vector2d position) {
        WorldElement objectAtPosition = super.objectAt(position);
        if (objectAtPosition == null) {
            return grasses.get(position);
        }
        return objectAtPosition;
    }


    public int getGrassPerDay() {
        return this.grassPerDay;
    }

    public void eatGrass(Animal animal) {
        animal.setEnergy(animal.getEnergy() + energyFromGrass);
        removeGrass(animal.getPosition());
    }

    @Override
    public List<WorldElement> getElements() {
        List<WorldElement> elements = super.getElements();
        elements.addAll(grasses.values());
        return elements;
    }

    public boolean isGrass(Vector2d position) {
        return grasses.get(position) != null;
    }


    @Override
    public Boundary getCurrentBounds() {
        List<WorldElement> elements = getElements();
        Vector2d lowerLeftCorner = new Vector2d(0, 0);
        Vector2d upperRightCorner = new Vector2d(this.width - 1, this.height - 1);
        for (WorldElement element : elements) {
            lowerLeftCorner = lowerLeftCorner.lowerLeft(element.getPosition());
            upperRightCorner = upperRightCorner.upperRight(element.getPosition());
        }
        return new Boundary(lowerLeftCorner, upperRightCorner);
    }
}
