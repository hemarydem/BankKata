import bank.Bank;

import java.util.Scanner;

public class Main extends Application {

    private static Scanner s = new Scanner(System.in);

    // Nettoie l'écran des prints précédents
    private static void flushScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) {

        // Init
        Bank b = new Bank();

        /// Declaration before loop
        boolean endOfSession = false;
        String userInput;

        // Loop
        while (!endOfSession) {

            // Menu display
            System.out.println("\n\nWhat operation do you want to do ?");
            System.out.println("0. See all accounts");
            System.out.println("1. Create a new account");
            System.out.println("2. Change balance on a given account");
            System.out.println("3. Block an account");
            System.out.println("q. Quit\n");

            // Getting primary input
            userInput = s.nextLine();

            String userInputName = "";
            String userInputSolde = "";
            String userInputthreshold = "";

            Integer sold;
            Integer threshold;

            boolean nexStep = false;
            boolean stop = false;

            // Processing user input
            switch (userInput) {
                case "q":
                    endOfSession = true;
                    b.closeDb();
                    break;
                case "0":
                    b.printAllAccounts();
                    break;
                case "1":
                    System.out.println("Rentrer votre nom s'il vous plait\nPour annuler et revenir au menu principal appuyer sur q puis sur entrer\n");
                    userInputName = s.nextLine();
                    userInputName = userInputName.trim();
                    if(userInputName.matches("^[q]$")) {
                        stop = true;
                    }
                    if(stop)
                        break;
                    while (!nexStep) {
                        System.out.println("rentrer votre solde\nPour annuler et revenir au menu principal appuyer sur q puis sur entrer\n");
                        userInputSolde = s.nextLine();
                        userInputSolde.replaceAll("\\s+","");
                        if(userInputSolde.matches("^[q]$")) {
                            stop = true;
                            nexStep = true;
                        } else if(userInputSolde.matches("^\\d+$|^\\d+[.]\\d+$")) {
                            nexStep = true;
                        } else {
                            System.out.println("Vous devez écrire votre chiffre soit:\nNombre -> 1 ou nombre.nombre 1.1\n");
                        }
                    }
                    if(stop)
                        break;
                    nexStep = false;
                    while (!nexStep) {
                        System.out.println("rentrer le seuil de découvert\nPour annuler et revenir au menu principal appuyer sur q puis sur entrer\n");
                        userInputthreshold = s.nextLine();
                        if(userInputthreshold.matches("^[q]$")) {
                            stop = true;
                            nexStep = true;
                        } else if(userInputthreshold.matches("^[-]\\d+$|^[-]\\d+[.]\\d+$|^0+$")) {
                            nexStep = true;
                        } else {
                            System.out.println("Vous devez écrire votre chiffre soit:\nNombre -> 1 ou nombre.nombre 1.1e\n");
                        }
                    }
                    b.createNewAccount(userInputName, Integer.parseInt(userInputSolde), Integer.parseInt(userInputthreshold));
                    break;
                case "2":
                        System.out.println("Rentrer votre nom s'il vous plait\nPour annuler et revenir au menu principal appuyer sur q puis sur entrer\n");
                        userInputName = s.nextLine();
                        userInputName = userInputName.trim();
                        if(userInputName.matches("^[q]$")) {
                            stop = true;
                        }
                    if(stop)
                        break;
                    nexStep = false;
                    while (!nexStep) {
                        System.out.println("rentrer  la sommes à ajouter ou retirer\nPour retirer mettre - (un moins) devant le chifffre\nPour annuler et revenir au menu principal appuyer sur q puis sur entrer\n");
                        userInputSolde = s.nextLine();
                        userInputSolde.replaceAll("\\s+","");
                        if(userInputSolde.matches("^[q]$")) {
                            stop = true;
                            nexStep = true;
                        } else if(userInputSolde.matches("^\\d+$|^\\d+[.]\\d+$|^[-]\\d+$|^[-]\\d+[.]\\d+$")) {
                            nexStep = true;
                        } else {
                            System.out.println("Vous devez écrire votre chiffre soit:\nNombre -> 1 ou nombre.nombre 1.1\nPour retirer mettre - (un moins) devant le chifffre\n");
                        }
                    }
                    if(stop)
                        break;
                    b.changeBalanceByName(userInputName,Integer.parseInt(userInputSolde));
                    break;
                case "3":
                    System.out.println("Rentrer le nom à bloquer s'il vous plait\nPour annuler et revenir au menu principal appuyer sur q puis sur entrer\n");
                    userInputName = s.nextLine();
                    userInputName = userInputName.trim();
                    if(userInputName.matches("^[q]$"))
                        stop = true;
                    if(stop)
                        break;
                    b.blockAccount(userInputName);
                    break;
            }
        }

    }
}

