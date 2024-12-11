package info.kgeorgiy.ja.korobov.bank;

import info.kgeorgiy.ja.korobov.bank.Bank;
import info.kgeorgiy.ja.korobov.bank.account.Account;
import info.kgeorgiy.ja.korobov.bank.account.RemoteAccount;
import info.kgeorgiy.ja.korobov.bank.person.LocalPerson;
import info.kgeorgiy.ja.korobov.bank.person.Person;
import info.kgeorgiy.ja.korobov.bank.person.PersonType;
import info.kgeorgiy.ja.korobov.bank.person.RemotePerson;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RemoteBank implements Bank {
    private final int port;
    private final ConcurrentMap<String, Account> accounts = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Person> persons = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, ConcurrentMap<String, Account>> personAccounts = new ConcurrentHashMap<>();

    public RemoteBank(final int port) {
        this.port = port;
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public Account createAccount(final String id) throws RemoteException {
        System.out.println("Creating account " + id);
        final Account account = new RemoteAccount(id);
        if (accounts.putIfAbsent(id, account) == null) {
            UnicastRemoteObject.exportObject(account, port);
            return account;
        } else {
            return getAccount(id);
        }
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public Account getAccount(final String id) {
        System.out.println("Retrieving account " + id);
        return accounts.get(id);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public Person createPerson(String firstName, String lastName, String passport) throws RemoteException {
        System.out.println("Creating person " + firstName + " " + lastName + ", that has passport: " + passport);
        if (!personAccounts.containsKey(passport)) {
            personAccounts.put(passport, new ConcurrentHashMap<>());
        }
        final Person person = new RemotePerson(firstName, lastName, passport, personAccounts.get(passport));
        if (persons.putIfAbsent(passport, person) == null) {
            UnicastRemoteObject.exportObject(person, port);
            return person;
        } else {
            return getPerson(passport, PersonType.REMOTE);
        }
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public Account createPersonAccount(final String passport, final String subId) throws RemoteException {
        final String id = passport + ":" + subId;
        final Person person = persons.get(passport);
        System.out.println("For person " + person.getFirstName() + " " + person.getLastName());
        final Account account = createAccount(id);
        personAccounts.get(passport).put(id, account);
        return account;
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public Person getPerson(final String passport, PersonType type) throws RemoteException {
        if (type == PersonType.LOCAL) {
            return new LocalPerson(persons.get(passport), personAccounts.get(passport));
        }
        return persons.get(passport);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public ConcurrentMap<String, Person> getAllPersons() {
        return persons;
    }
}
