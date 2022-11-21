package me.frostythedev.ssimulation.exceptions;

/*
 * PROGRAMMERS: Tevin Cole, Jalisa Thompson, Simone Doughlin-Welsh
 *
 * This class defines an exception that is thrown when a item requested cannot be found in a related storage
 * container, it takes the name of the items and displays a corresponding error message
 * */
public class InvalidItemException extends SupermarketException {

    public InvalidItemException(String itemName) {
        super("Could not find an item matching the name: '%s'".formatted(itemName));
    }
}
