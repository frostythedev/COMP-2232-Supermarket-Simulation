package me.frostythedev.ssimulation.exceptions;

public class VegetableNotAvailableException extends SupermarketException {

    public VegetableNotAvailableException(String vegName) {
        super("Vegetable '%s' is not available".formatted(vegName));
    }
}
