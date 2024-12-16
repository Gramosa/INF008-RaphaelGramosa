import java.util.ArrayList;
import java.lang.Runnable;

public class Menu {
    private String title;
    private String description;
    private Menu parent;
    private ArrayList<Menu> subMenus = new ArrayList<Menu>();
    private Runnable action;

    public Menu(String title){
        this.title = title;
        this.action = null;
        this.parent = null;
    }

    public Menu(String title, String description){
        this(title);
        this.description = description;
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
            //System.out.println("There is no action defined to this menu");
            return false;
        }

        action.run();
        return true;
    }

    public int numberChildren(){
        return subMenus.size();
    }

    public Menu go(String index){
        // System.out.println("go: " + index);
        if(index.equals("0")){
            return parent;
        }

        return subMenus.get(Integer.parseInt(index) - 1);
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
    
        biggestDescription += 7 + numberChildren();
    
        System.out.print("=".repeat(Math.max(0, (biggestDescription - titleSize) / 2)) + " ");
        System.out.print(title);
        System.out.println(" " + "=".repeat(Math.max(0, (biggestDescription - titleSize) / 2)));
    
        for(int i = 0; i < numberChildren(); i++){
            String line = new String();
            line += "| " + (i + 1) + " - ";
            line += subMenus.get(i).description;
            line += " ".repeat(Math.max(0, biggestDescription - line.length() - 2)) + " |";
            System.out.println(line);
        }
    
        System.out.print("| 0 - ");
        if(parent == null){
            System.out.println("exit");
        }
        else{
            System.out.println("return");
        }
    
        System.out.println("=".repeat(Math.max(0, biggestDescription)));
    }
    
}
