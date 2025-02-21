package br.edu.ifba.inf008.interfaces;

// A ideia seria que todo plugin que precisar se comunicar com outros plugins implemente essa interface
public interface IPluginListener {
    void onEvent(IEventData data);
}
