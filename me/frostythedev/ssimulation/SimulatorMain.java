package me.frostythedev.ssimulation;

import me.frostythedev.ssimulation.exceptions.UnknownCommand;
import me.frostythedev.ssimulation.utils.Utilities;

import java.util.Scanner;

public class SimulatorMain {

    public static void main(String[] args){
        // Initializes the randomNumGenerator of the function
        Utilities.init();

        // Defines local scope variables for the commandline options which can be supplied
        int iter = 50;
        boolean verbose = false;
        String logFile = null;

        // Processes the commands

        for(int i = 0; i < args.length; i++){
            // If the command iter is found, read the following argument as the number of CYCLES supplied, if
            // there is an error parsing the integer, print out the error and set the total cycles to the default value
            if(args[i].equals("iter")) {
                if(i < args.length - 1){
                    i++;
                    try{
                        iter = Integer.parseInt(args[i]);
                    }
                    catch(NumberFormatException e) {
                        iter = 50;
                        System.out. println("Invalid iter command total number of iterations is missing");
                    }
                }
                // if the supplied command is verbose, set the verbose option to true
            } else if (args[i].equalsIgnoreCase("verbose")) {
                verbose = true;
                // if the supplied command is log, read the next argument as the file that is to be logged
            } else if (args[i].equalsIgnoreCase("log")) {
                if(args[i+1] != null && !args[i+1].equalsIgnoreCase("")) {
                    logFile = args[i + 1];
                }
                i+=2;
            }else{
                // Unknown command recognised
                try {
                    throw new UnknownCommand(args[i]);
                } catch (UnknownCommand e) {
                    print("EXCEPTION> " + e);
                }
            }
        }


        // Create an instance of the supermarket simulation using the supplied amount of iternations, default=50
        Supermarket supermarket = new Supermarket(iter);

        //initialize the simulation
        supermarket.init();

        // Set the options from the commandline
        supermarket.setDetailedLogs(verbose);
        supermarket.setLogFile(logFile);

        Scanner scanner = new Scanner(System.in);

        while (true){

            // Displays the menu of the simulation and reads in the selection that the user entered before processing
            // the respective action based on the selection, continue displaying the menu and reading in selections
            // until specified by the user or a termination occurs.
            displayMenu();
            print("Enter your selection:\n");
            String selection = scanner.nextLine();

            if(selection != null && !selection.equals("")){
                selection = selection.substring(0, 1);

                switch (selection.toUpperCase()) {
                    case "A" -> {

                        supermarket.init();
                        supermarket.run(); }
                    case "B" -> supermarket.displayLogs(logFile);
                    case "C" -> {
                        print("Simulation Ended!");
                        System.exit(0);
                    }
                    default -> print("Unknown command please enter another!");
                }
            }
        }

    }

    // Prints the menu of the simulation
    private static void displayMenu(){
        print("Supermarket Simulator");
        print("=======================");
        print("A. Start the Simulation\n" +
                "B. Display the Log\n" +
                "C. Exit");

    }

    // General utility function for printing to the command line
    private static void print(String message){
        System.out.printf(message + "\n");
    }
}
