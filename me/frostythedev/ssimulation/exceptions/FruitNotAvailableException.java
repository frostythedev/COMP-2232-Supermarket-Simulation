package me.frostythedev.ssimulation.exceptions;

/*
 * PROGRAMMERS: Tevin Cole, Jalisa Thompson, Simone Doughlin-Welsh
 *
 * This class defines an exception that is thrown when a fruit is not available for purchase due to
 * unavailability by the vendor, it takes the name of the fruit and prints an errorMessage
 * */
public class FruitNotAvailableException extends SupermarketException {

    public FruitNotAvailableException(String fruit) {
        super("Fruit '%s' is not available".formatted(fruit));
    }
}
