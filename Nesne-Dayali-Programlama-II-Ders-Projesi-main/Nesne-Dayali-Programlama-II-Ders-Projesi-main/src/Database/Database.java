package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    // Veri tabanı dosyasının adı
    private static final String URL = "jdbc:sqlite:smartlibrary.db";

    // Bağlantıyı sağlayan metodumuz
    // SQLite veritabanına JDBC üzerinden bağlanır
    public static Connection connect() {
        Connection conn = null;
        try {
            // DriverManager ile bağlantı oluşturulur
            conn = DriverManager.getConnection(URL);
            if (conn != null) {
                // SQLite'da Foreign Key desteği varsayılan olarak kapalı oyüzden açtık
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON;");
                }
            }
        } catch (SQLException e) {
            System.out.println("Bağlantı hatası: " + e.getMessage());
        }
        return conn;
    }

    // Tabloları oluşturan metodumuz (Proje ilk çalıştığında çalışmalı)
    // Eğer tablolar yoksa (IF NOT EXISTS) oluştururuz
    public static void createNewDatabase() {

        // Kitaplar tablosu SQL sorgusu
        String sqlBooks = "CREATE TABLE IF NOT EXISTS books (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " title TEXT NOT NULL,\n"
                + " author TEXT NOT NULL,\n"
                + " year INTEGER\n"
                + ");";

        // Öğrenciler tablosu SQL sorgusu
        String sqlStudents = "CREATE TABLE IF NOT EXISTS students (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " name TEXT NOT NULL,\n"
                + " department TEXT\n"
                + ");";

        // Ödünç işlemleri tablosu SQL sorgusu (Foreign Key içerir)
        String sqlLoans = "CREATE TABLE IF NOT EXISTS loans (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " bookId INTEGER,\n"
                + " studentId INTEGER,\n"
                + " dateBorrowed TEXT,\n"
                + " dateReturned TEXT,\n"
                + " FOREIGN KEY (bookId) REFERENCES books (id),\n"
                + " FOREIGN KEY (studentId) REFERENCES students (id)\n"
                + ");";

        try (Connection conn = connect();
                Statement stmt = conn.createStatement()) {
            // Sorguları sırasıyla çalıştırıyoruz
            stmt.execute(sqlBooks);
            stmt.execute(sqlStudents);
            stmt.execute(sqlLoans);
            // System.out.println("Veri tabanı ve tablolar hazır.");
        } catch (SQLException e) {
            System.out.println("Tablo oluşturma hatası: " + e.getMessage());
        }
    }
}