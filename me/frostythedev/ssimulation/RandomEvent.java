package me.frostythedev.ssimulation;

public abstract class RandomEvent {

    private String name;

    public abstract void run();

    enum Type {
        CUSTOMER_PURCHASE,
        FRIDGE_DOWN,
        ELECTRICITY_OFF,

    }
}
