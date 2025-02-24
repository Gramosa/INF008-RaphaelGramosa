package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IPluginController;
import br.edu.ifba.inf008.interfaces.IPluginListener;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IEventData;
import br.edu.ifba.inf008.interfaces.IUIController;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.control.MenuItem;

public class BookModule implements IPlugin, IPluginListener {
    private HashMap<String, Book> books = new HashMap<>();
    private BookModuleUI bookModuleUI;

    public boolean init() {
        bookModuleUI = new BookModuleUI(this);

        ICore core = ICore.getInstance();
        IUIController uiController = core.getUIController();
        IPluginController pluginController = core.getPluginController();

        // Criar item de menu para abrir a aba "Book Library"
        String menuText = "Book Module";
        MenuItem newBookMenuItem = uiController.createMenuItem(menuText, "Add a new book");
        newBookMenuItem.setOnAction(e -> bookModuleUI.buildCreateBookTab());

        MenuItem searchBookMenuItem = uiController.createMenuItem(menuText, "Search for a book");
        searchBookMenuItem.setOnAction(e -> bookModuleUI.buildSearchBookTab());

        MenuItem listBooksMenuItem = uiController.createMenuItem(menuText, "List all books");
        listBooksMenuItem.setOnAction(e -> bookModuleUI.buildListBooksTab());

        pluginController.subscribe("get_book", this);
        pluginController.subscribe("get_book_string", this);
        return true;
    }

    public boolean addBook(Book book) {
        if (books.containsKey(book.getIsbn())) {
            return false;
        }
        books.put(book.getIsbn(), book);
        return true;
    }

    public Book getBook(String isbn) {
        return books.get(isbn);
    }

    public ArrayList<Book> getBooksByTitle(String title, boolean anyMatch) {
        ArrayList<Book> matchingBooks = new ArrayList<>();

        for (Book book : books.values()) {
            String bookTitle = book.getTitle().toLowerCase(); // Pegar o título do livro em minúsculas
            String searchTitle = title.toLowerCase(); // Pegae a string de busca em minúsculas

            // Da match se conter
            if(anyMatch){
                if (bookTitle.contains(searchTitle)) {
                    matchingBooks.add(book);
                }
            }
            else{
                if(bookTitle.equals(searchTitle)){
                    matchingBooks.add(book);
                }
            }
        }

        return matchingBooks;
    }

    public ArrayList<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, R> R onEvent(IEventData<T> event){
        String eventName = event.getEventName();
        if(eventName.equals("get_book")){
            String isbn = (String) event.getData();
            return (R) getBookResponse(isbn);
        }
        else if(eventName.equals("get_book_string")){
            String isbn = (String) event.getData();
            return (R) getBookResponseString(isbn);
        }
        else{
            return null;
        }
    }

    private ArrayList<String> getBookResponse(String bookIsbn){
        Book book = getBook(bookIsbn);
        if(book == null){
            return new ArrayList<>();
        }

        return book.toArrayList();
    }

    private String getBookResponseString(String bookIsbn){
        Book book = getBook(bookIsbn);
        if(book == null){
            return "";
        }

        return book.toString();
    }
}
