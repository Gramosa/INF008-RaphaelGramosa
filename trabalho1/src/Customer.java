import java.io.Serializable;
import java.util.ArrayList;

public class Customer extends User implements Serializable {
    private ShoppingCart cart = new ShoppingCart();
    private ArrayList<Order> ordersHistory = new ArrayList<Order>();
    private String address;

    public Customer(String name, String email, String password, String address){
        super(name, email, password);
        this.address = address;
    }
}
