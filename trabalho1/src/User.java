import java.io.Serializable;

abstract public class User implements Serializable {
    static private int userCount = 0;

    private int id;
    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
        this.id = userCount++;
    }

    public int getId(){
        return id;
    }
}
