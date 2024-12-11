package info.kgeorgiy.ja.korobov.bank.tests;

import info.kgeorgiy.java.advanced.base.BaseTester;

import java.rmi.RemoteException;

public class MyTester {

    public static void main(String[] args) throws RemoteException {
        new BaseTester().add("MyBankTests", BankTest.class).run(new String[]{"MyBankTests", "Ivan Korobov"});
    }

}
