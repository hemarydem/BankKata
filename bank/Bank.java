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
    public HashMap<Integer,Account> lisAccounts = new HashMap<Integer, Account>();
    private Integer AccountIndex = 0;

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
            try (Statement s = c.createStatement()) {
                String str = "CREATE TABLE " + TABLE_NAME +
                        " (userId SERIAL PRIMARY KEY,"
                +" userName VARCHAR(255), " +
                        "solde FLOAT, " +
                        "threshold FLOAT, " +
                        "blocked BOOLEAN);";
                //System.out.println(str);
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
    public void sqlCallInserUpdaDele(String sqlRequest) { // for -> INSERT, UPDATE, DELETE
        try (Statement s = c.createStatement()) {
            s.executeUpdate(sqlRequest);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void createNewAccount(String name, int balance, int threshold) {
        Boolean valid = true;
        if( threshold > 0 || threshold > balance )
            valid = false;
        if(valid) {
            Account nwAccount = new Account(name, (float) balance, (float) threshold);
            this.lisAccounts.put(this.getAccountIndex(),nwAccount);
            String sqlRequest = "INSERT INTO accounts(userName, solde, threshold, blocked) VALUES ('" +
                    this.lisAccounts.get(this.getAccountIndex()).getName() + "', " +
                    this.lisAccounts.get(this.getAccountIndex()).getSolde().toString() + ", " +
                    this.lisAccounts.get(this.getAccountIndex()).getThreshold().toString() + ", " +
                    this.lisAccounts.get(this.getAccountIndex()).getBlocked().toString() + ");";
            this.sqlCallInserUpdaDele(sqlRequest);
            this.incrgAccountIndex();
        } else {
            if( threshold > 0 )
                System.out.println("\nerror: La limite de découvert ne peut pas être supérieur à 0\n");
            if(threshold > balance)
                System.out.println("\nerror: Le solde ne peut être inférieur à la limite de découvert\n");
        }
    }

    public String printAllAccounts() {
        String allAccountString = "";
        if(this.getAccountIndex() == 0)
            return allAccountString;
        for(int i = 0; i < this.getAccountIndex(); i++) {
            allAccountString = allAccountString + this.lisAccounts.get(i).toString() + "\n";
        }
        return allAccountString;
    }

    public void changeBalanceByName(String name, int balanceModifier) {
        for(int i = 0; i < this.getAccountIndex(); i++) {
            if(this.lisAccounts.get(i).getName().compareTo(name) == 0) {
                    if(this.lisAccounts.get(i).getBlocked()) {
                    System.out.println("\nerror: Le compte est bloqué\n");
                } else {
                        if((this.lisAccounts.get(i).getSoldeInt() + balanceModifier) < this.lisAccounts.get(i).getThresholdInt()) {
                            System.out.println("\nerror: la somme retirer dépasse le découvert\n");
                        } else {
                            this.lisAccounts.get(i).addToSolde((float) balanceModifier);
                            this.sqlCallInserUpdaDele(
                                    "UPDATE " +
                                            TABLE_NAME +
                                            " SET solde = " +
                                            Integer.toString(this.lisAccounts.get(i).getSoldeInt()) +
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
        for(int i = 0; i < this.getAccountIndex(); i++) {
            if( this.lisAccounts.get(i).getName().compareTo(name) == 0) {
                this.lisAccounts.get(i).setBlocked(true);
                this.sqlCallInserUpdaDele(
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
