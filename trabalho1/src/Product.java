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
}
