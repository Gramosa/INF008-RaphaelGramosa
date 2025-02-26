package br.edu.ifba.inf008.plugins;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Book implements Serializable{
    private String isbn;
    private String title;
    private String author;
    private LocalDate pubDate;
    private String genre;

    public Book(String isbn, String title, String author, LocalDate pubDate, String genre){
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.pubDate = pubDate;
        this.genre = genre;
    }

    @Override
    public String toString(){
        return String.format(
            "ISBN: %s. Title: %s. Author: %s. PubDate: %s. Genre: %s.",
            isbn, title, author, pubDate.toString(), genre
            );
    }

    public String getIsbn(){
        return isbn;
    }

    public String getTitle(){
        return title;
    }

    public ArrayList<String> toArrayList(){
        ArrayList<String> array = new ArrayList<>();
        array.add(isbn);
        array.add(title);
        array.add(author);
        array.add(pubDate.toString());
        array.add(genre);

        return array;
    }
}
