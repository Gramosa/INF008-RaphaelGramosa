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
}
