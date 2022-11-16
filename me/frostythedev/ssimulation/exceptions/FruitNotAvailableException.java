package me.frostythedev.ssimulation.exceptions;

public class FruitNotAvailableException extends SupermarketException {

    public FruitNotAvailableException(String fruit) {
        super("Fruit '%s' is not available".formatted(fruit));
    }
}
