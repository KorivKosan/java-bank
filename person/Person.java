package info.kgeorgiy.ja.korobov.bank.person;

import info.kgeorgiy.ja.korobov.bank.account.Account;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentMap;

public interface Person extends Remote, Serializable {

    /**
     * Getting First Name of {@link Person}
     * */
    String getFirstName() throws RemoteException;

    /**
     * Getting Last Name of {@link Person}
     * */
    String getLastName() throws RemoteException;

    /**
     * Getting Passport of {@link Person}
     * */
    String getPassport() throws RemoteException;

    /**
     * Getting all {@link Account}'s of {@link Person}
     * */
    ConcurrentMap<String, Account> getAccounts() throws RemoteException;
}
