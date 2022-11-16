package me.frostythedev.ssimulation.exceptions;

public class UnknownCommand extends SupermarketException{

    public UnknownCommand(String command) {
        super("Invalid command '%s' supplied at commandline.".formatted(command));
    }
}
