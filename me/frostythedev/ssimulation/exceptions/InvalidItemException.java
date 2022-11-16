package me.frostythedev.ssimulation.exceptions;

public class InvalidItemException extends SupermarketException {

    public InvalidItemException(String itemName) {
        super("Could not find an item matching the name: '%s'".formatted(itemName));
    }
}
