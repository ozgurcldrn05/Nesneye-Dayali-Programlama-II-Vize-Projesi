package Models;

// Kitap verilerini temsil eden model sınıfımız (POJO)
public class Book {
    private int id;
    private String title;
    private String author;
    private int year;

    // Constructor (Ekleme yaparken id gerekmez, DB otomatik atar)
    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    // Constructor (Listeleme yaparken id gerekli)
    public Book(int id, String title, String author, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    // Getter ve Setter metodlarımız (Verilere erişim için)
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    // Nesneyi String olarak yazdırmak için override edilen metodumuz
    @Override
    public String toString() {
        return id + ". " + title + " - " + author + " (" + year + ")";
    }
}