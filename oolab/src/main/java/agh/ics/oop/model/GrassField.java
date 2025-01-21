package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

import java.util.*;

public class GrassField extends AbstractWorldMap{
    private Random rand = new Random();
    private Map<Vector2d, Grass> grasses = new HashMap<>();
    private final int energyFromGrass;


    public GrassField(int numberOfGrasses, int energyFromGrass,int width, int height){
        super(width, height);
        this.energyFromGrass = energyFromGrass;
        int placedGrasses = 0;

        while(placedGrasses< numberOfGrasses){
                Vector2d potentialGrassPosition = new Vector2d(rand.nextInt(width ),rand.nextInt(height));
                if(grasses.get(potentialGrassPosition)==null){
                    grasses.put(potentialGrassPosition,new Grass(potentialGrassPosition));
                    placedGrasses +=1;

                }
        }
    }

    @Override
    public WorldElement objectAt(Vector2d position){
        WorldElement objectAtPosition = super.objectAt(position);
        if (objectAtPosition == null){
            return grasses.get(position);
        }
        return objectAtPosition;
    }

    public int eatingGrass(Vector2d position){
        if (grasses.get(position) != null){
            grasses.remove(position);
            return energyFromGrass;
        }
        else{
            return 0;
        }
    }

    @Override
    public List<WorldElement> getElements(){
        List<WorldElement> elements = super.getElements();
        elements.addAll(grasses.values());
        return elements;
    }

    public boolean isGrass(Vector2d position){
        return grasses.get(position)!=null;
    }

    @Override
    public Boundary getCurrentBounds() {
        List<WorldElement> elements = getElements();
        Vector2d lowerLeftCorner = new Vector2d(0,0);
        Vector2d  upperRightCorner=new Vector2d(this.width-1,this.height-1);
        for(WorldElement element: elements) {
            lowerLeftCorner = lowerLeftCorner.lowerLeft(element.getPosition());
            upperRightCorner = upperRightCorner.upperRight(element.getPosition());
        }
        return new Boundary(lowerLeftCorner,upperRightCorner);
    }

}
