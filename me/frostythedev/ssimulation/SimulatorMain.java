package me.frostythedev.ssimulation;

import me.frostythedev.ssimulation.utils.Utilities;

import java.util.Scanner;

public class SimulatorMain {

    public static void main(String[] args){
        Utilities.init();

        Supermarket supermarket = new Supermarket();
        supermarket.init();

        Scanner scanner = new Scanner(System.in);

        displayMenu();
        print("Enter your selection:\n");
        String selection = scanner.nextLine();

        selection = selection.substring(0, 1);

        switch (selection.toUpperCase()){
            case "A":
                supermarket.cycle();
                break;
            case "B":
                supermarket.displayLogs();
                break;
            case "C":
                print("Simulation Ended!");
                System.exit(0);
                break;
            default:
                break;
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