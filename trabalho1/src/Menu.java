import java.util.ArrayList;
import java.lang.Runnable;

public class Menu {
    private String key;
    private String title;
    private String description;
    private Menu parent;
    private ArrayList<Menu> subMenus = new ArrayList<Menu>();
    private Runnable action;

    public Menu(String key, String title, String description){
        this.key = key;
        this.title = title;
        this.description = description;
        this.action = null;
        this.parent = null;
    }

    public String getKey(){
        return this.key;
    }

    public String getDescription(){
        return this.description;
    }

    public String getTitle(){
        return this.title;
    }

    public void setAction(Runnable action){
        this.action = action;
    }

    public void setParent(Menu menu){
        this.parent = menu;
    }

    public boolean addSubMenu(Menu subMenu){
        if(subMenus.contains(subMenu)){
            System.out.println("Failed to add submenu, it was already added");
            return false;
        }

        subMenus.add(subMenu);
        subMenu.setParent(this);
        return true;
    }

    public boolean executeAction(){
        if(action == null){
            System.out.println("There is no action defined to this menu");
            return false;
        }

        action.run();
        return true;
    }

    public boolean haveChildren(){
        return subMenus.size() != 0;
    }

    public Menu go(String key){
        if(key == ".."){
            return parent;
        }

        for(Menu subMenu : subMenus){
            if(subMenu.getKey() == key){
                return subMenu;
            }
        }

        return null;
    }

    public void display(){
        int biggestDescription = 0;
        int titleSize = title.length();

        for(Menu subMenu: subMenus){
            int size = subMenu.title.length();
            if(size > biggestDescription){
                biggestDescription = size;
            }
        }

        biggestDescription += 7 + subMenus.size();

        System.out.print("=".repeat((biggestDescription - titleSize)/2) + " ");
        System.out.print(title);
        System.out.println(" " + "=".repeat((biggestDescription - titleSize)/2));

        for(int i = 0; i < subMenus.size(); i++){
            String line = new String();
            line += "| " + (i + 1) + " - ";
            line += subMenus.get(i).description;
            line += " ".repeat(biggestDescription - line.length() - 2) + " |";
            System.out.println(line);
        }

        System.out.println("=".repeat(biggestDescription));
    }
}
