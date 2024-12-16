import java.security.NoSuchAlgorithmException;

public class App {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        SystemManager sys;

        try {
            sys = SystemManager.loadFromFile("systemManager.dat");
        } catch (Exception e) {
            sys = new SystemManager();
        }

        Administrator admin = new Administrator("admin", "admin", "admin");
        sys.addUser(admin);

        MenuSystem mSystem = new MenuSystem(sys);
        mSystem.init();
        mSystem.startLoop();

        try {
            sys.saveOnFile("systemManager.dat");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
