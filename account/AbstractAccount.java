package info.kgeorgiy.ja.korobov.bank.account;

public class AbstractAccount implements Account {
    private final String id;
    private int amount = 0;

    public AbstractAccount(final String id) {
        this.id = id;
    }

    public AbstractAccount(final String id, final int amount) {
        this(id);
        this.amount = amount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized int getAmount() {
        System.out.println("Amount of account " + id + " is " + amount);
        return amount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void setAmount(final int amount) {
        this.amount = amount;
        getAmount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void addAmount(final int amount) {
        this.amount += amount;
        getAmount();
    }
}
