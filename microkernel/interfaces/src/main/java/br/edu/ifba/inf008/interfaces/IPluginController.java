package br.edu.ifba.inf008.interfaces;

import java.util.function.Consumer;

import br.edu.ifba.inf008.interfaces.ICore;

public interface IPluginController
{
    public abstract boolean init();
    public abstract void subscribe(String event, Consumer<Object> listener);
    public abstract void emit(String event, Object data);
}
