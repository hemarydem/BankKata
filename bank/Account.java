package bank;

class Account {

    // Attributes
    public String name;
    public Float solde;
    public Float threshold;
    public Boolean blocked;

    // Constructor
    Account (String name, float soldeMoney, float threshold) {
        this.name;
        this.solde;
        this.threshold;
        this.blocked;

    }

    // Methods
    public String getName() {
        return this.name;
    }

    public Float getSolde() {
        return this.solde;
    }

    public Float addToSolde(Float money) {
        this.solde += money;
    }

    public Float withdrawal(Float money) {
        this.solde =  this.solde - money;
    }

    public String toString() {
        // TODO
        return "";
    }
}
