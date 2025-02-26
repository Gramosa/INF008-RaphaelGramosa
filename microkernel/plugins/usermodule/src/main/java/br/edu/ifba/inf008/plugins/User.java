package br.edu.ifba.inf008.plugins;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{
    private int id;
    private String name;

    public User(int id, String name){
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString(){
        return String.format(
            "ID: %s. name: %s.",
            id, name
            );
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public ArrayList<String> toArrayList(){
        ArrayList<String> array = new ArrayList<>();
        array.add(Integer.toString(id));
        array.add(name);
        return array;
    }
}
