package it.polimi.ingsw.view.client;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Cli {

    private final String validUsername = "^[a-zA-Z0-9]*$";

    private void start(){

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to ADRENALINA");
        System.out.println("Insert username:");

        String username = scanner.nextLine();

        while (!Pattern.matches(validUsername, username)){
            System.out.println("The username can only contain letters and numbers");
            System.out.println("Insert username:");
            username = scanner.nextLine();
        }

        System.out.println("Choose Map:");
        System.out.println("0 -- FIRE");
        System.out.println("1 -- EARTH");
        System.out.println("2 -- WIND");
        System.out.println("3 -- WATER");

        int votedMap = scanner.nextInt();

        while (votedMap < 0 || votedMap > 3){
            System.out.println("Select a number between 0 and 3");
            System.out.println("Choose Map:");
            System.out.println("0 -- FIRE");
            System.out.println("1 -- EARTH");
            System.out.println("2 -- WIND");
            System.out.println("3 -- WATER");

            votedMap = scanner.nextInt();
        }

        System.out.println("Choose the amount of skulls you want to play with: (5 to 8)");

        int nSkulls = scanner.nextInt();

        while (nSkulls < 5 || nSkulls > 8){
            System.out.println("Select a number between 5 and 8");
            System.out.println("Choose the amount of skulls you want to play with: (5 to 8)");

            nSkulls = scanner.nextInt();
        }

        System.out.println("Choose network type:");
        System.out.println("0 -- Socket");
        System.out.println("1 -- RMI");

        int chosenIndex = scanner.nextInt();

        while (chosenIndex != 0 && chosenIndex != 1){
            System.out.println("Choose network type:");
            System.out.println("0 -- Socket");
            System.out.println("1 -- RMI");

            chosenIndex = scanner.nextInt();
        }

        return;
    }

    public static void main(String[] args){

        Cli cli = new Cli();

        cli.start();

    }

}
