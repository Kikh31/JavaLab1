package Entities.Banks;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
public class CentralBank {
    private HashMap<String, Bank> banks;

    public CentralBank() {
        banks = new HashMap<String, Bank>();
    }

    public void CreateBank(Bank newBank) {
        banks.put(newBank.getName(), newBank);
    }

    public void TransferMoney(String recipientBankName, UUID recipientId, String senderBankName, UUID senderId, double value) throws Exception {
        if (banks.containsKey(recipientBankName) && banks.get(recipientBankName).getAccounts().containsKey(recipientId)) {
            banks.get(recipientBankName).TransferMoney(recipientId, senderId, value);
        } else {
            banks.get(senderBankName).getAccounts().get(senderId).ReplenishAccount(value);
            throw new Exception("The recipient's bank or ID is incorrectly specified");
        }
    }

    public void BankOperations() {

    }

    public void AccrueInterests() {
        banks.values().forEach(Bank::AccrueInterests);
    }

    public void PayInterests() {
        banks.values().forEach(Bank::PayInterests);
    }

    public void WithdrawCommission() {
        banks.values().forEach(Bank::WithdrawCommissions);
    }
}
