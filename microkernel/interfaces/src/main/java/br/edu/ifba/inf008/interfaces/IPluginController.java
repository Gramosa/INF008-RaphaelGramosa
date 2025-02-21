package br.edu.ifba.inf008.interfaces;

public interface IPluginController
{
    public abstract boolean init();
    public abstract void subscribe(String event, IPluginListener plugin);
    public abstract boolean emit(String event, IEventData data); //Vai ser falso se ninguem ouvir a emissão
}
