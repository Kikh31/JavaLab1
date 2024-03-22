package Entities.Clients;

import Observing.IObserver;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Client implements IObserver {
    private String name;
    private String surname;
    private String address;
    private Integer passportNumber;
    private List<String> notifications;

    @Override
    public void modifyClient(String notification) {
        notifications.add(notification);
    }
}
