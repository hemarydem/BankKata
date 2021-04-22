package bank;

class Account {

    // Attributes
    public String name;
    public Float solde;
    public Float threshold;
    public Boolean blocked;

    // Constructor
    public Account (String name, float soldeMoney, float threshold) {
        this.name = name;
        this.solde = soldeMoney;
        this.threshold =  threshold;
        this.blocked = true;
    }

    // Methods
    public String getName() {
        return this.name;
    }

    public Float getSolde() {
        return this.solde;
    }

    public Float addToSolde(Float money) {
        return this.solde += money;
    }

    public Float withdrawal(Float money) {
        return this.solde =  this.solde - money;
    }

    public String toString() {
        // TODO
        return "";
    }
}
