import java.util.Scanner;

public class calculadoraVuelo {
    static final int maxAdditional10Kg = 3;
    static final int maxAdditional23Kg = 2;
    static final double baseXSFare = 175000;
    static final double xsComfortPercentage = 0;
    static final double sComfortPercentage = 0.25;
    static final double mComfortPercentage = 0.30;
    static final double seatSelectionCost = 50000;
    static final double windowOrAisleCost = 15000;
    static final double additional10KgCost = 50000;
    static final double additional23KgCost = 100000;

    static double[] lastTenPrices = new double[10];
    static int lastIndex = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to the flight calculator, please select one of the options: ");
            System.out.println("1: Travel alone.");
            System.out.println("2: Travel with friends.");
            System.out.println("3: Exit.");
            System.out.println("4: Show last 10 prices.");
            System.out.println("5: Show highest price.");

            int typeofTrip = sc.nextInt();

            if (typeofTrip == 3) {
                System.out.println("Thank you for using the flight calculator. Goodbye!");
                break;
            }

            if (typeofTrip == 4) {
                showLastTenPrices();
                continue;
            }

            if (typeofTrip == 5) {
                showHighestPrice();
                continue;
            }

            double[] luggageWeights = new double[1];
            double[] ticketCosts;
            if (typeofTrip == 1) {
                luggageWeights[0] = luggageRequest("YOU", sc);
                ticketCosts = new double[1];
                ticketCosts[0] = fareSuggestion(luggageWeights[0]);
                storePrice(ticketCosts[0]); // Almacenar el precio del boleto
                handleAdditionalServices(sc, ticketCosts, 0);
            } else if (typeofTrip == 2) {
                System.out.println("How many friends are you traveling with?");
                int numOfFriends = sc.nextInt();
                luggageWeights = new double[numOfFriends];
                ticketCosts = new double[numOfFriends];
                for (int i = 0; i < numOfFriends; i++) {
                    System.out.println("Friend " + (i + 1) + ":");
                    luggageWeights[i] = luggageRequest("Friend " + (i + 1), sc);
                    ticketCosts[i] = fareSuggestion(luggageWeights[i]);
                    storePrice(ticketCosts[i]); // Almacenar el precio del boleto
                    handleAdditionalServices(sc, ticketCosts, i);
                }
            }
        }
    }

    public static double luggageRequest(String type, Scanner sc) {
        System.out.println("Great! Approximately how many kilograms of luggage will " + type + " be carrying?");
        return sc.nextDouble();
    }

    public static double fareSuggestion(double luggageKgs) {
        double totalPrice = baseXSFare;
        String fareType = "XS";

        if (luggageKgs > 3) {
            if (luggageKgs > 10 && luggageKgs < 23) {
                totalPrice = baseXSFare + (baseXSFare * sComfortPercentage);
                fareType = "S";
            } else if (luggageKgs >= 23) {
                totalPrice = baseXSFare + (baseXSFare * mComfortPercentage);
                fareType = "M";
            }
        }

        System.out.println("The fare I can suggest you is " + fareType + " with a total cost of " + (int) totalPrice);
        return totalPrice;
    }

    public static void handleAdditionalServices(Scanner sc, double[] ticketCosts, int index) {
        System.out.println("Would you like to add any additional services? (Y/N)");
        String choice = sc.next();

        if (choice.equalsIgnoreCase("Y")) {
            double totalCost = 0;

            System.out.println("Additional services available:");
            System.out.println("1: Seat selection");
            System.out.println("2: Additional 10 kg luggage");
            System.out.println("3: Additional 23 kg luggage");

            while (true) {
                System.out.println("Enter the number corresponding to the service you want to add (or 0 to finish): ");
                int serviceChoice = sc.nextInt();

                if (serviceChoice == 0) {
                    break;
                }

                switch (serviceChoice) {
                    case 1:
                        totalCost += handleSeatSelection(sc, ticketCosts, index);
                        break;
                    case 2:
                        totalCost += handleAdditionalLuggage(10, additional10KgCost, maxAdditional10Kg, sc);
                        break;
                    case 3:
                        totalCost += handleAdditionalLuggage(23, additional23KgCost, maxAdditional23Kg, sc);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

            System.out.println("Total additional service cost: COP " + (int) totalCost);
            ticketCosts[index] += totalCost;
        } else {
            System.out.println("No additional services selected.");
        }
        System.out.println("Total ticket cost for this person: COP " + (int) ticketCosts[index]);
    }

    public static double handleSeatSelection(Scanner sc, double[] ticketCosts, int index) {
        System.out.println("Seat selection:");
        System.out.println("1: Window");
        System.out.println("2: Aisle");
        System.out.println("3: Middle");

        System.out.println("Choose your seat type (1/2/3): ");
        int seatType = sc.nextInt();

        double seatCost = seatSelectionCost;

        if (seatType == 1 || seatType == 2) {
            System.out.println("You selected window or aisle seat.");
            seatCost += windowOrAisleCost;
        } else {
            System.out.println("You selected middle seat.");
        }

        ticketCosts[index] += seatCost;
        return seatCost;
    }

    public static double handleAdditionalLuggage(int kg, double costPerKg, int maxAllowed, Scanner sc) {
        System.out.println("How many additional " + kg + " kg luggage would you like to add?");
        int additionalLuggageCount = sc.nextInt();

        if (additionalLuggageCount > maxAllowed) {
            additionalLuggageCount = maxAllowed;
            System.out.println("You can only add up to " + maxAllowed + " additional " + kg + " kg luggage.");
        }

        double totalCost = additionalLuggageCount * costPerKg;
        System.out.println("Added " + additionalLuggageCount + " additional " + kg + " kg luggage. Total cost: COP " + (int) totalCost);
        return totalCost;
    }

    public static void storePrice(double price) {
        lastTenPrices[lastIndex] = price;
        lastIndex = (lastIndex + 1) % 10; // Avanzar al siguiente índice circularmente
    }

    public static void showLastTenPrices() {
        System.out.println("Last 10 prices:");

        for (double price : lastTenPrices) {
            System.out.println((int) price);
        }
    }

    public static void showHighestPrice() {
        double highestPrice = Double.MIN_VALUE;
        for (double price : lastTenPrices) {
            if (price > highestPrice) {
                highestPrice = price;
            }
        }
        System.out.println("Highest price calculated so far: COP " + (int) highestPrice);
    }
}
