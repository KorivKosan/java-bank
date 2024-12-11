package info.kgeorgiy.ja.korobov.bank.tests.expected;

/**
 * {@link Class} for check Account
 * */
public class AccountExpected {
    String id;
    int amount;

    public AccountExpected(String id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    /**
     * Getting id of {@link AccountExpected}
     * */
    public synchronized String getId() {
        return id;
    }

    /**
     * Getting amount of {@link AccountExpected}
     * */
    public synchronized int getAmount() {
        return amount;
    }

    /**
     * Set amount of {@link AccountExpected}
     * */
    public synchronized void setAmount(int amount) {
        this.amount = amount;
    }
}
