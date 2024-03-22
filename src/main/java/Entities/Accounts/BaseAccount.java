package Entities.Accounts;

import Entities.Banks.Bank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public abstract class BaseAccount {
    protected UUID id;
    protected Bank bank;
    protected Double balance;
    //private Collection<>;
    protected Boolean IsSuspicious;

    public BaseAccount(UUID _id) {
        id = _id;
    }

    public void WithdrawMoney(Double value) throws Exception {
        if(value > balance) throw new Exception("Insufficient funds");
        balance -= value;
    }

    public void ReplenishAccount(Double value) {
        balance += value;
    }

    public void TransferMoney(UUID recipientId, double value) throws Exception {
        this.WithdrawMoney(value);
        bank.TransferMoney(recipientId, this.id, value);
    }

    public void TransferMoney(String bankName, UUID recipientId, double value) throws Exception {
        this.WithdrawMoney(value);
        bank.getCentralBank().TransferMoney(bankName, recipientId, this.bank.getName(), this.id, value);
    }
}
