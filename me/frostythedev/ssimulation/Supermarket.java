package me.frostythedev.ssimulation;

import me.frostythedev.ssimulation.fruits.Fruit;
import me.frostythedev.ssimulation.utils.Utilities;

import java.util.ArrayList;
import java.util.List;

public class Supermarket {

    private int totalCycles, money;
    public final int DEFAULT_TOTAL_CYCLES = 50;

    private List<Vendor> vendors;
    private List<Item> items;

    private List<String> logs;

    public Supermarket() {
        totalCycles = DEFAULT_TOTAL_CYCLES;
    }

    public void init(){
        vendors = new ArrayList<>();

        vendors.add(new Vendor("Fruits Are Here") {
            @Override
            public void init() {

                int randomTotal = Utilities.generateRndNumber(10, 101);

                while (randomTotal != 0){

                    int rndAdd = Utilities.generateRndNumber(randomTotal);

                    addStock(Fruit.FruitType.values()[Utilities.generateRndNumber(Fruit.FruitType.values().length)], rndAdd);
                    randomTotal = randomTotal - rndAdd;
                }
            }

            @Override
            public void restock() {

            }
        });
    }

    private void addStock(Fruit.FruitType value, int rndAdd) {

    }

    public void cycle() {
        for (int i = 0; i < totalCycles; i++) {
            // perform simulation actions.
        }
    }

    public void log(String log){

    }

    public void displayLogs() {
    }
}
