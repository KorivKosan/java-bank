package info.kgeorgiy.ja.korobov.bank.account;

public class LocalAccount extends AbstractAccount  {
    public LocalAccount(final String id) {
        super(id);
    }

    public LocalAccount(final String id, final int amount) {
        super(id, amount);
    }
}

