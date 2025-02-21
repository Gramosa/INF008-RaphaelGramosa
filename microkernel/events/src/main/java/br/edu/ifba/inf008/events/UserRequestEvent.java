package br.edu.ifba.inf008.events;

import br.edu.ifba.inf008.interfaces.IEventData;

public class UserRequestEvent implements IEventData{
    private Integer userId;
    
    public UserRequestEvent(Integer userId){
        this.userId = userId;
    }

    public String getEventName(){
        return "request_user";
    }

    public Integer getUserId(){
        return userId;
    }
}
