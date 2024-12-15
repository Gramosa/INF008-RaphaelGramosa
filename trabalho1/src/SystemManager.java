import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

public class SystemManager implements Serializable{
    private HashMap<Integer, Product> products = new HashMap<Integer, Product>();
    private HashMap<Integer, User> users = new HashMap<Integer, User>();
    private HashMap<Integer, Order> orders = new HashMap<Integer, Order>();

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

    public void addProduct(Product product){
        products.put(product.getId(), product);
    }

    public void addUser(User user){
        users.put(user.getId(), user);
    }

    public void addOrders(Order order){
        orders.put(order.getId(), order);
    }

    public Product getProduct(int id){
        return products.get(id);
    }

    public User getUser(int id){
        return users.get(id);
    }

    public Order getOrders(int id){
        return orders.get(id);
    }
}
