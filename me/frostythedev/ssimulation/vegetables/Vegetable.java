package me.frostythedev.ssimulation.vegetables;

import me.frostythedev.ssimulation.Item;

public class Vegetable extends Item {

    private VegeType vegeType;

    public Vegetable(String name, double cost, int quantity, int spoilt, VegeType vegeType) {
        super(name, cost, quantity, spoilt);
        this.vegeType = vegeType;
    }

    public enum VegeType {
        CARROTS,
        LETTUCE,
        CUCUMBERS,
        PARSLEY,
        ONIONS
    }
}
