package me.frostythedev.ssimulation.fruits;

import me.frostythedev.ssimulation.Item;

public class Fruit extends Item {

    private FruitType fruitType;

    public Fruit(String name, double cost, int quantity, int spoilt, FruitType fruitType) {
        super(name, cost, quantity, spoilt);
        this.fruitType = fruitType;
    }

    public FruitType getFruitType() {
        return fruitType;
    }

    public void setFruitType(FruitType fruitType) {
        this.fruitType = fruitType;
    }

    public enum FruitType {
        MANGO,
        AVOCADO,
        LIME,
        BANANA,
        WATERMELONS,
    }
}
