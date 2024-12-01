import java.util.HashMap;

public class ShoppingCart {
    private HashMap<Integer, Integer> items = new HashMap<Integer, Integer>(); // id: quantidade

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
