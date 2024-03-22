package Observing;

public interface IObservable {
    void addObserver(IObserver o);
    void notifyClients(String notification);
}
