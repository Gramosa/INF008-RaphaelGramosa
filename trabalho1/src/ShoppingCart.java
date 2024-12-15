import java.io.Serializable;
import java.util.HashMap;

public class ShoppingCart implements Serializable {
    private HashMap<Integer, Integer> items = new HashMap<Integer, Integer>(); // productId: quantidade

    public void eraseCart(){
        items.clear();
    }

    public HashMap<Integer, Integer> getItems(){
        return items;
    }

    public void updateItem(int productId, int ammount){
        if(ammount <= 0){
            System.out.println("Cant add a negative ammount");
            return;
        }

        items.put(productId, ammount);
    }
}
