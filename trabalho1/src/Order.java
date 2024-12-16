import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Order implements Serializable{
    private static int orderCount = 0;

    private int id;
    private LocalDateTime creationDate;
    private ArrayList<OrderItem> items = new ArrayList<OrderItem>();

    public static int getOrderCount(){
        return orderCount;
    }

    public static void setOrderCount(int value){
        orderCount = value;
    }

    public Order(){
        this.id = orderCount++;
        this.creationDate = LocalDateTime.now();
    }

    public int getId(){
        return id;
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

    static private class OrderItem implements Serializable {
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

    public String toText() {
        // Inicializando o texto da ordem com seu ID e data de criação
        String text = String.format("Order ID: %d\n", id);
        text += String.format("Creation Date: %s\n", creationDate);
    
        // Adicionando detalhes dos itens da ordem
        text += "Items:\n";
        for (OrderItem item : items) {
            text += String.format("%s - Quantity: %d - Subtotal: %.2f\n", 
                                  item.product.toText(), 
                                  item.ammount, 
                                  item.calculateTotalPrice());
        }
    
        // Calculando o preço total da ordem
        text += String.format("Total Price: %.2f\n", calculateOrderPrice());
    
        return text;
    }
}
