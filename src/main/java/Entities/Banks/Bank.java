package Entities.Banks;

import Entities.Accounts.BaseAccount;
import Entities.Accounts.CreditAccount;
import Entities.Accounts.DebitAccount;
import Entities.Accounts.DepositAccount;
import Entities.Clients.Client;
import Observing.IObservable;
import Observing.IObserver;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@Builder
public class Bank implements IObservable {
    private String name;
    private CentralBank centralBank;
    private Map<UUID, BaseAccount> accounts;
    private List<IObserver> observers;
    private Map<Client, List<UUID>> clientAccounts;
    private double debitInterest;
    private Map<Double, Double> depositInterests;
    private Double creditLimit;
    private Double creditCommission;
    private Double withdrawAndTransferLimit;

    public void AddClient(Client newClient) {
        clientAccounts.put(newClient, new ArrayList<UUID>());
    }

    public void AddDebitAccount(Client client) {
        DebitAccount account = new DebitAccount(UUID.randomUUID());
        if(client.getAddress().isEmpty() && client.getPassportNumber() == -1) account.setIsSuspicious(true);
        accounts.put(account.getId(), account);

        if (!clientAccounts.containsKey(client)) AddClient(client);

        clientAccounts.get(client).add(account.getId());
    }

    public void AddDepositAccount(Client client, Integer daysBeforeUnblocking) {
        DepositAccount account = new DepositAccount(UUID.randomUUID(), daysBeforeUnblocking);
        if(client.getAddress().isEmpty() && client.getPassportNumber() == -1) account.setIsSuspicious(true);
        accounts.put(account.getId(), account);

        if (!clientAccounts.containsKey(client)) AddClient(client);

        clientAccounts.get(client).add(account.getId());
    }

    public void AddCreditAccount(Client client) {
        CreditAccount account = new CreditAccount(UUID.randomUUID());
        if(client.getAddress().isEmpty() && client.getPassportNumber() == -1) account.setIsSuspicious(true);
        accounts.put(account.getId(), account);

        if (!clientAccounts.containsKey(client)) AddClient(client);

        clientAccounts.get(client).add(account.getId());
    }

    public void TransferMoney(UUID recipientId, UUID senderId, double value) throws Exception {
        if (accounts.containsKey(recipientId)) {
            accounts.get(recipientId).ReplenishAccount(value);
        } else {
            accounts.get(senderId).ReplenishAccount(value);
            throw new Exception("The recipient's ID is incorrect");
        }
    }

    public Double getDepositeInterest(Double balance) {
        return depositInterests.get(depositInterests.keySet()
                .stream()
                .filter(x -> x <= balance)
                .mapToDouble(Double::valueOf)
                .max()
                .orElse(0D));
    }

    public void AccrueInterests() {
        for (BaseAccount account : accounts.values()) {
            if (account.getClass().equals(DebitAccount.class)) {
                ((DebitAccount) account).setAccumulatedInterest(((DebitAccount) account).getAccumulatedInterest() + account.getBalance() * (debitInterest / 100));
            }
            if (account.getClass().equals(DepositAccount.class)) {
                ((DepositAccount) account).setAccumulatedInterest(((DepositAccount) account).getAccumulatedInterest() + account.getBalance() * (getDepositeInterest(account.getBalance()) / 100));
            }
        }
    }

    public void PayInterests() {
        for (BaseAccount account : accounts.values()) {
            if(account.getClass().equals(DebitAccount.class)) {
                account.ReplenishAccount(((DebitAccount) account).getAccumulatedInterest());
            }
            if(account.getClass().equals(DepositAccount.class)) {
                account.ReplenishAccount(((DepositAccount) account).getAccumulatedInterest());
            }
        }
    }

    public void WithdrawCommissions() {
        for(BaseAccount account : accounts.values()) {
            if(account.getClass().equals(CreditAccount.class) && account.getBalance() < 0) {
                account.setBalance(account.getBalance() - (-(account.getBalance()) * (creditCommission / 100)));
            }
        }
    }

    /*public void TransactionCancel() {

    }*/

    public void ChangeDebitInterest(Double newInterest) {
        debitInterest = newInterest;
        notifyClients("Debit interest has been changed");
    }

    public void ChangeDepositInterest(Map<Double, Double> newDepositInterest) {
        depositInterests = newDepositInterest;
        notifyClients("Deposit interest has been changed");
    }

    public void ChangeCreditLimit(Double newCreditLimit) {
        creditLimit = newCreditLimit;
        notifyClients("Credit limit has been changed");
    }

    public void ChangeCreditCommission(Double newCreditCommission) {
        creditCommission = newCreditCommission;
        notifyClients("Credit commission has been changed");
    }

    public void ChangeWithdrawAndTransferLimit(Double newWithdrawAndTransferLimit) {
        withdrawAndTransferLimit = newWithdrawAndTransferLimit;
        notifyClients("Withdraw and transfer limit has been changed");
    }

    @Override
    public void addObserver(IObserver o) {
        observers.add(o);
    }

    @Override
    public void notifyClients(String notification) {
        for(IObserver o : observers) {
            o.modifyClient(notification);
        }
    }
}
