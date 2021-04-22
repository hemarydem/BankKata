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
        System.out.printf(this.name);
        System.out.printf(this.solde);
        System.out.printf(this.threshold);
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
        String result = this.name + "|" + this.solde.toString() + "|" + this.threshold.toString();
        System.out.printf(result);
        return result;
    }
}
