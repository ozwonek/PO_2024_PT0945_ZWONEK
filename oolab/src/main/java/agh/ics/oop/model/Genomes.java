package agh.ics.oop.model;

import java.util.*;

public class Genomes { // czemu liczba mnoga?
    private final List<Integer> genes = new ArrayList<>();
    private static final Random random = new Random();
    private final int genesLength; // czy to jest potrzebne?


    public Genomes(int genesLength) {
        this.genesLength = genesLength;
        for (int i = 0; i < genesLength; i++) {
            int randomGen = random.nextInt(8);
            this.genes.add(randomGen);
        }
    }

    public Genomes(Animal parent1, Animal parent2) { // czemu genom powstaje z dwóch zwierząt, a nie dwóch genomów?
        this.genesLength = parent1.getGenomes().getGenesLength();
        Animal dominantParent = parent1.getEnergy() > parent2.getEnergy() ? parent1 : parent2;
        Animal otherParent = parent1.getEnergy() > parent2.getEnergy() ? parent2 : parent1;
        this.inheritGenes(dominantParent, otherParent);
        this.mutateGenes(random.nextInt(genesLength));
    }

    private void inheritGenes(Animal dominantParent, Animal subDaddy) { // subDaddy? que?
        double dominantPercentege = (double) dominantParent.getEnergy() / (subDaddy.getEnergy() + dominantParent.getEnergy());
        boolean side = random.nextBoolean(); //strona z której bierzemy dominujący
        Animal leftParent = side ? dominantParent : subDaddy;
        Animal rightParent = side ? subDaddy : dominantParent;

        int intersection = (int) Math.round(dominantPercentege * genesLength);
        if (!side) intersection = genesLength - (int) Math.round(dominantPercentege * this.genesLength);

        for (int i = 0; i < genesLength; i++) {
            Animal parent = i > intersection ? rightParent : leftParent;
            this.genes.add(parent.getGenomes().get(i));
        }
    }

    private void mutateGenes(int mutationCount) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < genesLength; i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);
        List<Integer> toMutate = indices.subList(0, mutationCount);
        for (int index : toMutate) {
            int randomGen = random.nextInt(8);
            this.genes.set(index, randomGen);
        }
    }

    public Integer get(int index) {
        return this.genes.get(index);
    }

    public int getGenesLength() {
        return this.genesLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genomes genomes = (Genomes) o;
        return Objects.equals(genes, genomes.genes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genes);
    }

    @Override
    public String toString() {
        return genes.toString();
    }
}
