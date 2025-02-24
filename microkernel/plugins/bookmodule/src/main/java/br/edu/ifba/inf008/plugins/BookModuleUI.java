package br.edu.ifba.inf008.plugins;

import java.util.ArrayList;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class BookModuleUI {
    private BookModule bookModule;
    private IUIController uiController;

    public BookModuleUI(BookModule bookModule) {
        this.bookModule = bookModule;
        uiController = ICore.getInstance().getUIController();
    }

    public void buildCreateBookTab() {
        // Criando os campos de entrada
        TextField isbnField = new TextField();
        isbnField.setPromptText("ISBN");

        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField authorField = new TextField();
        authorField.setPromptText("Author");

        TextField pubDateField = new TextField();
        pubDateField.setPromptText("Publication date");

        TextField genreField = new TextField();
        genreField.setPromptText("genre");

        // Botão para adicionar um livro
        Button addButton = new Button("Add book");
        addButton.setOnAction(e -> {
            String isbn = isbnField.getText();
            String title = titleField.getText();
            String author = authorField.getText();
            String pubDate = pubDateField.getText();
            String genre = genreField.getText();

            if (isbn == null || isbn.isEmpty() ||
                title == null || title.isEmpty() ||
                author == null || author.isEmpty() ||
                pubDate == null || pubDate.isEmpty() ||
                genre == null || genre.isEmpty()) {
                
                    uiController.showPopup("Please fill in all fields!");
                    return;
            }

            Book book = new Book(isbn, title, author, pubDate, genre);

            if (bookModule.addBook(book)) {
                uiController.showPopup(String.format("Book \"%s\" added with success!", titleField.getText()));
            } else {
                uiController.showPopup("ISBN already exists!");
            }
        });

        // Layout da aba
        VBox layout = new VBox(10, isbnField, titleField, authorField, pubDateField, genreField, addButton);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        // Criar a aba no UIController
        uiController.createTabOnRight("Create book", layout);
    }

    public void buildSearchBookTab(){
        // Criando os campos de entrada
        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        // Botão para buscar um livro
        Button searchButton = new Button("Search Book");
        searchButton.setOnAction(e -> {
            String title = titleField.getText();

            ArrayList<Book> matchingBooks = bookModule.getBooksByTitle(title, true);
            if (!matchingBooks.isEmpty()) {
                for(Book book : matchingBooks){
                    uiController.createTabOnLeft("Search Book Query", book.toString());
                }
                uiController.showPopup("Search match!");
            } else {
                uiController.showPopup("Not a single book found!");
            }
        });

        // Layout da aba
        VBox layout = new VBox(10, titleField, searchButton);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        // Criar a aba no UIController
        uiController.createTabOnRight("Search Book", layout);
    }

    public void buildListBooksTab() {
        ICore core = ICore.getInstance();
        IUIController uiController = core.getUIController();

        // Botão para listar os livros
        Button listButton = new Button("List Books");
        listButton.setOnAction(e -> {
            for (Book book : bookModule.getAllBooks()) {
                uiController.createTabOnLeft("List Books Query", book.toString());
            }
            uiController.showPopup(String.format("%d books found!",bookModule.getAllBooks().size()));
        });

        // Layout da aba
        VBox layout = new VBox(10, listButton);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        // Criar a aba no UIController
        uiController.createTabOnRight("List Books", layout);
    }
}
