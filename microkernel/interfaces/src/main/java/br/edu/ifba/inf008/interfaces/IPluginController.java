package br.edu.ifba.inf008.interfaces;

public interface IPluginController
{
    public abstract boolean init();
    public abstract void subscribe(String event, IPluginListener plugin);
    public <T, R> R emit(IEventData<T> data); //vai ser null se ninguem estiver ouvindo
}
