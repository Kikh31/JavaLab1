package Entities.Accounts;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DebitAccount extends BaseAccount {
    private Double accumulatedInterest;

    public DebitAccount(UUID _id) {
        super(_id);
    }
}
