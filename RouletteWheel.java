//A JAVA PROGRAM

//Authors: Brendan Loyd bmlnh9@umsystems.edu Zachary Fulliam zjf66b@umsystem.edu
//HomeWork 2 4500
//09-17-2021
//External files: None


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//This JAVA program simulates a roulette wheel at a casino asking the user a series of questions including betting strategy before starting simulation.

public class Main {
    //This method validates inputs made my the user to insure correct input, otherwise it will produce an error and re prompt
    public static int validateInputs(int min, int max, Scanner scanner) {
        boolean tryFlag = true;
        int input = -1;
        while(tryFlag) {
            try {
                input = scanner.nextInt();
                while (input < min || input > max) {
                    System.out.print("That is an invalid input. Please enter a number between " + min + " and " + max +": ");
                    input = scanner.nextInt();
                }
                tryFlag = false;
            } catch (Exception e) {
                System.err.print("Wrong input! Please enter an Integer: ");
                scanner.nextLine();
            }
        }
        return input;
    }
    //This method handles the choice input by the user for the betting strategy and calls the correct method using a switch statement.
    public static void choiceHandler(final int slots, final int rouletteZero, final int timesVisited, final int dollars, final int choice) {
        switch(choice) {
            case 1:
                martingaleStrategy(slots, rouletteZero, timesVisited, dollars, choice);
                break;
            case 2:
                randomStrategy(slots, rouletteZero, timesVisited, dollars, choice);
                break;
            case 3:
                fixedBetStrategy(slots, rouletteZero, timesVisited, dollars, choice);
                break;
        }
    }

