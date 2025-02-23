package br.edu.ifba.inf008.interfaces;

public interface IEventData<T> {
    public abstract String getEventName();
    public abstract T getData();
}
