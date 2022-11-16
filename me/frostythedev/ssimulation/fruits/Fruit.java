package me.frostythedev.ssimulation.fruits;

import me.frostythedev.ssimulation.Item;

public class Fruit extends Item {

    public Fruit(String name, double cost, int quantity, int spoilt, Item.Type itemType) {
        super(name, cost, quantity, spoilt, itemType);
    }
}
