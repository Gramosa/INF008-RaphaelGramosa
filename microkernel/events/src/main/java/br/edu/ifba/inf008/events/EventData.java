package br.edu.ifba.inf008.events;

import br.edu.ifba.inf008.interfaces.IEventData;

public class EventData<T> implements IEventData<T>{
    private final String event;
    private final T data;

    //Meio que não é obrigatorio definir um callback
    public EventData(String event, T data){
        this.event = event;
        this.data = data;
    }

    public String getEventName(){
        return event;
    }

    public T getData(){
        return data;
    }
}
