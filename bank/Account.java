package bank;

import java.sql.Statement;

class Account {
    // Attributes
    private String name;
    private Integer solde;
    private Integer threshold;
    private Boolean blocked;

    // Constructor
    public Account (String name, int soldeMoney, int threshold) {
        this.name = name;
        this.solde = soldeMoney;
        this.threshold =  threshold;
        this.blocked = false;
    }

    // Methods
    public String getName() {
        return this.name;
    }                   // geter from here to line 33

    public Integer getSolde() {
        return this.solde;
    }

    public Integer getThreshold() {
        return this.threshold;
    }

    public Boolean getBlocked() {
        return this.blocked;
    }

    public void setBlocked(boolean newStatus) {
        this.blocked = newStatus;
    } // seter from here to line 41

    public Integer addToSolde(Integer money) {
        return this.solde += money;
    }

    public String toString() { // get all object informations in a string
        return this.name + " | " + this.getSolde().toString() + " | " + this.getThreshold().toString() + " | " + this.getBlocked().toString() ;
    }
}
