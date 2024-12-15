import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

public class MenuSystem {
    private SystemManager sys;
    private Menu currentMenu;
    private Scanner scanner = new Scanner(System.in);

    public MenuSystem(SystemManager sys){
        this.sys = sys;
        this.currentMenu = null;
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
        
        if(!currentMenu.haveChildren()){
            currentMenu.executeAction();
        }
    }

    public void init(){
        currentMenu = new Menu("root", "Main Menu", "");

        Menu loginMenu = new Menu("1", "Login Menu", "Login");
        loginMenu.setAction(() -> {
            System.out.print("User name: ");
            String userName = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
        });
    }
}
