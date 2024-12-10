import java.io.Serializable;
import java.util.HashMap;

public class ShoppingCart implements Serializable {
    private HashMap<Integer, Integer> items = new HashMap<Integer, Integer>(); // id: quantidade

    private Integer getAmmount(int productId){
        return items.get(productId);
    }

    public void updateProduct(int productId, int ammount){
        if(ammount <= 0){
            System.out.println("Não é possível comprar produtos com uma quantidade negativa");
            return;
        }
        items.put(productId, ammount);
    }

    public void finishCart(){
        
    }
}
