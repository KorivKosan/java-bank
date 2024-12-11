package info.kgeorgiy.ja.korobov.bank.tests;

import info.kgeorgiy.ja.korobov.bank.account.Account;
import info.kgeorgiy.ja.korobov.bank.Bank;
import info.kgeorgiy.ja.korobov.bank.RemoteBank;
import info.kgeorgiy.ja.korobov.bank.person.Person;
import info.kgeorgiy.ja.korobov.bank.person.PersonType;
import info.kgeorgiy.ja.korobov.bank.tests.expected.AccountExpected;
import info.kgeorgiy.ja.korobov.bank.tests.expected.PersonExpected;
import org.junit.jupiter.api.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class BankTest {
    protected static final Random RANDOM = new Random(1313131313131313L);
    private static final List<String> FIRST_NAMES = List.of("Ivan", "Ksenia", "Artemiy", "Kirill", "Dmitriy", "Timur");
    private static final List<String> LAST_NAMES = List.of("Korobov", "Kirushkina", "Mitcelovskiy", "Dukhanin", "Petukhov", "Biktimirov");
    private final static int PORT = 8888;
    Bank bank;
    static Registry registry;

    private static <T> T random(final List<T> values) {
        return values.get(RANDOM.nextInt(values.size()));
    }

    protected static final List<PersonExpected> PERSONS = RANDOM.ints(5)
        .mapToObj(id -> {
            String firstName = random(FIRST_NAMES);
            String lastName = random(LAST_NAMES);
            String passport = firstName + lastName + "_" + id;
            return new PersonExpected(firstName, lastName, passport);
        }).toList();

    public BankTest() {}

    @BeforeAll
    public static void startServer() throws RemoteException {
        registry = LocateRegistry.createRegistry(PORT);
    }

    @BeforeEach
    public void restartServer() throws RemoteException {
        bank = new RemoteBank(PORT);
        UnicastRemoteObject.exportObject(bank, PORT);
        registry.rebind("//localhost/bank", bank);
    }

    @AfterAll
    public static void completeServer() throws RemoteException {
        UnicastRemoteObject.unexportObject(registry, true);
    }

    @AfterEach
    public void cancelAll() throws RemoteException, NotBoundException {
        for (Person person : bank.getAllPersons().values()) {
            for (Account account : person.getAccounts().values()) {
                UnicastRemoteObject.unexportObject(account, true);
            }
            UnicastRemoteObject.unexportObject(person, true);
        }
        UnicastRemoteObject.unexportObject(bank, true);
        registry.unbind("//localhost/bank");
    }

    public static <R> void test(List<Person> received, List<PersonExpected> expected) {
        IntStream.range(0, expected.size()).forEach(i -> {
            try {
                equalsAllPersonFields(received.get(i), expected.get(i));
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public static void equalsAllPersonFields(Person received, PersonExpected expected) throws RemoteException {
        Assertions.assertEquals(expected.getFirstName(), received.getFirstName());
        Assertions.assertEquals(expected.getLastName(), received.getLastName());
        Assertions.assertEquals(expected.getPassport(), received.getPassport());
        Assertions.assertEquals(expected.getAccounts().size(), received.getAccounts().size());
        for (String id : expected.getAccounts().keySet()) {
            Assertions.assertEquals(expected.getAccounts().get(id).getAmount(),
                received.getAccounts().get(id).getAmount());
        }
    }

    @Test
    public void test01_createPerson() throws RemoteException {
        List<Person> received01 = new ArrayList<>();
        List<PersonExpected> expected01 = new ArrayList<>();

        PersonExpected personExpected = new PersonExpected(random(PERSONS));
        expected01.add(personExpected);

        received01.add(createPerson(personExpected));

        test(received01, expected01);
    }

    @Test
    public void test02_createPersonAccount() throws RemoteException {
        List<Person> received02 = new ArrayList<>();
        List<PersonExpected> expected02 = new ArrayList<>();

        PersonExpected personExpected = new PersonExpected(random(PERSONS));
        personExpected.addAccount(new AccountExpected("account1", 0));
        expected02.add(personExpected);

        Person person = createPerson(personExpected);
        createPersonAccount(person.getPassport(), "account1");
        received02.add(person);

        test(received02, expected02);
    }

    @Test
    public void test03_changeAmount() throws RemoteException {
        List<Person> received03 = new ArrayList<>();
        List<PersonExpected> expected03 = new ArrayList<>();

        PersonExpected personExpected = new PersonExpected(random(PERSONS));
        personExpected.addAccount(new AccountExpected("account1", 100));
        expected03.add(personExpected);

        Person person = createPerson(personExpected);
        Account personAccount = createPersonAccount(person.getPassport(), "account1");
        personAccount.setAmount(personAccount.getAmount() + 100);
        received03.add(person);

        test(received03, expected03);
    }

    @Test
    public void test04_getLocalPersonBeforeCreateAccount() throws RemoteException {
        List<Person> received04 = new ArrayList<>();
        List<PersonExpected> expected04 = new ArrayList<>();

        PersonExpected personExpected = new PersonExpected(random(PERSONS));
        PersonExpected personExpectedLocal = new PersonExpected(personExpected);
        personExpected.addAccount(new AccountExpected("account1", 0));
        expected04.add(personExpected);
        expected04.add(personExpectedLocal);

        Person person = createPerson(personExpected);
        Person personLocal = bank.getPerson(person.getPassport(), PersonType.LOCAL);
        createPersonAccount(person.getPassport(), "account1");
        received04.add(person);
        received04.add(personLocal);

        test(received04, expected04);
    }

    @Test
    public void test05_getLocalPersonAfterCreateAccount() throws RemoteException {
        List<Person> received05 = new ArrayList<>();
        List<PersonExpected> expected05 = new ArrayList<>();

        PersonExpected personExpected = new PersonExpected(random(PERSONS));
        personExpected.addAccount(new AccountExpected("account1", 0));
        PersonExpected personExpectedLocal = new PersonExpected(personExpected);
        expected05.add(personExpected);
        expected05.add(personExpectedLocal);

        Person person = createPerson(personExpected);
        createPersonAccount(person.getPassport(), "account1");
        Person personLocal = bank.getPerson(person.getPassport(), PersonType.LOCAL);
        received05.add(person);
        received05.add(personLocal);

        test(received05, expected05);
    }

    @Test
    public void test06_getRemotePersonBeforeCreateAccount() throws RemoteException {
        List<Person> received06 = new ArrayList<>();
        List<PersonExpected> expected06 = new ArrayList<>();

        PersonExpected personExpected = new PersonExpected(random(PERSONS));
        personExpected.addAccount(new AccountExpected("account1", 0));
        PersonExpected personExpectedRemote = new PersonExpected(personExpected);
        expected06.add(personExpected);
        expected06.add(personExpectedRemote);

        Person person = createPerson(personExpected);
        Person personRemote = bank.getPerson(person.getPassport(), PersonType.REMOTE);
        createPersonAccount(person.getPassport(), "account1");
        received06.add(person);
        received06.add(personRemote);

        test(received06, expected06);
    }

    @Test
    public void test07_changeAmountLocalAccount() throws RemoteException {
        List<Person> received07 = new ArrayList<>();
        List<PersonExpected> expected07 = new ArrayList<>();

        PersonExpected personExpected = new PersonExpected(random(PERSONS));
        PersonExpected personExpectedLocal = new PersonExpected(personExpected);
        personExpected.addAccount(new AccountExpected("account1", 0));
        personExpectedLocal.addAccount(new AccountExpected("account1", 100));
        expected07.add(personExpected);
        expected07.add(personExpectedLocal);

        Person person = createPerson(personExpected);
        Account personAccount = createPersonAccount(person.getPassport(), "account1");
        Person personLocal = bank.getPerson(person.getPassport(), PersonType.LOCAL);
        personLocal.getAccounts().get(personLocal.getPassport() + ":" + "account1").setAmount(100);
        received07.add(person);
        received07.add(personLocal);

        test(received07, expected07);
    }

    @Test
    public void test08_changeAmountRemoteAccount() throws RemoteException {
        List<Person> received08 = new ArrayList<>();
        List<PersonExpected> expected08 = new ArrayList<>();

        PersonExpected personExpected = new PersonExpected(random(PERSONS));
        PersonExpected personExpectedRemote = new PersonExpected(personExpected);
        personExpected.addAccount(new AccountExpected("account1", 100));
        personExpectedRemote.addAccount(new AccountExpected("account1", 100));
        expected08.add(personExpected);
        expected08.add(personExpectedRemote);

        Person person = createPerson(personExpected);
        createPersonAccount(person.getPassport(), "account1");
        Person personRemote = bank.getPerson(person.getPassport(), PersonType.REMOTE);
        personRemote.getAccounts().get(personRemote.getPassport() + ":" + "account1").setAmount(100);
        received08.add(person);
        received08.add(personRemote);

        test(received08, expected08);
    }

    @Test
    public void test09_checkThreadSafety() throws RemoteException {
        List<Person> received09 = new ArrayList<>();
        List<PersonExpected> expected09 = new ArrayList<>();

        PersonExpected personExpected = new PersonExpected(random(PERSONS));
        personExpected.addAccount(new AccountExpected("account1", 1000));
        expected09.add(personExpected);

        Person person = createPerson(personExpected);
        Account account = createPersonAccount(person.getPassport(), "account1");

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                try {
                    account.addAmount(100);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        received09.add(person);

        test(received09, expected09);
    }

    private Account createPersonAccount(String passport, String id) throws RemoteException {
        return bank.createPersonAccount(passport, id);
    }

    private Person createPerson(PersonExpected personExpected) throws RemoteException {
        return bank.createPerson(personExpected.getFirstName(), personExpected.getLastName(), personExpected.getPassport());
    }
}
