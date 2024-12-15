import java.security.NoSuchAlgorithmException;

public class Administrator extends User {
    public Administrator(String name, String email, String password) throws NoSuchAlgorithmException{
        super(name, email, password);
    }
}
