import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

public class SystemManager implements Serializable{
    private HashMap<Integer, Product> products = new HashMap<Integer, Product>();
    private HashMap<Integer, User> users = new HashMap<Integer, User>();

    public static SystemManager loadFromFile(String fileName) throws Exception{
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        SystemManager systemManager = (SystemManager) ois.readObject();
        ois.close();

        return systemManager;
    }

    public void saveOnFile(String fileName) throws Exception{
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.close();
    }
}
