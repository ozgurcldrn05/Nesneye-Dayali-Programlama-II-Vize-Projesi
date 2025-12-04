package Repositories;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Models.Loan;
import Database.Database;

public class LoanRepository {

    // Kitap ödünç verme işlemini gerçekleştiren metodumuz
    public void add(Loan loan) {
        // Geçersiz nesne kontrolümüz
        if (loan == null) {
            System.out.println("HATA: Geçersiz ödünç işlemi.");
            return;
        }
        // Önce kitap müsait mi kontrol ediyoruz (Başka birinde mi?)
        if (isBookBorrowed(loan.getBookId())) {
            System.out.println("HATA: Bu kitap şu anda başkasında!");
            return;
        }

        // SQL INSERT sorgusu
        String sql = "INSERT INTO loans(bookId, studentId, dateBorrowed) VALUES(?,?,?)";
        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, loan.getBookId());
            pstmt.setInt(2, loan.getStudentId());
            pstmt.setString(3, loan.getDateBorrowed());
            pstmt.executeUpdate();
            System.out.println("Kitap ödünç verildi.");
        } catch (SQLException e) {
            // Foreign Key hatası kontrolümüz (Olmayan kitap veya öğrenci ID'si)
            if (e.getMessage().contains("FOREIGN KEY")) {
                System.out.println("HATA: Kitap ID veya Öğrenci ID bulunamadı. Lütfen mevcut ID'leri kullanınız.");
            } else {
                System.out.println("Veritabanı hatası: " + e.getMessage());
            }
        }
    }

    // Kitabın şu an ödünçte olup olmadığını kontrol eden metodumuz
    // Eğer dateReturned alanı NULL ise kitap henüz teslim edilmemiştir
    public boolean isBookBorrowed(int bookId) {
        String sql = "SELECT count(*) FROM loans WHERE bookId = ? AND dateReturned IS NULL";
        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // 0'dan büyükse kitap dışarıdadır
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    // Kitap İade (Update işlemi)
    // İlgili kaydın dateReturned alanını güncelliyoruz
    public void returnBook(int bookId, String returnDate) {
        String sql = "UPDATE loans SET dateReturned = ? WHERE bookId = ? AND dateReturned IS NULL";
        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, returnDate);
            pstmt.setInt(2, bookId);
            int affectedRows = pstmt.executeUpdate();
            // Etkilenen satır sayısı kontrolümüz
            if (affectedRows > 0) {
                System.out.println("Kitap başarıyla teslim alındı.");
            } else {
                System.out.println("Bu kitap zaten teslim alınmış veya hiç ödünç verilmemiş.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Tüm ödünç geçmişini listelemek için kullanılan metodumuz
    public List<Loan> getAll() {
        List<Loan> loans = new ArrayList<>();
        // JOIN işlemi ile kitap ve öğrenci isimlerini de çekiyoruz
        String sql = "SELECT loans.id, loans.bookId, books.title, loans.studentId, students.name, loans.dateBorrowed, loans.dateReturned "
                +
                "FROM loans " +
                "LEFT JOIN books ON loans.bookId = books.id " +
                "LEFT JOIN students ON loans.studentId = students.id";
        try (Connection conn = Database.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                loans.add(new Loan(
                        rs.getInt("id"),
                        rs.getInt("bookId"),
                        rs.getString("title"), // Kitap Adı
                        rs.getInt("studentId"),
                        rs.getString("name"), // Öğrenci Adı
                        rs.getString("dateBorrowed"),
                        rs.getString("dateReturned")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return loans;
    }
}