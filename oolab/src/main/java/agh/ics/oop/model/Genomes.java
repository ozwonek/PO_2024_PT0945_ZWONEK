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
        double dominantPercentege = (double) parent1.getEnergy() / (parent1.getEnergy() + parent2.getEnergy());
//        System.out.println(dominantPercentege + " " +  parent1.getEnergy()+" " + " " + parent2.getEnergy()+ " "+ " "+ (int) Math.round(dominantPercentege* genesLength) +" "+genesLength );
        boolean side = random.nextBoolean(); //strona z której bierzemy dominujący
        if(side){
            int intersection = (int) Math.round(dominantPercentege* genesLength);
            for(int i = 0;i<intersection;i++){
                this.genes.add(parent1.getGenomes().get(i));
            }
            for(int i = intersection;i<genesLength;i++){
                this.genes.add(parent2.getGenomes().get(i));
            }

        }
        else{
            int intersection = genesLength - (int) Math.round(dominantPercentege* this.genesLength) ;
            for(int i = 0;i<intersection;i++){
                genes.add(parent2.getGenomes().get(i));
            }
            for(int i = intersection;i<this.genesLength;i++){
                genes.add(parent1.getGenomes().get(i));
            }

        }
        int toChange = random.nextInt(genesLength);
        List<Integer> indeksy = new ArrayList<>();
        for (int i = 0; i < genesLength; i++) {
            indeksy.add(i);
        }
        Collections.shuffle(indeksy);
        List<Integer> toMutate  = indeksy.subList(0,toChange);
        for(int index: toMutate){
            int randomGen = random.nextInt(8);
            this.genes.set(index,randomGen);
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
