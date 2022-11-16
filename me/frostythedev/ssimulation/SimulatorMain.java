package me.frostythedev.ssimulation;

import me.frostythedev.ssimulation.exceptions.UnknownCommand;
import me.frostythedev.ssimulation.utils.Utilities;

import java.util.Scanner;

public class SimulatorMain {

    public static void main(String[] args){
        Utilities.init();

        int iter = 50;
        boolean verbose = false;
        String logFile = null;

        for(int i = 0; i < args.length; i++){
            if(args[i].equals("iter")) {
                if(i < args.length - 1){
                    i++;
                    try{
                        iter = Integer.parseInt(args[i]);
                    }
                    catch(NumberFormatException e) {
                        System.out. println("Invalid iter command total number of iterations is missing");
                    }
                }
            } else if (args[i].equalsIgnoreCase("verbose")) {
                verbose = true;
            } else if (args[i].equalsIgnoreCase("log")) {
                if(args[i+1] != null && !args[i+1].equalsIgnoreCase("")) {
                    logFile = args[i + 1];
                }
            }else{
                try {
                    throw new UnknownCommand(args[i]);
                } catch (UnknownCommand e) {
                    print("EXCEPTION> " + e);
                }
            }
        }


        Supermarket supermarket = new Supermarket(iter);
        supermarket.init();
        supermarket.setDetailedLogs(verbose);
        supermarket.setLogFile(logFile);

        Scanner scanner = new Scanner(System.in);

        while (true){
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

    private static void displayMenu(){
        print("Supermarket Simulator");
        print("=======================");
        print("A. Start the Simulation\n" +
                "B. Display the Log\n" +
                "C. Exit");

    }

    private static void print(String message){
        System.out.printf(message + "\n");
    }
}
