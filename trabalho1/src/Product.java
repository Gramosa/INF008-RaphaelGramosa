import java.io.Serializable;

public class Product implements Serializable {
    private static int productCount = 0;

    private int id;
    private String name;
    private String category;
    private String description;
    private int stock;
    private double price;

    public Product(String name, String category, String description, double price, int stock){
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.id = productCount++;
    }

    public static int getProductCount(){
        return productCount;
    }

    public static void setProductCount(int value){
        productCount = value;
    }

    public double getPrice(){
        return price;
    }

    public int getId(){
        return id;
    }

    public int getStock(){
        return stock;
    }

    public boolean canUpdateStock(int ammount){
        return this.stock + ammount >= 0;
    }

    public boolean updateStock(int ammount){
        if(!canUpdateStock(ammount)){
            return false;
        }

        this.stock += ammount;
        return true;
    }

    public String toText(){
        return String.format(
            "ID: %d\nName: %s\nCategory: %s\nDescription: %s\nPrice: R$ %.2f\nStock: %d",
            id, 
            name, 
            category, 
            description, 
            price, 
            stock
        );
    }
}
