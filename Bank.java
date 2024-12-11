package info.kgeorgiy.ja.korobov.bank;

import info.kgeorgiy.ja.korobov.bank.account.Account;
import info.kgeorgiy.ja.korobov.bank.person.Person;
import info.kgeorgiy.ja.korobov.bank.person.PersonType;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentMap;

public interface Bank extends Remote {

    /**
     * Creating {@link Account} with transmitted id
     *
     * @param id {@link String} with id of {@link Account}
     * @return {@link Account} with transmitted id
     * @throws RemoteException if has errors with remote
     * */
    Account createAccount(String id) throws RemoteException;

    /**
     * Getting {@link Account} with transmitted id
     *
     * @param id {@link String} with id of {@link Account}
     * @return {@link Account} with transmitted id
     * @throws RemoteException if has errors with remote
     * */
    Account getAccount(String id) throws RemoteException;

    /**
     * Getting {@link Person} with transmitted passport and type of {@link Person}
     *
     * @param passport {@link String} with passport of {@link Person}
     * @param type {@link PersonType} with type of {@link Person}
     * @return {@link Person} with transmitted passport and type
     * @throws RemoteException if has errors with remote
     * */
    Person getPerson(String passport, PersonType type) throws RemoteException;

    /**
     * Creating {@link Person} with transmitted first name, last name and passport
     *
     * @param firstName {@link String} with first name of {@link Person}
     * @param lastName {@link String} with last name of {@link Person}
     * @param passport {@link String} with passport of {@link Person}
     * @return {@link Person} with transmitted first name, last name and passport
     * @throws RemoteException if has errors with remote
     * */
    Person createPerson(String firstName, String lastName, String passport) throws RemoteException;

    /**
     * Creating {@link Account} for {@link Person} with transmitted passport and id
     *
     * @param passport {@link String} with passport of {@link Person}
     * @param id {@link String} with subid of {@link Account}
     * @return {@link Account} for {@link Person} with transmitted passport and id
     * @throws RemoteException if has errors with remote
     * */
    Account createPersonAccount(String passport, String id) throws RemoteException;

    /**
     * Getting all {@link Person} in bank
     *
     * @return {@link ConcurrentMap} with all {@link String} passports and {@link Person} in bank
     * @throws RemoteException if has errors with remote
     * */
    ConcurrentMap<String, Person> getAllPersons() throws RemoteException;
}
