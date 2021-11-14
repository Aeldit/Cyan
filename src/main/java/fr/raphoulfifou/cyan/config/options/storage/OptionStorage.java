package fr.raphoulfifou.cyan.config.options.storage;

public interface OptionStorage<T> {
    T getData();

    void save();

}
