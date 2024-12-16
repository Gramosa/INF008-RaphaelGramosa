import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SecurePassword implements Serializable{
    private byte[] cryptPassword;
    private byte[] salt = new byte[16];

    public SecurePassword(){}

    public SecurePassword(String saltString){
        this.salt = Base64.getDecoder().decode(saltString);
    }

    public void generateSalt(){
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
    }

    public void hashPassword(String plainPassword) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        cryptPassword = md.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
    }

    public String saltToString(){
        return Base64.getEncoder().encodeToString(salt);
    }

    public String cryptPasswordToString(){
        return Base64.getEncoder().encodeToString(cryptPassword);
    }
}
