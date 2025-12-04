package Repositories;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Models.Student;
import Database.Database;

public class StudentRepository {

    // Yeni bir öğrenci eklemek için kullanılan metodumuz
    public void add(Student student) {
        // Gelen nesnenin ve alanlarının geçerliliğini kontrol ediyoruz
        if (student == null || student.getName() == null || student.getName().trim().isEmpty() ||
                student.getDepartment() == null || student.getDepartment().trim().isEmpty()) {
            System.out.println("HATA: Geçersiz öğrenci bilgisi. İsim ve bölüm boş olamaz.");
            return;
        }
        // SQL INSERT sorgusu
        String sql = "INSERT INTO students(name, department) VALUES(?,?)";
        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Parametreleri atıyoruz
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getDepartment());
            // Sorguyu çalıştırıyoruz
            pstmt.executeUpdate();
            System.out.println("Öğrenci başarıyla eklendi.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Öğrenci silme metodu
    public void delete(int id) {
        // 1. Aktif (teslim edilmemiş) ödünç kaydı var mı kontrol et
        if (hasActiveLoan(id)) {
            System.out.println(
                    "HATA: Bu öğrencinin üzerinde teslim edilmemiş kitaplar var. Önce kitapları teslim alınız.");
            return;
        }

        // 2. Geçmiş ödünç kayıtlarını temizle (Foreign Key hatasını önlemek için)
        deleteStudentHistory(id);

        // 3. Öğrenciyi sil
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Öğrenci başarıyla silindi.");
            } else {
                System.out.println("HATA: Silinecek öğrenci bulunamadı.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Öğrencinin üzerinde kitap var mı kontrolü
    private boolean hasActiveLoan(int studentId) {
        String sql = "SELECT count(*) FROM loans WHERE studentId = ? AND dateReturned IS NULL";
        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    // Öğrencinin geçmiş ödünç kayıtlarını silme
    private void deleteStudentHistory(int studentId) {
        String sql = "DELETE FROM loans WHERE studentId = ?";
        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Geçmiş kayıtlar temizlenirken hata: " + e.getMessage());
        }
    }

    // Tüm öğrencileri listelemek için kullanılan metodumuz
    public List<Student> getAll() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Connection conn = Database.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            // Sonuç kümesi üzerinde dönüyoruz
            while (rs.next()) {
                // Her satırı bir Student nesnesine dönüştürüp listeye ekliyoruz
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return students;
    }
}