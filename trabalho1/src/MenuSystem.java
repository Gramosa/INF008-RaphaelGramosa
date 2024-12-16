import java.util.Arrays;
import java.util.Scanner;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MenuSystem {
    private SystemManager sys;
    private Menu currentMenu;
    private Scanner scanner = new Scanner(System.in);

    public MenuSystem(SystemManager sys) {
        this.sys = sys;
        this.currentMenu = null;
    }

    private void login() throws NoSuchAlgorithmException {

        String email = getInput("Email:");
        String password = getInput("Password:");

        User user = sys.getUserFromEmailAndPassword(email, password);
        if (user == null) {
            System.out.println("Failed to login");
            return;
        }

        sys.setCurrentUser(user);
        System.out.println("Success on login");

        if (user instanceof Administrator) {
            navegate("1");
        } else {
            navegate("2");
        }

    }

    private void logout() {
        sys.setCurrentUser(null);
        System.out.println("You have been logged out.");
        navegate("0/0");
    }

    private void showProductById() {
        int id = getIntegerInput("Enter product ID:");
        Product product = sys.getProduct(id);

        if (product != null) {
            System.out.println(product.toText());
        } else {
            System.out.println("Product not found.");
        }
    }

    private void createProduct() {
        String name = getInput("Product name:");
        String category = getInput("Product category:");
        String description = getInput("Product description:");
        double price = getDoubleInput("Product price:");
        int stock = getIntegerInput("Product stock:");

        Product newProduct = new Product(name, category, description, price, stock);
        sys.addProduct(newProduct);
        System.out.println("Product created successfully");
    }

    private void createUser() throws NoSuchAlgorithmException {
        String name = getInput("Full Name:");
        String email = getInput("Email:");
        String password = getInput("Password:");

        String role;
        while (true) {
            role = getInput("Is the user an Admin or Customer? (A/C):").toUpperCase();
            if (role.equals("A") || role.equals("C")) {
                break;
            }
            System.out.println("Invalid role. Please enter 'A' for Admin or 'C' for Customer.");
        }

        User newUser;
        if (role.equals("A")) {
            newUser = new Administrator(name, email, password);
        } else {
            String address = getInput("Delivery Address:");
            newUser = new Customer(name, email, password, address, sys);
        }

        if (sys.addUser(newUser)) {
            System.out.println("User created successfully!");
        } else {
            System.out.println("Failed to create user: Email already exists.");
        }
    }

    private void mostExpensiveOrderReport() {
        Order mostExpensiveOrder = sys.getMostExpensiveOrder();
        if (mostExpensiveOrder != null) {
            System.out.println("\n=== Most Expensive Order ===");
            System.out.println(mostExpensiveOrder.toText());
        } else {
            System.out.println("No orders found.");
        }
    }

    private void lowestStockReport() {
        Product lowestStockProduct = sys.getProductWithLowestStock();
        if (lowestStockProduct != null) {
            System.out.println("\n=== Product with Lowest Stock ===");
            System.out.println(lowestStockProduct.toText());
        } else {
            System.out.println("No products found.");
        }
    }

    //Customer
    private void viewProducts() {
        System.out.println("=== All Available Products ===");
        for (Product product : sys.getAllProducts()) {
            System.out.println(product.toText());
        }
    }

    private void addProductToCart() {
        int productId = getIntegerInput("Enter product ID to add to cart:");
        int quantity = getIntegerInput("Enter quantity to add:");
    
        // Chama o método da classe Customer para adicionar o produto ao carrinho
        Customer currentCustomer = (Customer) sys.getCurrentUser();
        currentCustomer.addProductToCart(productId, quantity);
    }
    
    private void viewCart() {
        Customer currentCustomer = (Customer) sys.getCurrentUser();
        currentCustomer.viewCart();
    }

    private void discardCart(){
        Customer currentCustomer = (Customer) sys.getCurrentUser();
        currentCustomer.eraseCart();
    }

    private void checkout() {
        Customer currentCustomer = (Customer) sys.getCurrentUser();
        Order order = currentCustomer.finishCart();

        if (order != null) {
            sys.addOrders(order);
            System.out.println("Order placed successfully.");
        } else {
            System.out.println("Failed to place order.");
        }
    }

    private void viewOrderHistory() {
        Customer currentCustomer = (Customer) sys.getCurrentUser();
        ArrayList<Order> orders = currentCustomer.getOrdersHistory();
        
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
        } else {
            System.out.println("=== Your Order History ===");
            System.out.println(currentCustomer.toText());
            
            for (Order order : orders) {
                System.out.println(order.toText());
            }
        }
    }

    public boolean navegate(String path) {
        ArrayList<String> indexes = new ArrayList<>(Arrays.asList(path.split("[/ ]")));

        for (String index : indexes) {
            // System.out.println("navegate: " + index);

            Menu menu = currentMenu.go(index);
            if (menu == null) {
                System.out.println("Invalid menu option");
                return false;
            }
            currentMenu = menu;
        }

        return true;
    }

    public String getInput(String label) {
        System.out.print(label + " ");
        return scanner.nextLine();
    }

    public int getIntegerInput(String label) {
        int input = 0;
        boolean valid = false;

        while (!valid) {
            String userInput = getInput(label);
            try {
                input = Integer.parseInt(userInput);
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Wrong input. Must be integer.");
            }
        }
        return input;
    }

    public double getDoubleInput(String label) {
        double input = 0.0;
        boolean valid = false;

        while (!valid) {
            String userInput = getInput(label);
            try {
                input = Double.parseDouble(userInput);
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Wrong input. Must be double");
            }
        }

        return input;
    }

    public void startLoop() {
        while (currentMenu != null) {
            currentMenu.executeAction();
            if(!currentMenu.getTitle().equals("Login Menu")){
                currentMenu.display();
                handleNavegateInput();
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        }
    }

    public boolean handleNavegateInput() {
        int input = getIntegerInput(">>>");

        // System.out.println("handleNavegateInput: " + input);
        if (input < 0 || currentMenu.numberChildren() < input) {
            System.out.println("Out of range");
            return false;
        }

        if(currentMenu.getTitle().equals("Admin Menu") || currentMenu.getTitle().equals("Customer Menu")){
            if(input == 0){
                logout();
                return true;
            }
        }

        if (currentMenu.getTitle().equals("Main Menu") && input == 0) {
            currentMenu = null; // Encerra o loop no startLoop
            return true;
        }

        navegate(String.valueOf(input));
        return true;
    }

    public void init() {
        currentMenu = new Menu("Main Menu");

        Menu loginMenu = new Menu("Login Menu", "Login");
        currentMenu.addSubMenu(loginMenu);

        Menu adminStartMenu = new Menu("Admin Menu");
        loginMenu.addSubMenu(adminStartMenu);

        Menu customerStartMenu = new Menu("Customer Menu");
        loginMenu.addSubMenu(customerStartMenu);

        // Menus de Gerenciamento de Produtos e Usuários
        Menu productManagementMenu = new Menu("Product Management Menu", "Manage Products");
        adminStartMenu.addSubMenu(productManagementMenu);

        Menu createUserMenu = new Menu("Create User", "Create a new user");
        adminStartMenu.addSubMenu(createUserMenu);

        // Menu de Relatórios
        Menu reportsMenu = new Menu("Reports", "View Reports");
        adminStartMenu.addSubMenu(reportsMenu);

        // Menus de Ações
        Menu listAllProductsMenu = new Menu("List All Products", "List All Products");
        Menu findProductMenu = new Menu("Find Product by ID", "Find Product");
        Menu createProductMenu = new Menu("Create Product", "Create Product");

        // Relatórios Específicos
        Menu mostExpensiveOrderMenu = new Menu("Most Expensive Order", "View Most Expensive Order");
        Menu lowestStockProductMenu = new Menu("Lowest Stock Product", "View Product with Lowest Stock");

        Menu viewProductsMenu = new Menu("View Products", "View All Products");
        Menu addToCartMenu = new Menu("Add to Cart", "Add Product to Cart");
        Menu viewCartMenu = new Menu("View Cart", "View Your Cart");
        Menu discardCardMenu = new Menu("Discard ShoppingCart", "Erase your cart");
        Menu checkoutMenu = new Menu("Checkout", "Finalize Your Order");
        Menu orderHistoryMenu = new Menu("Order History", "View Your Orders");

        customerStartMenu.addSubMenu(viewProductsMenu);
        customerStartMenu.addSubMenu(addToCartMenu);
        customerStartMenu.addSubMenu(viewCartMenu);
        customerStartMenu.addSubMenu(discardCardMenu);
        customerStartMenu.addSubMenu(checkoutMenu);
        customerStartMenu.addSubMenu(orderHistoryMenu);

        viewProductsMenu.setAction(this::viewProducts);
        addToCartMenu.setAction(this::addProductToCart);
        viewCartMenu.setAction(this::viewCart);
        discardCardMenu.setAction(this::discardCart);
        checkoutMenu.setAction(this::checkout);
        orderHistoryMenu.setAction(this::viewOrderHistory);

        // Adicionando Submenus
        productManagementMenu.addSubMenu(listAllProductsMenu);
        productManagementMenu.addSubMenu(findProductMenu);
        productManagementMenu.addSubMenu(createProductMenu);

        reportsMenu.addSubMenu(mostExpensiveOrderMenu);
        reportsMenu.addSubMenu(lowestStockProductMenu);

        // Ações dos Menus
        listAllProductsMenu.setAction(this::viewProducts);
        findProductMenu.setAction(this::showProductById);
        createProductMenu.setAction(this::createProduct);

        mostExpensiveOrderMenu.setAction(this::mostExpensiveOrderReport);
        lowestStockProductMenu.setAction(this::lowestStockReport);

        createUserMenu.setAction(() -> {
            try {
                createUser();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        });

        loginMenu.setAction(() -> {
            try {
                login();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        });
    }
}
