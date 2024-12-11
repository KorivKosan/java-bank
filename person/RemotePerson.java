package info.kgeorgiy.ja.korobov.bank.person;

import info.kgeorgiy.ja.korobov.bank.account.Account;

import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentMap;

public class RemotePerson extends AbstractPerson {

    public RemotePerson(String firstName, String lastName, String passport) {
        super(firstName, lastName, passport);
    }

    public RemotePerson(String firstName, String lastName, String passport, ConcurrentMap<String, Account> accounts) throws RemoteException {
        super(firstName, lastName, passport, accounts);
    }

}
