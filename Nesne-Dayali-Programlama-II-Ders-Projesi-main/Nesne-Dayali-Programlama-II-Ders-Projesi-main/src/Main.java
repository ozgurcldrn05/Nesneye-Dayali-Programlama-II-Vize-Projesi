import java.util.Scanner;
import java.util.List;
import java.util.stream.Collectors;
import java.io.PrintStream;
import Models.*;
import Repositories.*;
import Database.Database;

public class Main {
    // Uygulamamın başlangıç noktası
    public static void main(String[] args) {
        // Türkçe karakterler için konsol ayarımızı yapıyoruz:
        // Windows terminalini UTF-8 moduna alıyoruz (chcp 65001)
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                new ProcessBuilder("cmd", "/c", "chcp", "65001").inheritIO().start().waitFor();
            }
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (Exception e) {
            // Hata olursa varsayılan ayarlarla devam ediyoruz
        }

        // Veri tabanını hazırla (Tablolar yoksa oluşturulur)
        Database.createNewDatabase();

        // Scanner'ı try-with-resources ile oluşturuyoruz
        try (Scanner scanner = createScanner()) {

            // Repository nesneleri (Veritabanı işlemleri için)
            // (Kompozisyon) mantığı ile ana sınıfta kullanılırlar
            BookRepository bookRepo = new BookRepository();
            StudentRepository studentRepo = new StudentRepository();
            LoanRepository loanRepo = new LoanRepository();

            // Sonsuz döngü ile kullanıcımız çıkış yapana kadar menüyü gösteriyoruz
            while (true) {
                System.out.println("\n--- SMART LIBRARY Y\u00d6NET\u0130M S\u0130STEM\u0130 ---");
                System.out.println("1. Kitap Ekle");
                System.out.println("2. Kitaplar\u0131 Listele");
                System.out.println("3. Kitap Sil");
                System.out.println("4. \u00d6\u011frenci Ekle");
                System.out.println("5. \u00d6\u011frencileri Listele");
                System.out.println("6. \u00d6\u011frenci Sil");
                System.out.println("7. Kitap \u00d6d\u00fcn\u00e7 Ver");
                System.out.println("8. \u00d6d\u00fcn\u00e7 Listesini G\u00f6r\u00fcnt\u00fcle");
                System.out.println("9. Kitap Geri Teslim Al");
                System.out.println("0. \u00c7\u0131k\u0131\u015f");
                System.out.print("Se\u00e7iminiz: ");

                int choice = -1;
                try {
                    // Kullanıcının seçimini alıp sayıya çevirme
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("L\u00fctfen ge\u00e7erli bir say\u0131 giriniz.");
                    continue; // Hatalı girişte döngünün başına dönüyoruz
                }

                switch (choice) {
                    case 1:
                        // KİTAP EKLEME İŞLEMİ
                        System.out.println("--- Kitap Ekleme (\u0130ptal i\u00e7in '0' yaz\u0131n\u0131z) ---");
                        System.out.print("Kitap Ad\u0131: ");
                        String title = scanner.nextLine();
                        if (title.equals("0")) {
                            System.out.println("\u0130\u015flem iptal edildi.");
                            break;
                        }
                        // Boş giriş kontrolümüz
                        if (title.trim().isEmpty()) {
                            System.out.println("HATA: Kitap ad\u0131 bo\u015f olamaz!");
                            break;
                        }
                        System.out.print("Yazar: ");
                        String author = scanner.nextLine();
                        if (author.trim().isEmpty()) {
                            System.out.println("HATA: Yazar ad\u0131 bo\u015f olamaz!");
                            break;
                        }
                        System.out.print("Y\u0131l: ");
                        int year;
                        try {
                            year = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("HATA: Y\u0131l ge\u00e7erli bir say\u0131 olmal\u0131d\u0131r!");
                            break;
                        }
                        // Yeni kitap nesnesi oluşturup veritabanına ekliyoruz
                        bookRepo.add(new Book(title, author, year));
                        break;

                    case 2:
                        // KİTAPLARI LİSTELEME İŞLEMİ
                        List<Book> books = bookRepo.getAll();
                        System.out.println("\n--- Kitap Listesi ---");
                        if (books.isEmpty()) {
                            System.out.println("UYARI: Listelenecek kitap bulunmamaktad\u0131r.");
                        } else {
                            for (Book b : books) {
                                System.out.println(b); // Book.toString() metodunu çağırıyoruz
                            }
                        }
                        break;

                    case 3:
                        // KİTAP SİLME İŞLEMİ
                        System.out.println("--- Kitap Silme (\u0130ptal i\u00e7in '0' yaz\u0131n\u0131z) ---");
                        System.out.print("Silinecek Kitap ID: ");
                        String deleteBookIdInput = scanner.nextLine();
                        if (deleteBookIdInput.equals("0")) {
                            System.out.println("\u0130\u015flem iptal edildi.");
                            break;
                        }
                        int deleteBookId;
                        try {
                            deleteBookId = Integer.parseInt(deleteBookIdInput);
                            bookRepo.delete(deleteBookId);
                        } catch (NumberFormatException e) {
                            System.out.println("HATA: Kitap ID say\u0131sal olmal\u0131d\u0131r!");
                        }
                        break;

                    case 4:
                        // ÖĞRENCİ EKLEME İŞLEMİ
                        System.out.println(
                                "--- \u00d6\u011frenci Ekleme (\u0130ptal i\u00e7in '0' yaz\u0131n\u0131z) ---");
                        System.out.print("\u00d6\u011frenci Ad\u0131: ");
                        String name = scanner.nextLine();
                        if (name.equals("0")) {
                            System.out.println("\u0130\u015flem iptal edildi.");
                            break;
                        }
                        if (name.trim().isEmpty()) {
                            System.out.println("HATA: \u00d6\u011frenci ad\u0131 bo\u015f olamaz!");
                            break;
                        }
                        System.out.print("B\u00f6l\u00fcm: ");
                        String dept = scanner.nextLine();
                        if (dept.trim().isEmpty()) {
                            System.out.println("HATA: B\u00f6l\u00fcm ad\u0131 bo\u015f olamaz!");
                            break;
                        }
                        // Yeni öğrenci nesnesi oluşturup veritabanına ekliyoruz
                        studentRepo.add(new Student(name, dept));
                        break;

                    case 5:
                        // ÖĞRENCİLERİ LİSTELEME İŞLEMİ
                        List<Student> students = studentRepo.getAll();
                        System.out.println("\n--- \u00d6\u011frenci Listesi ---");
                        if (students.isEmpty()) {
                            System.out.println("UYARI: Listelenecek \u00f6\u011frenci bulunmamaktad\u0131r.");
                        } else {
                            // Stream API kullanarak listeyi yazdırıyoruz
                            for (String s : students.stream().map(Object::toString).collect(Collectors.toList())) {
                                System.out.println(s);
                            }
                        }
                        break;

                    case 6:
                        // ÖĞRENCİ SİLME İŞLEMİ
                        System.out.println(
                                "--- \u00d6\u011frenci Silme (\u0130ptal i\u00e7in '0' yaz\u0131n\u0131z) ---");
                        System.out.print("Silinecek \u00d6\u011frenci ID: ");
                        String deleteStudentIdInput = scanner.nextLine();
                        if (deleteStudentIdInput.equals("0")) {
                            System.out.println("\u0130\u015flem iptal edildi.");
                            break;
                        }
                        int deleteStudentId;
                        try {
                            deleteStudentId = Integer.parseInt(deleteStudentIdInput);
                            studentRepo.delete(deleteStudentId);
                        } catch (NumberFormatException e) {
                            System.out.println("HATA: \u00d6\u011frenci ID say\u0131sal olmal\u0131d\u0131r!");
                        }
                        break;

                    case 7:
                        // KİTAP ÖDÜNÇ VERME İŞLEMİ
                        System.out.println(
                                "--- Kitap \u00d6d\u00fcn\u00e7 Verme (\u0130ptal i\u00e7in '0' yaz\u0131n\u0131z) ---");
                        System.out.print("Kitap ID: ");
                        String bookIdInput = scanner.nextLine();
                        if (bookIdInput.equals("0")) {
                            System.out.println("\u0130\u015flem iptal edildi.");
                            break;
                        }
                        int bookId;
                        try {
                            bookId = Integer.parseInt(bookIdInput);
                        } catch (NumberFormatException e) {
                            System.out.println("HATA: Kitap ID say\u0131sal olmal\u0131d\u0131r!");
                            break;
                        }
                        System.out.print("\u00d6\u011frenci ID: ");
                        int studentId;
                        try {
                            studentId = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("HATA: \u00d6\u011frenci ID say\u0131sal olmal\u0131d\u0131r!");
                            break;
                        }
                        System.out.print("Tarih (GG.AA.YYYY): ");
                        String date = scanner.nextLine();
                        if (date.trim().isEmpty()) {
                            System.out.println("HATA: Tarih bo\u015f olamaz!");
                            break;
                        }

                        // Ödünç işlemini başlatmak için Loan nesnesi oluşturup veritabanına ekliyoruz
                        loanRepo.add(new Loan(bookId, studentId, date));
                        break;

                    case 8:
                        // ÖDÜNÇ GEÇMİŞİNİ LİSTELEME
                        List<Loan> loans = loanRepo.getAll();
                        System.out.println("\n--- \u00d6d\u00fcn\u00e7 Ge\u00e7mi\u015fi ---");
                        if (loans.isEmpty()) {
                            System.out.println("UYARI: \u00d6d\u00fcn\u00e7 kayd\u0131 bulunmamaktad\u0131r.");
                        } else {
                            for (Loan l : loans) {
                                System.out.println(l);
                            }
                        }
                        break;

                    case 9:
                        // KİTAP İADE ALMA İŞLEMİ
                        System.out.println("--- Kitap \u0130ade Alma (\u0130ptal i\u00e7in '0' yaz\u0131n\u0131z) ---");
                        System.out.print("Teslim Al\u0131nacak Kitap ID: ");
                        String returnBookIdInput = scanner.nextLine();
                        if (returnBookIdInput.equals("0")) {
                            System.out.println("\u0130\u015flem iptal edildi.");
                            break;
                        }
                        int returnBookId;
                        try {
                            returnBookId = Integer.parseInt(returnBookIdInput);
                        } catch (NumberFormatException e) {
                            System.out.println("HATA: Kitap ID say\u0131sal olmal\u0131d\u0131r!");
                            break;
                        }
                        System.out.print("Teslim Tarihi (GG.AA.YYYY): ");
                        String returnDate = scanner.nextLine();
                        if (returnDate.trim().isEmpty()) {
                            System.out.println("HATA: Tarih bo\u015f olamaz!");
                            break;
                        }

                        // İade işlemini gerçekleştiriyoruz
                        loanRepo.returnBook(returnBookId, returnDate);
                        break;

                    case 0:
                        // ÇIKIŞ işlemimiz
                        System.out.println("Sistem kapat\u0131l\u0131yor...");
                        return;

                    default:
                        System.out.println("Ge\u00e7ersiz se\u00e7im!");
                }

                // İşlem bittikten sonra kullanıcının sonuçları görmesi için bekletme
                if (choice != 0) {
                    System.out
                            .println("\nAna men\u00fcye d\u00f6nmek i\u00e7in Enter tu\u015funa bas\u0131n\u0131z...");
                    scanner.nextLine();
                }
            }
        }
    }

    // Scanner oluşturmak için yardımcı metod
    private static Scanner createScanner() {
        // UTF-8 kodlaması ile Scanner oluşturuyoruz
        try {
            return new Scanner(System.in, "UTF-8");
        } catch (Exception e) {
            return new Scanner(System.in);
        }
    }
}