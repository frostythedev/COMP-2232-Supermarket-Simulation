package me.frostythedev.ssimulation.exceptions;

public class FruitNotAvailableException extends SupermarketException {

    public FruitNotAvailableException(String name) {
        super("Fruit '%s' is not available".formatted(name));
    }
}
