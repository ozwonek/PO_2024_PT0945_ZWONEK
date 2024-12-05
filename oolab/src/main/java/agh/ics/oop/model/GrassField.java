package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

import java.util.*;

public class GrassField extends AbstractWorldMap{
    private Random rand = new Random();
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    public GrassField(int numberOfGrasses){
        int placedGrasses = 0;
        while(placedGrasses< numberOfGrasses){
                Vector2d potentialGrassPosition = new Vector2d(rand.nextInt((int) Math.sqrt(10 * numberOfGrasses)),rand.nextInt((int) Math.sqrt(10 * numberOfGrasses)));
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
    @Override
    public List<WorldElement> getElements(){
        List<WorldElement> elements = super.getElements();
        elements.addAll(grasses.values());
        return elements;
    }
    @Override
    public Boundary getCurrentBounds() {
        List<WorldElement> elements = getElements();
        Vector2d lowerLeftCorner = new Vector2d(Integer.MAX_VALUE,Integer.MAX_VALUE);
        Vector2d  upperRightCorner=new Vector2d(Integer.MIN_VALUE,Integer.MIN_VALUE);
        for(WorldElement element: elements) {
            lowerLeftCorner = lowerLeftCorner.lowerLeft(element.getPosition());
            upperRightCorner = upperRightCorner.upperRight(element.getPosition());
        }
        return new Boundary(lowerLeftCorner,upperRightCorner);
    }
}
