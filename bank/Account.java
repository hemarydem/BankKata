package bank;

import java.sql.Statement;

class Account {
    // Attributes
    private String name;
    private Float solde;
    private Float threshold;
    private Boolean blocked;

    // Constructor
    public Account (String name, float soldeMoney, float threshold) {
        this.name = name;
        this.solde = soldeMoney;
        this.threshold =  threshold;
        this.blocked = false;
    }

    public Account (String name, float soldeMoney, float threshold, boolean blocked) {
        this.name = name;
        this.solde = soldeMoney;
        this.threshold =  threshold;
        this.blocked = blocked;
    }

    // Methods
    public String getName() {
        return this.name;
    }

    public Float getSolde() {
        return this.solde;
    }

    public Float getThreshold() {
        return this.threshold;
    }
    public int getThresholdInt() {
        return Math.round(this.threshold);
    }

    public Boolean getBlocked() {
        return this.blocked;
    }

    public int getSoldeInt() {
        return Math.round(this.solde);
    }

    public Float addToSolde(Float money) {
        return this.solde += money;
    }

    public Float withdrawal(Float money) {
        return this.solde =  this.solde - money;
    }

    public String toString() {
        Integer sld = this.getSoldeInt();
        Integer Thrshld = this.getThresholdInt();
        String result = this.name + " | " + sld.toString() + " | " + Thrshld.toString() + " | " + this.getBlocked().toString() ;
        System.out.printf(result);
        return result;
    }
}
