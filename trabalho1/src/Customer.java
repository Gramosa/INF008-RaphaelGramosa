import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;

public class Customer extends User{
    private SystemManager sys;

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
            if (product == null || !product.canUpdateStock(-amount)) {
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

    public void addProductToCart(int productId, int quantity) {
        Product product = sys.getProduct(productId);
        if (product != null) {
            if (quantity <= 0) {
                System.out.println("Quantity must be greater than 0.");
                return;
            }
            cart.updateItem(productId, quantity);
            System.out.println(quantity + " (s) added to your cart.");
        } else {
            System.out.println("Product not found.");
        }
    }

    public void viewCart() {
        if (cart.getCartLenght() == 0) {
            System.out.println("Your cart is empty.");
        } else {
            System.out.println("=== Your Cart ===");
            for (Map.Entry<Integer, Integer> entry : cart.getItems().entrySet()) {
                Product product = sys.getProduct(entry.getKey());
                if (product != null) {
                    System.out.println(product.toText() + " x" + entry.getValue());
                } else {
                    System.out.println("Product not found.");
                }
            }
        }
    }

    public void eraseCart(){
        cart.eraseCart();
    }

    public ArrayList<Order> getOrdersHistory(){
        return ordersHistory;
    }

    public String toText() {
        String userInfo = super.toText();  // Chama o método toText() da classe pai User
        String customerInfo = String.format("Address: %s\nOrders History: %d orders", address, ordersHistory.size());
        return userInfo + "\n" + customerInfo;
    }
}