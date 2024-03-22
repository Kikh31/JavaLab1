package Entities.Accounts;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DepositAccount extends BaseAccount {
    private Double accumulatedInterest;
    private Integer daysBeforeUnblocking;
    public DepositAccount(UUID _id, Integer _daysBeforeUnblocking) {
        super(_id);
        daysBeforeUnblocking = _daysBeforeUnblocking;
    }

    @Override
    public void WithdrawMoney(Double value) throws Exception {
        if(daysBeforeUnblocking > 0) throw new Exception("Withdrawal of funds is blocked");
        super.WithdrawMoney(value);
    }

    @Override
    public void TransferMoney(UUID recipientId, double value) throws Exception {
        if(daysBeforeUnblocking > 0) throw new Exception("Transfer of funds is blocked");
        super.TransferMoney(recipientId, value);
    }

    @Override
    public void TransferMoney(String bankName, UUID recipientId, double value) throws Exception {
        if(daysBeforeUnblocking > 0) throw new Exception("Transfer of funds is blocked");
        super.TransferMoney(bankName, recipientId, value);
    }
}
