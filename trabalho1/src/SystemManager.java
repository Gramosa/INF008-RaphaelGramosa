import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

public class SystemManager implements Serializable{
    private HashMap<Integer, Product> products = new HashMap<Integer, Product>();
    private HashMap<Integer, User> users = new HashMap<Integer, User>();
    private HashMap<Integer, Order> orders = new HashMap<Integer, Order>();

    private transient User currentUser = null;

    public static SystemManager loadFromFile(String fileName) throws Exception{
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);

        int orderCount = ois.readInt();  // Carrega o contador de pedidos
        int userCount = ois.readInt();   // Carrega o contador de usuários
        int productCount = ois.readInt();

        Order.setOrderCount(orderCount);
        User.setUserCount(userCount);
        Product.setProductCount(productCount);

        SystemManager systemManager = (SystemManager) ois.readObject();
        ois.close();

        return systemManager;
    }

    public void saveOnFile(String fileName) throws Exception{
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeInt(Order.getOrderCount());  
        oos.writeInt(User.getUserCount());    
        oos.writeInt(Product.getProductCount());
        
        oos.writeObject(this);
        oos.close();
    }

    private User getUserFromEmail(String email){
        for(User user: users.values()){
            if(email.equals(user.getEmail())){
                return user;
            }
        }
        
        return null;
    }

    public User getUserFromEmailAndPassword(String email, String password) throws NoSuchAlgorithmException{
        User user = getUserFromEmail(email);
        if(user == null){
            System.out.println("Email not found");
            return null;
        }

        String salt = user.getSecurePassword().saltToString();
        SecurePassword securePassword = new SecurePassword(salt);
        securePassword.hashPassword(password);

        if(!user.getSecurePassword().cryptPasswordToString().equals(securePassword.cryptPasswordToString())){
            System.out.println("Wrong password");
            return null;
        }

        return user;
    }

    public void addProduct(Product product){
        products.put(product.getId(), product);
    }

    public boolean addUser(User user){
        if(getUserFromEmail(user.getEmail()) != null){
            //System.out.println("Impossible to add user, email already exists");
            return false;
        }
        users.put(user.getId(), user);
        return true;
    }

    public void addOrders(Order order){
        orders.put(order.getId(), order);
    }

    public Order getMostExpensiveOrder() {
        Order mostExpensiveOrder = null;
        double maxTotal = 0;
    
        for (Order order : orders.values()) {
            double orderTotal = order.calculateOrderPrice();
            if (orderTotal > maxTotal) {
                maxTotal = orderTotal;
                mostExpensiveOrder = order;
            }
        }
    
        return mostExpensiveOrder;
    }

    public Product getProductWithLowestStock() {
        Product productWithLowestStock = null;
        int minStock = Integer.MAX_VALUE;
    
        for (Product product : products.values()) {
            int productStock = product.getStock();
    
            if (productStock < minStock) {
                minStock = productStock;
                productWithLowestStock = product;
            }
        }
    
        return productWithLowestStock;
    }

    public Product getProduct(int id){
        return products.get(id);
    }

    public ArrayList<Product> getAllProducts(){
        return new ArrayList<Product>(products.values());
    }

    public User getUser(int id){
        return users.get(id);
    }

    public Order getOrders(int id){
        return orders.get(id);
    }

    public User getCurrentUser(){
        return currentUser;
    }

    public void setCurrentUser(User user){
        this.currentUser = user;
    }

}
