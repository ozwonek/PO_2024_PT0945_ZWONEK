package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

import java.util.*;

import static agh.ics.oop.model.Vector2d.*;

public class GrassField extends AbstractWorldMap {
    private final int grassPerDay;
    private static final Random rand = new Random();
    private Map<Vector2d, Grass> grasses = new HashMap<>();
    private final int energyFromGrass;
    private double probabilityToMakeJungle;

    public GrassField(int numberOfGrasses, int grassPerDay, double probabilityToMakeJungle, int energyFromGrass, int width, int height) {
        super(width, height);
        this.energyFromGrass = energyFromGrass;
        int placedGrasses = 0;
        this.grassPerDay = grassPerDay;

        while (placedGrasses < numberOfGrasses) {
            Vector2d potentialGrassPosition = new Vector2d(rand.nextInt(width), rand.nextInt(height));
            if (grasses.get(potentialGrassPosition) == null) {
                grasses.put(potentialGrassPosition, new Grass(potentialGrassPosition));
                placedGrasses += 1;

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
    public double getProbabilityToMakeJungle() {
        return probabilityToMakeJungle;
    }

    public int getGrassPerDay() {
        return this.grassPerDay;
    }

    public void eatGrass(Animal animal) {
        animal.setEnergy(animal.getEnergy() + energyFromGrass);
        grasses.remove(animal.getPosition());
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

    public void growGrass(int grassPerDay, double probability) {
        List<Vector2d> probablyElementOfJungle = new ArrayList<>();
        for (Grass currentGrass : grasses.values()) {
            if (!isGrass(currentGrass.getPosition().add(UP)) && currentGrass.getPosition().add(UP).follows(lowerLeft) && currentGrass.getPosition().add(UP).proceeds(upperRight)) {
                probablyElementOfJungle.add(currentGrass.getPosition().add(UP));
            }
            if (!isGrass(currentGrass.getPosition().add(DOWN)) && currentGrass.getPosition().add(DOWN).follows(lowerLeft) && currentGrass.getPosition().add(DOWN).proceeds(upperRight)) {
                probablyElementOfJungle.add(currentGrass.getPosition().add(DOWN));
            }
            if (!isGrass(currentGrass.getPosition().add(LEFT)) && currentGrass.getPosition().add(LEFT).follows(lowerLeft) && currentGrass.getPosition().add(LEFT).proceeds(upperRight)) {
                probablyElementOfJungle.add(currentGrass.getPosition().add(LEFT));
            }
            if (!isGrass(currentGrass.getPosition().add(RIGHT)) && currentGrass.getPosition().add(RIGHT).follows(lowerLeft) && currentGrass.getPosition().add(RIGHT).proceeds(upperRight)) {
                probablyElementOfJungle.add(currentGrass.getPosition().add(RIGHT));
            }
        }
        System.out.println(probablyElementOfJungle);

        int placedGrasses = 0;

        for (int i = 0; i < grassPerDay; i++) {
            int index = rand.nextInt(probablyElementOfJungle.size());
            Grass probablyGrass = new Grass(probablyElementOfJungle.get(index));
            if (rand.nextDouble() < probability) {
                grasses.put(probablyGrass.getPosition(), probablyGrass);
                placedGrasses += 1;
            }

            while (placedGrasses < grassPerDay) {
                Vector2d potentialGrassPosition = new Vector2d(rand.nextInt(width), rand.nextInt(height));
                if (grasses.get(potentialGrassPosition) == null) {
                    grasses.put(potentialGrassPosition, new Grass(potentialGrassPosition));
                    placedGrasses += 1;
                }
            }

        }
    }
}
