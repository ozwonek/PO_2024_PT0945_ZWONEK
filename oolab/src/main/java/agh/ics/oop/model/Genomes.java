package agh.ics.oop.model;

import java.util.ArrayList;
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
