package bank;


import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Bank {

    /*
        Strings de connection à la base postgres
     */
    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://localhost:5439/postgres";
    private static final String DB_USER = "postgres";

    /*
        Strings de connection à la base mysql, à décommenter et compléter avec votre nom de bdd et de user
     */
    // private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    // private static final String DB_URL = "jdbc:mysql://localhost:3306/bank_db";
    // private static final String DB_USER = "bank_user";

    private static final String DB_PASS = "1234";

    private static final String TABLE_NAME = "accounts";

    private Connection c;


    /*
        attribut bank
    */
    public HashMap<Integer,Account> lisAccounts = new HashMap<Integer, Account>();                      //all accounts are store in the array
    private Integer AccountIndex = 0;                                                   // increment for each account created
    public HashMap<Integer,Account>  getBankAccountList() {
        return this.lisAccounts;
    }
    public Integer getAccountIndex() {
        return this.AccountIndex;
    }
    public void incrgAccountIndex() {
        this.AccountIndex++;
    }

    public Bank() {
        initDb();
    }

    private void initDb() {
        try {
            Class.forName(JDBC_DRIVER);
            c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("Opened database successfully");
            try (Statement s = c.createStatement()) {                       // creat the tab
                String str = "CREATE TABLE " + TABLE_NAME +
                        " (userName VARCHAR(255) PRIMARY KEY, " +
                        "solde INTEGER, " +
                        "threshold INTEGER, " +
                        "blocked BOOLEAN);";
                s.executeUpdate(str);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void closeDb() {
        try {
            c.close();
        } catch (SQLException e) {
            System.out.println("Could not close the database : " + e);
        }
    }

    void dropAllTables() {
        try (Statement s = c.createStatement()) {
            s.executeUpdate(
                       "DROP SCHEMA public CASCADE;" +
                            "CREATE SCHEMA public;" +
                            "GRANT ALL ON SCHEMA public TO postgres;" +
                            "GRANT ALL ON SCHEMA public TO public;");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    public void sqlCallInserUpdaDele(String sqlRequest) { //function do a sql query for -> INSERT, UPDATE, DELETE
        try (Statement s = c.createStatement()) {
            s.executeUpdate(sqlRequest);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void createNewAccount(String name, int balance, int threshold) {
        Boolean valid = true;

        if( threshold > 0 || threshold > balance )  // check if the threshold is not bigger than balance
            valid = false;                          // and if threshold is over 0

        if(this.accountsAllready(name))             // check if the account does not aleady existe
            valid = false;

        if(name == null || name.equals(""))         // check if name is not null or empty
            valid = false;

        if(valid) {
            Account nwAccount = new Account(name, balance, threshold);                                  // build new instace
            this.lisAccounts.put(this.getAccountIndex(),nwAccount);                                     // insert the new account in the array
            String sqlRequest = "INSERT INTO accounts(userName, solde, threshold, blocked) VALUES ('" +
                    this.lisAccounts.get(this.getAccountIndex()).getName() + "', " +
                    this.lisAccounts.get(this.getAccountIndex()).getSolde().toString() + ", " +         // builde sql request
                    this.lisAccounts.get(this.getAccountIndex()).getThreshold().toString() + ", " +
                    this.lisAccounts.get(this.getAccountIndex()).getBlocked().toString() + ");";
            this.sqlCallInserUpdaDele(sqlRequest);                                                      //exect the request
            this.incrgAccountIndex();                                                                   // increment account index
        } else {
            if( threshold > 0 )                                                                         // error messages
                System.out.println("\nerror: La limite de découvert ne peut pas être supérieur à 0\n");

            if(threshold > balance)
                System.out.println("\nerror: Le solde ne peut être inférieur à la limite de découvert\n");

            if(this.accountsAllready(name))
                System.out.println("\nerror: ce compte existe déjà trouver un autre Nom\n");

            if(name == null || name.equals("")) {
                System.out.println("\nerror: vous devez rentrer un nom de compte\n");
            }
        }
    }

    public String printAllAccounts() {
        String allAccountString = "";

        if(this.getAccountIndex() == 0)                 // if the index is not bigger than 0
            return allAccountString;                    // it means there is not count created

        for(int i = 0; i < this.getAccountIndex(); i++) {
            allAccountString = allAccountString + this.lisAccounts.get(i).toString() + "\n"; // concate the string
        }                                                                                    // contains all informations
        return allAccountString;
    }

    public boolean accountsAllready(String nwAccount) {
        if(this.getAccountIndex() == 0)                 // if the index is not bigger than 0
            return false;                               // it means there is not count created

        for(int i = 0; i < this.getAccountIndex(); i++) {
            if(nwAccount.equals(this.lisAccounts.get(i).getName())) //  if there is the same name in the array
                return true;                                        //  method return true to not have two accounts
        }                                                           //  with the same name

        return false;
    }

    public void changeBalanceByName(String name, int balanceModifier) {
        for(int i = 0; i < this.getAccountIndex(); i++) {

            if(this.lisAccounts.get(i).getName().compareTo(name) == 0) {          // loop to find the account
                    if(this.lisAccounts.get(i).getBlocked()) {                     // block the operation if the account is blocked
                    System.out.println("\nerror: Le compte est bloqué\n");
                } else {
                        if((this.lisAccounts.get(i).getSolde() + balanceModifier) < this.lisAccounts.get(i).getThreshold()) { // block the operation if
                            System.out.println("\nerror: la somme retirer dépasse le découvert\n");                           // it get sold lower than the Threshold
                        } else {
                            this.lisAccounts.get(i).addToSolde( balanceModifier);                       // update the object
                            this.sqlCallInserUpdaDele(                                                  // and the database's data
                                    "UPDATE " +
                                            TABLE_NAME +
                                            " SET solde = " +
                                            Integer.toString(this.lisAccounts.get(i).getSolde()) +
                                            " WHERE userName = '" +
                                            name + "'"
                            );
                        }
                }
            } else {
                System.out.println("\nerror: nom de compte non trouvé\n");
            }
        }
    }

    public void blockAccount(String name) {
        for(int i = 0; i < this.getAccountIndex(); i++) {                   // loop to find the account
            if( this.lisAccounts.get(i).getName().compareTo(name) == 0) {
                this.lisAccounts.get(i).setBlocked(true);                   // update blocked status
                this.sqlCallInserUpdaDele(                                  // update db
                        "UPDATE "+ TABLE_NAME + " SET blocked = " +
                                this.lisAccounts.get(i).getBlocked().toString() +
                                " WHERE userName = '" +
                                name +"'");
            } else {
                System.out.println("error: nom de compte non trouvé");
            }
        }
    }

    // For testing purpose
    String getTableDump() {
        String query = "select userName, solde, threshold, blocked from " + TABLE_NAME;
        String res = "";

        try (PreparedStatement s = c.prepareStatement(query)) {
            ResultSet r = s.executeQuery();

            // Getting nb colmun from meta data
            int nbColumns = r.getMetaData().getColumnCount();

            // while there is a next row
            while (r.next()){
                String[] currentRow = new String[nbColumns];

                // For each column in the row
                for (int i = 1 ; i <= nbColumns ; i++) {
                    currentRow[i - 1] = r.getString(i);
                }
                res += Arrays.toString(currentRow);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return res;
    }
}
