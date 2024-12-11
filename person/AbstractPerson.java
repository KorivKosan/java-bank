package info.kgeorgiy.ja.korobov.bank.person;

import info.kgeorgiy.ja.korobov.bank.account.Account;

import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractPerson implements Person {
    private final String firstName;
    private final String lastName;
    private final String passport;
    ConcurrentMap<String, Account> accounts = new ConcurrentHashMap<>();


    public AbstractPerson(String firstName, String lastName, String passport) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passport = passport;
    }

    public AbstractPerson(String firstName, String lastName, String passport, ConcurrentMap<String, Account> accounts) throws RemoteException {
        this(firstName, lastName, passport);
        this.accounts = accounts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String getFirstName() {
        return firstName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String getLastName() {
        return lastName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String getPassport() {
        return passport;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized ConcurrentMap<String, Account> getAccounts() {
        return accounts;
    }

}
