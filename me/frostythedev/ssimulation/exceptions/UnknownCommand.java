package me.frostythedev.ssimulation.exceptions;

public class UnknownCommand extends SupermarketException{

    public UnknownCommand() {
        super("Invalid arguments supplied at commandline.");
    }
}
