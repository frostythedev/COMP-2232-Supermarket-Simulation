package me.frostythedev.ssimulation.exceptions;

/*
 * PROGRAMMERS: Tevin Cole, Jalisa Thompson, Simone Doughlin-Welsh
 *
 * This class defines an exception that is thrown when a vegetable is not available for purchase due to
 * unavailability by the vendor, it takes the name of the vegetable and prints an errorMessage
 * */
public class VegetableNotAvailableException extends SupermarketException {

    public VegetableNotAvailableException(String vegName) {
        super("Vegetable '%s' is not available".formatted(vegName));
    }
}
