package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.events.EventData;

import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IPluginController;
import br.edu.ifba.inf008.interfaces.IPluginListener;
import br.edu.ifba.inf008.interfaces.IPluginSerialization;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IEventData;
import br.edu.ifba.inf008.interfaces.IUIController;

import java.io.Serializable;
import java.net.URLClassLoader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.scene.control.MenuItem;

public class LoanModule implements IPlugin, IPluginListener, IPluginSerialization{
    private static final String fileName = "LoanModule.dat";

    private HashMap<Integer, ArrayList<Loan>> loans = new HashMap<>(); //userId, loans
    private HashMap<Integer, ArrayList<Loan>> archivedLoans = new HashMap<>();
    private LoanModuleUI loanModuleUI;

    private IPluginController pluginController;
    private IUIController uiController;

    public boolean init() {
        pluginController = ICore.getInstance().getPluginController();
        uiController = ICore.getInstance().getUIController();
        loanModuleUI = new LoanModuleUI(this);

        ICore core = ICore.getInstance();
        IUIController uiController = core.getUIController();
        pluginController = core.getPluginController();

        // Criar item de menu para abrir a aba "Loan Library"
        String menuText = "Loan Module";
        MenuItem borrowBookMenuItem = uiController.createMenuItem(menuText, "Loan a book");
        borrowBookMenuItem.setOnAction(e -> loanModuleUI.buildBorrowBookTab());

        MenuItem returnLoanMenuItem = uiController.createMenuItem(menuText, "Return a loan");
        returnLoanMenuItem.setOnAction(e -> loanModuleUI.buildReturnBookTab());

        // MenuItem listLoansMenuItem = uiController.createMenuItem(menuText, "List all loans");
        // listLoansMenuItem.setOnAction(e -> loanModuleUI.buildListLoansTab());

        MenuItem userTransactionsMenuItem = uiController.createMenuItem(menuText, "Check user transactions");
        userTransactionsMenuItem.setOnAction(e -> loanModuleUI.buildUserLoansTransaction());
        
        pluginController.subscribe("get_loaned_isbns", this);

        return true;
    }

