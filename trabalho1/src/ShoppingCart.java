import java.io.Serializable;
import java.util.HashMap;

public class ShoppingCart implements Serializable {
    private HashMap<Product, Integer> items = new HashMap<Product, Integer>(); // product: quantidade

    private Integer getAmmount(Product product){
        return items.get(product);
    }

    private void eraseCart(){
        items.clear();
    }

    private Order generateOrder(){
        Order order = new Order();
        for(Product product : items.keySet()){
            order.addProduct(product, getAmmount(product));
        }
        return order;
    }

    public 

    public Order finishCart(){
        Order order = generateOrder();
        eraseCart();

        return order;
    }

    public void updateItem(Product product, int ammount){
        if(ammount <= 0){
            System.out.println("Não é possível comprar produtos com uma quantidade negativa");
            return;
        }
        items.put(product, ammount);
    }
}
