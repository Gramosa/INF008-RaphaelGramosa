package br.edu.ifba.inf008.plugins;

public class User {
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
}
