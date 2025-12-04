package Models;

// Öğrenci verilerini temsil eden model sınıfımız
public class Student {
    private int id;
    private String name;
    private String department;

    // Constructor (Yeni öğrenci eklerken kullanıyoruz)
    public Student(String name, String department) {
        this.name = name;
        this.department = department;
    }

    // Constructor (Veritabanından okurken kullanıyoruz)
    public Student(int id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    // Getter metodları
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    // Nesneyi ekrana yazdırmak için format,
    @Override
    public String toString() {
        return id + ". " + name + " (" + department + ")";
    }
}