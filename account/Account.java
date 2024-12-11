package info.kgeorgiy.ja.korobov.bank.account;

import java.io.Serializable;
import java.rmi.*;

public interface Account extends Remote, Serializable {

    /**
     * Getting id of {@link Account}
     * */
    String getId() throws RemoteException;

    /**
     * Getting amount of {@link Account}
     * */
    int getAmount() throws RemoteException;

    /**
     * Getting amount of {@link Account}
     * */
    void setAmount(int amount) throws RemoteException;

    /**
     * Add amount to {@link Account}
     * */
    void addAmount(int amount) throws RemoteException;
}
