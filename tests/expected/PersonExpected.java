package info.kgeorgiy.ja.korobov.bank.tests.expected;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * {@link Class} for check Person
 * */
public class PersonExpected {
    String firstName;
    String lastName;
    String passport;
    ConcurrentMap<String, AccountExpected> accounts = new ConcurrentHashMap<>();


    public PersonExpected(PersonExpected person) {
        this.firstName = person.firstName;
        this.lastName = person.lastName;
        this.passport = person.passport;
        this.accounts = new ConcurrentHashMap<>(person.accounts);
    }

    public PersonExpected(String firstName, String lastName, String passport) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passport = passport;
    }

    /**
     * Getting First Name of {@link PersonExpected}
     * */
    public synchronized String getFirstName() {
        return firstName;
    }

    /**
     * Getting Last Name of {@link PersonExpected}
     * */
    public synchronized String getLastName() {
        return lastName;
    }

    /**
     * Getting Passport of {@link PersonExpected}
     * */
    public synchronized String getPassport() {
        return passport;
    }

    /**
     * Add account for {@link PersonExpected}
     * */
    public synchronized void addAccount(AccountExpected account1) {
        account1.id = passport + ":" + account1.id;
        accounts.put(account1.id, account1);
    }

    /**
     * Get all accounts of {@link PersonExpected}
     * */
    public synchronized ConcurrentMap<String, AccountExpected> getAccounts() {
        return accounts;
    }
}
