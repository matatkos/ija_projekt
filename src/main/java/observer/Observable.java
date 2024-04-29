package observer;

import java.util.ArrayList;
import java.util.List;

public class Observable {
    private List<Observer> observers; // List of observers.

    public Observable() {
        observers = new ArrayList<>();
    }


    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public interface Observer {

        void update();
    }
}
