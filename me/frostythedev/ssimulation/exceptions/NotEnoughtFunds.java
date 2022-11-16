package me.frostythedev.ssimulation.exceptions;

public class NotEnoughtFunds extends SupermarketException {

    public NotEnoughtFunds() {
        super("The supermarket does not have enough funds to resupply from this vendor.");
    }
}
