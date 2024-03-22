package Entities.Accounts;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreditAccount extends BaseAccount {
    public CreditAccount(UUID _id) {
        super(_id);
    }

    @Override
    public void WithdrawMoney(Double value) throws Exception {
        if(balance - value < bank.getCreditLimit()) throw new Exception("The credit limit is reached");
        balance -= value;
    }
}
