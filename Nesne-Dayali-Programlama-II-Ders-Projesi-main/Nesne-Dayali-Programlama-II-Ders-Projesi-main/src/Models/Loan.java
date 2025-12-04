package Models;

// Ödünç alma işlemini temsil eden model sınıfımız
public class Loan {
    private int id;
    private int bookId;
    private int studentId;
    private String bookTitle; // Yeni alan: Kitap Adı
    private String studentName; // Yeni alan: Öğrenci Adı
    private String dateBorrowed;
    private String dateReturned;

    // Constructor (Yeni ödünç verme işlemi için)
    public Loan(int bookId, int studentId, String dateBorrowed) {
        this.bookId = bookId;
        this.studentId = studentId;
        this.dateBorrowed = dateBorrowed;
        this.dateReturned = null; // İlk başta null (henüz iade edilmedi)
    }

    // Constructor (Veritabanından okuma işlemi için - İsimler dahil)
    public Loan(int id, int bookId, String bookTitle, int studentId, String studentName, String dateBorrowed,
            String dateReturned) {
        this.id = id;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.studentId = studentId;
        this.studentName = studentName;
        this.dateBorrowed = dateBorrowed;
        this.dateReturned = dateReturned;
    }

    // Getter Metodlarımız
    public int getId() {
        return id;
    }

    public int getBookId() {
        return bookId;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getDateBorrowed() {
        return dateBorrowed;
    }

    public String getDateReturned() {
        return dateReturned;
    }

    // Ödünç durumunu ekrana yazdırmak için...
    @Override
    public String toString() {
        String status = (dateReturned == null) ? "Ödünçte" : "Teslim Edildi: " + dateReturned;
        // Kitap ve Öğrenci isimleri varsa onları da gösteririz, yoksa sadece ID
        // gösteririz
        // (Geriye dönük uyumluluk için)
        String bTitle = (bookTitle != null) ? " (" + bookTitle + ")" : "";
        String sName = (studentName != null) ? " (" + studentName + ")" : "";

        return "Loan ID: " + id + " | Kitap: " + bookId + bTitle + " | Öğrenci: " + studentId + sName + " | Tarih: "
                + dateBorrowed
                + " | Durum: " + status;
    }
}