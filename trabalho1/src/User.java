import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

abstract public class User implements Serializable {
    static private int userCount = 0;

    private int id;
    private String name;
    private String email;
    private SecurePassword password;

    public User(String name, String email, String password) throws NoSuchAlgorithmException{
        this.name = name;
        this.email = email;

        this.password = new SecurePassword();
        this.password.hashPassword(password);
        
        this.id = userCount++;
    }

    public int getId(){
        return id;
    }

    public String getEmail(){
        return this.email;
    }

    public SecurePassword getSecurePassword(){
        return this.password;
    }
}
