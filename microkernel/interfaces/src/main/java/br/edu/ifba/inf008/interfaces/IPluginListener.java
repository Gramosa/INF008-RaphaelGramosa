package br.edu.ifba.inf008.interfaces;

// A ideia seria que todo plugin que precisar se comunicar com outros plugins implemente essa interface
public interface IPluginListener {
    abstract <T, R> R onEvent(IEventData<T> data);
}
