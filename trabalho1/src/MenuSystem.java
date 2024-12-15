import java.util.Arrays;
import java.util.Scanner;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MenuSystem {
    private SystemManager sys;
    private Menu currentMenu;
    private Scanner scanner = new Scanner(System.in);

    public MenuSystem(SystemManager sys){
        this.sys = sys;
        this.currentMenu = null;
    }

    private void login() throws NoSuchAlgorithmException{
        if (sys.getCurrentUser() == null) {
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            User user = sys.getUserFromEmailAndPassword(email, password);
            if (user == null) {
                System.out.println("Failed to login");
                return;
            }

            sys.setCurrentUser(user);
            System.out.println("Sucess on login");

            if(user instanceof Customer){
                navegate("1");
            }
            else{
                navegate("2");
            }
        }
    }

    public boolean navegate(String path){
        ArrayList<String> indexes = new ArrayList<String>(Arrays.asList(path.split("/")));
        
        for(String index: indexes){
            Menu menu = currentMenu.go(index);
            if(menu == null){
                System.out.println("Invalid switch of menu");
                return false;
            }

            currentMenu = menu;
        }

        return true;
    }

    public void displayCurrentMenu(){
        currentMenu.display();

        if(currentMenu.numberChildren() == 0){
            currentMenu.executeAction();
        }
    }

    public void handleNavegateInput(){
        System.out.print(">>> ");
        String input = scanner.nextLine();
        
        if(currentMenu.numberChildren() < Integer.parseInt(input)){
            System.out.println("Out of range");
        };
    }

    public void startLoop(){
        while(currentMenu != null){
            displayCurrentMenu();

            System.out.print(">>> ");
            String input = scanner.nextLine();
        }
    }

    public void init() {
        currentMenu = new Menu("Main Menu");

        Menu loginMenu = new Menu("Login Menu", "Login");
        Menu AdminStartMenu = new Menu("Admin Menu");
        Menu CustomerStartMenu = new Menu("Customer Menu");

        currentMenu.addSubMenu(loginMenu);
        loginMenu.addSubMenu(AdminStartMenu);
        loginMenu.addSubMenu(CustomerStartMenu);

        loginMenu.setAction(() -> {
            try {
                login();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        });
    }
}
