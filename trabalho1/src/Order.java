import java.time.LocalDateTime;
import java.util.ArrayList;

public class Order {
    private static int orderCount = 0;

    private int id;
    private LocalDateTime creationDate;
    private ArrayList<OrderItem> items = new ArrayList<OrderItem>();

    public Order(){
        this.id = orderCount++;
        this.creationDate = LocalDateTime.now();
    }

    public void addProduct(Product product, int ammount){
        items.add(new OrderItem(product, ammount));
    }

    public double calculateOrderPrice(){
        double price = 0;
        for (OrderItem item : items) {
            price += item.calculateTotalPrice();
        }

        return price;
    }

    static private class OrderItem {
        private Product product;
        private int ammount;
    
        public OrderItem(Product product, int ammount){
            this.product = product;
            this.ammount = ammount;
        }
    
        public double calculateTotalPrice(){
            return product.getPrice() * ammount;
        }
    }
}
