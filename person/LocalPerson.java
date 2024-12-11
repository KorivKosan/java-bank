package info.kgeorgiy.ja.korobov.bank.person;

import info.kgeorgiy.ja.korobov.bank.account.Account;
import info.kgeorgiy.ja.korobov.bank.account.LocalAccount;

import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocalPerson extends AbstractPerson  {

    public LocalPerson(Person person, ConcurrentMap<String, Account> accounts) throws RemoteException {
        super(person.getFirstName(), person.getLastName(), person.getPassport(), getLocalAccounts(accounts));
    }

    private static ConcurrentMap<String, Account> getLocalAccounts(ConcurrentMap<String, Account> accounts) throws RemoteException {
        ConcurrentMap<String, Account> localAccounts = new ConcurrentHashMap<>();
        for (String id : accounts.keySet()) {
            localAccounts.put(id, new LocalAccount(id, accounts.get(id).getAmount()));
        }
        return localAccounts;
    }

    public Account addLocalAccount(Account account) throws RemoteException {
        Account localAccount = new LocalAccount(account.getId(), account.getAmount());
        accounts.put(account.getId(), localAccount);
        return localAccount;
    }
}