    //Simply creates a random number.
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    //Prompts enter key after displaying results.
    //https://stackoverflow.com/questions/26184409/java-console-prompt-for-enter-input-before-moving-on/26184535
    public static void promptEnterKey(){
        System.out.println("Press \"ENTER\" to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    // Displays results to the user.
    public static void displayResults(final int slots, final int rouletteZero, final int timesVisited, final int dollars, int totalBet, int winnings, final int choice, int biggestWin, int zeroDollars, int leftWithMoreDollars, int leftWithLessDollars, int leftWithSameDollars, List<Integer> changeInMoney) {
        Double averageChangeInMoney = changeInMoney.stream().mapToDouble(val -> val).average().orElse(0.0); // https://stackoverflow.com/questions/10791568/calculating-average-of-an-array-list

        System.out.println("You started with: " + slots + " slots.");
        System.out.println("You entered: " + rouletteZero + " slots that start with 0/00.");
        System.out.println("You visited: " + timesVisited + " times.");
        System.out.println("You started with: $" + dollars + " each time you visited the casino.");
        System.out.println("Number of dollars put at risk: $" + (dollars*timesVisited));
        System.out.println("Chosen strategy was: " + choice);
        System.out.println("The total dollars bet was: $" + totalBet);
        System.out.println("The total dollars walked away with is: $" + ((timesVisited * dollars) + winnings) + " at a % gain of: " + ((winnings / (double)(dollars*timesVisited)) * 100) + "%.");
        System.out.println("Your biggest win was: " + biggestWin);
        System.out.println("Your average change in dollars over your visits was: $" + averageChangeInMoney);
        System.out.println("How many times you ended up with zero dollars: " + zeroDollars);
        System.out.println("How many times you left with more dollars: " + leftWithMoreDollars);
        System.out.println("How many times you left with less dollars: " + leftWithLessDollars);
        System.out.println("How many times you left with the same dollars: " + leftWithSameDollars);

        promptEnterKey();
    }

    //This method comprises the enter workings of the martingale strategy. calling the displayResults method at the end.
    public static void martingaleStrategy(final int slots, final int rouletteZero, final int timesVisited, final int dollars, final int choice) {
        int totalWinnings = 0, totalBet = 0, biggestWin = 0, endedUpWithZeroDollars = 0, leftWithMoreDollars = 0, leftWithLessDollars = 0, leftWithSameDollars = 0;
        List<Integer> changeInMoney = new ArrayList<>();

        List<Integer> zeros = IntStream.range(0, rouletteZero).boxed().collect(Collectors.toList()); // https://stackoverflow.com/questions/16711147/populating-a-list-with-a-contiguous-range-of-integers/23675131
        for(int i = 0; i < timesVisited; i++) {
            int currentDollars = dollars;
            int bet = 1, totalBetPerVisit = 0;
            boolean winFlag = true;

            while (winFlag) {
                totalBetPerVisit += bet;
                int winningNumber = getRandomNumber(0, slots);
                if (!zeros.contains(winningNumber) && winningNumber % 2 == 1) {
                    totalWinnings += (bet*2) - totalBetPerVisit;
                    currentDollars += bet * 2;
                    biggestWin = Math.max(bet * 2, biggestWin);
                    winFlag = false;
                } else {
                    currentDollars -= bet;
                    bet *= 2;
                    if (currentDollars < bet) {
                        winFlag = false;
                    }
                    else if (currentDollars == 0) {
                        endedUpWithZeroDollars++;
                    }
                }
            }

            changeInMoney.add(currentDollars - dollars);
            totalBet += totalBetPerVisit;

            if(currentDollars > dollars) {
                leftWithMoreDollars++;
            } else if (currentDollars < dollars) {
                leftWithLessDollars++;
            } else {
                leftWithSameDollars++;
            }
        }

        displayResults(slots, rouletteZero, timesVisited, dollars, totalBet, totalWinnings, choice, biggestWin, endedUpWithZeroDollars, leftWithMoreDollars, leftWithLessDollars, leftWithSameDollars, changeInMoney);
    }

    //This method comprises the enter workings of the randomStrategy. calling the displayResults method at the end.
    public static void randomStrategy(final int slots, final int rouletteZero, final int timesVisited, final int dollars, final int choice) {
        int totalWinnings = 0, totalBet = 0, biggestWin = 0, endedUpWithZeroDollars = 0, leftWithMoreDollars = 0, leftWithLessDollars = 0, leftWithSameDollars = 0;
        List<Integer> changeInMoney = new ArrayList<>();

        List<Integer> zeros = IntStream.range(0, rouletteZero).boxed().collect(Collectors.toList()); // https://stackoverflow.com/questions/16711147/populating-a-list-with-a-contiguous-range-of-integers/23675131
        for(int i = 0; i < timesVisited; i++) {
            int currentDollars = dollars;
            int bet = getRandomNumber(1, dollars);
            int betCount = 0;

            int totalBetPerVisit = 0;
            boolean winFlag = true;

            while (winFlag) {
                betCount++;
                if (betCount <= 50) {
                    totalBetPerVisit += bet;
                    int winningNumber = getRandomNumber(0, slots);
                    totalBet += totalBetPerVisit;
                    if (!zeros.contains(winningNumber) && winningNumber % 2 == 1) {
                        totalWinnings += bet * 2;
                        currentDollars += bet * 2;
                        biggestWin = Math.max(bet * 2, biggestWin);
                    } else {
                        currentDollars -= bet;
                        bet = getRandomNumber(1, dollars);
                        if (currentDollars < bet) {
                            winFlag = false;
                        }
                        else if (currentDollars == 0) {
                            endedUpWithZeroDollars++;
                        }
                    }
                } else {
                    winFlag = false;
                }
            }

            changeInMoney.add(currentDollars - dollars);

            if(currentDollars > dollars) {
                leftWithMoreDollars++;
            } else if (currentDollars < dollars) {
                leftWithLessDollars++;
            } else {
                leftWithSameDollars++;
            }
        }

        displayResults(slots, rouletteZero, timesVisited, dollars, totalBet, totalWinnings ,choice, biggestWin, endedUpWithZeroDollars, leftWithMoreDollars, leftWithLessDollars, leftWithSameDollars, changeInMoney);
    }

    //This method comprises the enter workings of the fixedBetStrategy. calling the displayResults method at the end.
    public static void fixedBetStrategy(final int slots, final int rouletteZero, final int timesVisited, final int dollars, final int choice) {
        int totalWinnings = 0, totalBet = 0, biggestWin = 0, endedUpWithZeroDollars = 0, leftWithMoreDollars = 0, leftWithLessDollars = 0, leftWithSameDollars = 0;
        List<Integer> changeInMoney = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Your fixed bet is: ");
        int bet = validateInputs(1, dollars, scanner);

        List<Integer> zeros = IntStream.range(0, rouletteZero).boxed().collect(Collectors.toList()); // https://stackoverflow.com/questions/16711147/populating-a-list-with-a-contiguous-range-of-integers/23675131
        for (int i = 0; i < timesVisited; i++) {
            int currentDollars = dollars;
            int betCount = 0, totalBetPerVisit = 0;
            boolean winFlag = true;

            while (winFlag) {
                betCount++;
                if (betCount <= 50) {
                    totalBetPerVisit += bet;
                    totalBet += bet;
                    int winningNumber = getRandomNumber(0, slots);
                    if (!zeros.contains(winningNumber) && winningNumber % 2 == 1) {
                        totalWinnings += bet * 2;
                        currentDollars += bet * 2;
                        biggestWin = Math.max(bet * 2, biggestWin);
                    } else {
                        currentDollars -= bet;
                        if (currentDollars < bet || betCount == 50) {
                            winFlag = false;
                        }
                        else if (currentDollars == 0) {
                            endedUpWithZeroDollars++;
                        }
                    }
                } else {
                    winFlag = false;
                }
            }

            changeInMoney.add(currentDollars - dollars);

            if(currentDollars > dollars) {
                leftWithMoreDollars++;
            } else if (currentDollars < dollars) {
                leftWithLessDollars++;
            } else {
                leftWithSameDollars++;
            }
        }

        displayResults(slots, rouletteZero, timesVisited, dollars, totalBet, totalWinnings, choice, biggestWin, endedUpWithZeroDollars, leftWithMoreDollars, leftWithLessDollars, leftWithSameDollars, changeInMoney);
    }
    //This method is the menu. It handles the gathering the inputs from the user and then calling the choiceHandler to bring the program to the next phase.
    public static void menu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello welcome to the casino, we will be playing a roulette wheel game.");

        System.out.print("How many slots will be in your roulette wheel? Enter a number between 2 and 200: ");
        final int slots = validateInputs(2, 200, scanner);

        System.out.println("How many slots will be labeled with 0/00? Enter an number between 0 and 2:");
        final int rouletteZero = validateInputs(0, 2, scanner);

        System.out.println("How many times would you like to visit the casino? Enter a number between 1 and 100,000:");
        final int timesVisited = validateInputs(1, 100000, scanner);

        System.out.println("How many dollars would you like to start with each time visiting the casino? Enter a number between 1 and 1,000,000:");
        final int dollars = validateInputs(1, 1000000, scanner);

        System.out.println("Choose a betting strategy:\n1)The Martingale Strategy.\n2)The Random Strategy.\n3)The Fixed Bet Strategy.");
        System.out.println("Enter a choice(1, 2, or 3):");
        final int strategyChoice = validateInputs(1, 3, scanner);

        choiceHandler(slots, rouletteZero, timesVisited, dollars, strategyChoice);
    }

    //Simply calls the menu function to start the program.
    public static void main(String[] args) {
        menu();
    }
}