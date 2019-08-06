package net.socialhangover.tag.config;

public abstract class ConfigKey<T> {
    public abstract T get();

    public abstract void set(T value);
}
