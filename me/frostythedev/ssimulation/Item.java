package me.frostythedev.ssimulation;

public abstract class Item {

    private String name;
    private double cost;
    private int quantity;
    private int spoilt;

    public Item(String name, double cost, int quantity, int spoilt) {
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.spoilt = spoilt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
