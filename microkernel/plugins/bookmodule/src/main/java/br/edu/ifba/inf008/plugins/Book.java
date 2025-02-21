package br.edu.ifba.inf008.plugins;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private String pubDate;
    private String genre;

    public Book(String isbn, String title, String author, String pubDate, String genre){
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
            isbn, title, author, pubDate, genre
            );
    }

    public String getIsbn(){
        return isbn;
    }

    public String getTitle(){
        return title;
    }
}