    @Override
    public void saveData(){
        HashMap<String, Serializable> pluginData = new HashMap<>();

        pluginData.put("loans", loans);
        pluginData.put("archivedLoans", archivedLoans);

        save(pluginData, fileName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadData(URLClassLoader ulc) {
        HashMap<String, Serializable> pluginData = load(fileName, ulc);
        if(pluginData.isEmpty()){
            return;
        }
        loans = (HashMap<Integer, ArrayList<Loan>>) pluginData.get("loans");
        archivedLoans = (HashMap<Integer, ArrayList<Loan>>) pluginData.get("archivedLoans");
    }

    public boolean addLoan(Integer userId, String bookIsbn, LocalDate borrowDate) {
        if(getLoanFromIsbn(loans, bookIsbn) != null){
            uiController.showPopup("Book were already borrowed!");
            return false;
        }

        if(!borrowDate.isBefore(LocalDate.now())){
            uiController.showPopup("A book cannot be borrowed in the future!");
            return false;
        }
        
        // Valida se o modulo de User esta funcional
        if(!checkEntityExistence(new EventData<>("get_user", userId), userId.toString(), "User")){
            return false;
        }

        // Valida se o modulo de Book esta funcional
        if(!checkEntityExistence(new EventData<>("get_book", bookIsbn), bookIsbn, "Book")){
            return false;
        }

        if(!loans.containsKey(userId)){
            loans.put(userId, new ArrayList<>());
        }

        if(loans.get(userId).size() == 5){
            uiController.showPopup("User cannot loan more books, reached the max (5 books)");
            return false;
        }

        loans.get(userId).add(new Loan(bookIsbn, userId, borrowDate));
        return true;
    }

    public boolean finishLoan(String bookIsbn){
        Loan loan = getLoanFromIsbn(loans, bookIsbn);
        if(loan == null){
            uiController.showPopup(String.format("There is no active loan for the ISBN \"%s\"", bookIsbn));
            return false;
        }

        Integer userId = loan.getUserId();
        if(!archivedLoans.containsKey(userId)){
            archivedLoans.put(userId, new ArrayList<>());
        }

        archivedLoans.get(userId).add(loan);
        loan.finishLoan();

        // Remover o empréstimo da lista de loans associada ao userId
        ArrayList<Loan> userLoans = loans.get(userId);
        if (userLoans != null) {
            userLoans.remove(loan);

            // Se não houver mais empréstimos para o usuário, remove o hashmap inteiro
            if (userLoans.isEmpty()) {
                loans.remove(userId);
            }
        }

        return true;
    }

    public String getUserLoansDetails(Integer userId, boolean active, boolean notActive){
        String txt = "";

        txt += "+ User data +";
        txt += "\n" + solveUser(userId);
        txt += "\n";

        if(active){
            txt += "\n+ Active Loans +";
            List<String> bookDetails = solveUserBooks(loans, userId);
            if(bookDetails.isEmpty()){
                txt += "\nNo active loans found.";
            }
            else{
                for(String bookDetail : bookDetails){
                    txt += "\n" + bookDetail;
                }
            }
        }
        if(active && notActive){
            txt += "\n";
        }
        if(notActive){
            txt += "\n+ Archived Loans +";
            List<String> bookDetails = solveUserBooks(archivedLoans, userId);
            if (bookDetails.isEmpty()) {
                txt += "\nNo archived loans found.";
            } else {
                for(String bookDetail : bookDetails){
                    txt += "\n" + bookDetail;
                }
            }
        }

        return txt;
    }

    public Loan getLoanFromIsbn(HashMap<Integer, ArrayList<Loan>> collection, String isbn) {
        for (ArrayList<Loan> userLoans : collection.values()) {
            for (Loan loan : userLoans) {
                if (loan.getBookIsbn().equals(isbn)) {
                    return loan;
                }
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, R> R onEvent(IEventData<T> event){
        if(event.getEventName().equals("get_loaned_isbns")){
            return (R) getLoanedIsbnsResponse();
        }
        else{
            return null;
        }
    }

    // Somente os empretismos ativos no momento
    private ArrayList<String> getLoanedIsbnsResponse(){
        ArrayList<String> array = new ArrayList<>();

        for(ArrayList<Loan> userLoans : loans.values()){
            for(Loan loan : userLoans){
                array.add(loan.getBookIsbn());
            }
        }

        return array;
    }

    private <T> Boolean checkEntityExistence(IEventData<T> eventData, String identifier, String entityName) {
        ArrayList<String> data = pluginController.emit(eventData);
        
        if (data == null) {
            uiController.showPopup("No answer from " + entityName + " module");
            return false;
        } else if (data.isEmpty()) {
            uiController.showPopup(String.format("Entity %s with identifier %s does not exist", entityName, identifier));
            return false;
        }
        return true;
    }

    private String solveBook(String isbn){
        String txt = pluginController.emit(new EventData<String>("get_book_string", isbn));
        if(txt == null){
            uiController.showPopup(String.format("Book module did not respond", isbn));
            return String.format("Isbn: %s.", isbn);
        }
        if(txt.isEmpty()){
            uiController.showPopup(String.format("Cannot solve book with isbn \"%s\"", isbn));
            return String.format("Isbn: %s. No data", isbn);
        }
        
        return txt;
    }

    private String solveUser(Integer userId){
        String txt = pluginController.emit(new EventData<Integer>("get_user_string", userId));
        
        if(txt == null){
            uiController.showPopup("User module did not respond");
            return String.format("Id: %d.", userId);
        }
        if(txt.isEmpty()){
            uiController.showPopup(String.format("Cannot solve user with id \"%d\"", userId));
            return String.format("Id: %d. No data", userId);
        }
        
        return txt;
    }

    private ArrayList<String> solveUserBooks(HashMap<Integer, ArrayList<Loan>> collection, Integer userId){
        ArrayList<String> array = new ArrayList<>();
        ArrayList<Loan> userLoans = collection.get(userId);

        if(userLoans == null || userLoans.isEmpty()){
            return array;
        }

        for(Loan loan : userLoans){
            String bookInfo = solveBook(loan.getBookIsbn());
            bookInfo += String.format("\t\nLoan date: %s", loan.getLoanDate().toString());
            if(loan.getReturnDate() != null){
                bookInfo += String.format("\t\nReturn date: %s", loan.getReturnDate().toString());
            }
            
            array.add(bookInfo);
        }

        return array;
    }
}
