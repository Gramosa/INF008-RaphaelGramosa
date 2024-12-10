import java.io.Serializable;

public class Product implements Serializable {
    private static int productCount = 0;

    private int id;
    private String name;
    private String category;
    private String description;
    private int stock;
    private double price;

    public Product(String name, String category, String description, double price){
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.id = productCount++;
    }

    public Product(String name, String category, String description, double price, int initialStock) {
        this(name, category, description, price);
        this.stock = initialStock;
    }

    public double getPrice(){
        return price;
    }

    public void updateStock(int ammount){
        this.stock += ammount;
    }
}
