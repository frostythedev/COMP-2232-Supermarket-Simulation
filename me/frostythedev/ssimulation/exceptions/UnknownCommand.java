package me.frostythedev.ssimulation.exceptions;

/*
 * PROGRAMMERS: Tevin Cole, Jalisa Thompson, Simone Doughlin-Welsh
 *
 * This class defines an UnknownCommand exception which is thrown when an unrecognized command is entered at the
 * commandline
 * */
public class UnknownCommand extends SupermarketException{

    public UnknownCommand(String command) {
        super("Invalid command '%s' supplied at commandline.".formatted(command));
    }
}
