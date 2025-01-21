package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Genomes {
    private final List<Integer> genes;
    private static final Random random = new Random();
    private final int genesLength;


    public Genomes(int genesLength) {
        this.genes = new ArrayList<>();
        this.genesLength = genesLength;
        for(int i =0; i<genesLength; i++){
            int randomGen = random.nextInt(8);
            this.genes.add(randomGen);
        }
    }



    public Genomes(Animal parent1, Animal parent2){
        this.genes = new ArrayList<>();
        this.genesLength = parent1.getGenomes().getGenesLength();
        if(!parent1.isFuller(parent2)){
            Animal swap = parent1;
            parent1 = parent2;
            parent2 = swap;
        }
        double dominantPercentege = (double) parent1.getEnergy() / (parent1.getEnergy() + parent2.getEnergy());
        boolean side = random.nextBoolean(); //strona z której bierzemy dominujący
        if(side){
            int intersection = (int) Math.round(dominantPercentege* genesLength);
            for(int i = 0;i<intersection;i++){
                genes.add(parent1.getGenomes().get(i));
            }
            for(int i = intersection;i<genesLength;i++){
                genes.add(parent2.getGenomes().get(i));
            }
        }
        else{
            int intersection = genesLength - (int) Math.round(dominantPercentege* genesLength);
            for(int i = 0;i<intersection;i++){
                genes.add(parent2.getGenomes().get(i));
            }
            for(int i = intersection;i<genesLength;i++){
                genes.add(parent1.getGenomes().get(i));
            }
        }
        int toChange = random.nextInt(genesLength);
        List<Integer> indeksy = new ArrayList<>();
        for (int i = 0; i <= genesLength; i++) {
            indeksy.add(i);
        }
        Collections.shuffle(indeksy);
        List<Integer> toMutate  = indeksy.subList(0,toChange);
        for(int index: toMutate){
            int randomGen = random.nextInt(8);
            this.genes.add(index,randomGen);
        }
    }
    public Integer get(int index){
        return this.genes.get(index);
    }
    public int getGenesLength(){
        return this.genesLength;
    }
    @Override
    public String toString() {
        return genes.toString();
    }
}
