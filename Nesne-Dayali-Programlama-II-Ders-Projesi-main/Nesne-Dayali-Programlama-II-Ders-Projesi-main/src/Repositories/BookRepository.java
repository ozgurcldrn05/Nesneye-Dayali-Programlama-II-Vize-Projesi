package Repositories;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Models.Book;
import Database.Database;

public class BookRepository {

    // Yeni bir kitap eklemek için kullanılan metodumuz
    public void add(Book book) {
        // Gelen nesnenin ve alanlarının geçerliliğini kontrol ediyoruz
        if (book == null || book.getTitle() == null || book.getTitle().trim().isEmpty() ||
                book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            System.out.println("HATA: Geçersiz kitap bilgisi. Kitap adı ve yazar boş olamaz.");
            return;
        }
        // SQL INSERT sorgusu (Parametreli sorgu - SQL Injection önler)
        String sql = "INSERT INTO books(title, author, year) VALUES(?,?,?)";
        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Parametreleri atıyoruz
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setInt(3, book.getYear());
            // Sorguyu çalıştırıyoruz
            pstmt.executeUpdate();
            System.out.println("Kitap başarıyla eklendi.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Kitap silme metodumuz
    public void delete(int id) {
        // 1. Kitap şu an ödünçte mi kontrol ediyoruz
        if (isBookBorrowed(id)) {
            System.out.println("HATA: Bu kitap şu an ödünçte olduğu için silinemez! Önce teslim alınız.");
            return;
        }

        // 2. Kitabın geçmiş ödünç kayıtlarını temizliyoruz
        deleteBookHistory(id);

        // 3. Kitabı silme işlemi
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Kitap başarıyla silindi.");
            } else {
                System.out.println("HATA: Silinecek kitap bulunamadı.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean isBookBorrowed(int bookId) {
        String sql = "SELECT count(*) FROM loans WHERE bookId = ? AND dateReturned IS NULL";
        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private void deleteBookHistory(int bookId) {
        String sql = "DELETE FROM loans WHERE bookId = ?";
        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Geçmiş kayıtlar temizlenirken hata: " + e.getMessage());
        }
    }

    // Tüm kitapları listelemek için kullanılan metodumuz
    public List<Book> getAll() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection conn = Database.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            // Sonuç kümesi (ResultSet) üzerinde dönüyoruz
            while (rs.next()) {
                // Her satırı bir Book nesnesine dönüştürüp listeye ekliyoruz
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("year")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return books;
    }
}