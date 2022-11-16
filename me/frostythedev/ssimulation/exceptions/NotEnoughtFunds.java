package me.frostythedev.ssimulation.exceptions;

/*
 * PROGRAMMERS: Tevin Cole, Jalisa Thompson, Simeone Douglin-Welch
 *
 * This class defines an exception that is thrown the supermarket runs out of funds to purchase from a vendor
 * */
public class NotEnoughtFunds extends SupermarketException {

    public NotEnoughtFunds() {
        super("The supermarket does not have enough funds to resupply from a vendor.");
    }
}
