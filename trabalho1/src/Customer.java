import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;

public class Customer extends User{
    private transient SystemManager sys;

    private ShoppingCart cart = new ShoppingCart();
    private ArrayList<Order> ordersHistory = new ArrayList<Order>();
    private String address;

    public Customer(String name, String email, String password, String address, SystemManager sys) throws NoSuchAlgorithmException{
        super(name, email, password);
        this.address = address;

        this.sys = sys;
    }

    private boolean validateCartItems() {
        for (Map.Entry<Integer, Integer> pair : cart.getItems().entrySet()) {
            int productId = pair.getKey();
            int amount = pair.getValue();

            Product product = sys.getProduct(productId);
            if (product == null || !product.canUpdateStock(amount)) {
                return false;
            }
        }
        return true;
    }

    private Order processCartItems() {
        Order order = new Order();

        for (Map.Entry<Integer, Integer> pair : cart.getItems().entrySet()) {
            Product product = sys.getProduct(pair.getKey());
            int amount = pair.getValue();
            product.updateStock(-amount);
            order.addProduct(product, amount);
        }

        cart.eraseCart();
        
        return order;
    }

    public Order finishCart() {
        if (!validateCartItems()) {
            System.out.println("Erro: Um ou mais itens no carrinho não podem ser processados.");
            return null;
        }
        
        Order order = processCartItems();
        ordersHistory.add(order);
        return order;
    }
}
