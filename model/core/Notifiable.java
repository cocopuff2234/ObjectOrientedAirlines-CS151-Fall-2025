package model.core;

public interface Notifiable {
    void notify(String message);
    default void notifyWithPrefix(String prefix, String message) {
        notify(prefix + ": " + message);
    }
}
