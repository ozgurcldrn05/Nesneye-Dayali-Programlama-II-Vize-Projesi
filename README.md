SmartLibrary — Kütüphane Yönetim Sistemi

SmartLibrary, “Nesne-Dayalı Programlama II” dersi kapsamında geliştirilmiş; Java + SQLite + JDBC ile çalışan bir masaüstü (konsol) uygulamasıdır.

🚀 Projenin Amacı

Üniversite kütüphanesi için kitap, öğrenci ve ödünç alma işlemlerini dijital ortamda yönetmek.

Kitap kayıtları, öğrenci kayıtları, ödünç alma / iade süreçleri gibi temel kütüphane iş akışlarını simüle etmek.

Kalıcılığı sağlamak için veritabanı (SQLite) kullanarak verilerin uygulama kapanıp açılsa bile saklanmasını temin etmek.

💻 Teknolojiler & Araçlar

Programlama Dili: Java (JDK 17, Java 8 uyumu) 
GitHub

Veritabanı: SQLite (via JDBC) 
GitHub

IDE / Kod Ortamı: Visual Studio Code önerilir 
GitHub

Mimari: Katmanlı yapı — Entity sınıfları, Repository (veritabanı erişimi), veri modelleri ve uygulama mantığı ayrıştırılmış biçimde. 
GitHub

📦 Uygulama Özellikleri

Book, Student, Loan gibi sınıflar aracılığıyla kitap, öğrenci ve ödünç alma işlemlerinin temsil edilmesi. 
GitHub

Encapsulation — sınıf içi değişkenler private, erişim getter/setter ile. 
GitHub

Repository desenine uygun, veritabanı işlemlerinin ayrı sınıflarda toplanması (Ekleme, Silme, Listeleme vb.) 
GitHub

SQLite ile kalıcılık: Tüm veriler bir .db dosyasında saklanıyor. Uygulama başlangıcında veritabanı ve tablolar otomatik oluşturuluyor (var değilse). 
GitHub

Koleksiyon kullanımı: Örneğin kitap / öğrenci listeleri için ArrayList. 
GitHub

Temel mantık kontrolleri: Ödünç verilmiş bir kitap, bir başkasına verilemiyor — iade yapılmadan ikinci kez ödünç verilmez. 
GitHub

🏗️ Proje Yapısı
SmartLibrary/
│
├── smartlibrary.db            # SQLite veritabanı dosyası (uygulama çalışınca oluşturulur)
├── sqlite-jdbc-<…>.jar        # Veritabanı sürücüsü
│
├── Main.java                  # Uygulama giriş noktası / menü sistemi
├── Database.java              # SQLite bağlantısı ve tablo oluşturma işlemleri
├── Book.java                  # Kitap nesnesi (Entity)
├── Student.java               # Öğrenci nesnesi (Entity)
├── Loan.java                  # Ödünç alma işlemi nesnesi (Entity)
│
├── BookRepository.java        # Kitap ekleme / silme / listeleme işlemleri
├── StudentRepository.java     # Öğrenci ekleme / silme / listeleme işlemleri
└── LoanRepository.java        # Ödünç alma / iade / listeleme işlemleri


GitHub

✅ Çalıştırma — Adım Adım

Bilgisayarınızda Java (JDK) yüklü olmalı.

Projeyi klonlayın / indirin, bir kod editöründe (örneğin Visual Studio Code) açın. 
GitHub

SQLite JDBC sürücüsünü (.jar dosyası) proje kütüphanelerine referans olarak ekleyin. 
GitHub

Main.java dosyasını çalıştırın. Konsol menüsüyle uygulama başlar. 
GitHub

📚 Kullanım Senaryoları / Menü İşlemleri

Kitap Ekle — Yeni kitap kaydı. 
GitHub

Kitapları Listele — Tüm kitapların görüntülenmesi. 
GitHub

Öğrenci Ekle — Yeni öğrenci kaydı. 
GitHub

Öğrencileri Listele — Sistemdeki tüm öğrenciler. 
GitHub

Kitap Ödünç Ver — Bir kitap, bir öğrenciye ödünç olarak verilir. Eğer kitap başkasındaysa uyarı. 
GitHub

Ödünç Listeyi Görüntüle — Hangi kitap kimin, ne zaman ödünç almış — iade durumu. 
GitHub

Kitap Geri Teslim Al — İade işlemi, iade tarihi kaydedilir. 
GitHub

Kitap Sil — Sistemdeki bir kitabı siler. 
GitHub

Öğrenci Sil — Sistemdeki bir öğrenciyi siler. 
GitHub

🎯 Kime / Ne İçin Uygun?

Nesne-Yönelimli Programlama (OOP) dersi kapsamında öğrenci projeleri için.

Basit bir kütüphane yönetim sistemi örneği görmek isteyenler için — CRUD (Create-Read-Update-Delete) + veritabanı + OOP + Repository desenine giriş.

Konsol uygulamaları ve SQLite + JDBC ile çalışmaya başlamak isteyenler için güzel bir temel.

📝 Geliştirme / Geliştirilebilir Özellikler (Opsiyonel)

Konsol yerine grafik arayüzü ile yeniden yazmak (GUI — Swing / JavaFX).

“Rezervasyon”, “geç teslim cezası”, “kitap kategorisi / raf yönetimi” gibi detaylı kütüphane işlemleri eklemek.

Çok kullanıcılı / yetkili kullanıcı sistemi — admin / öğrenci / personel gibi roller.

Veri yedekleme / dışa aktarma (örneğin CSV / JSON) özelliği.

Hata / veri doğrulama kontrollerinin artırılması, kullanıcı dostu mesajlar ve menü iyileştirmeleri.

👤 Proje Hakkında

Geliştirici: Ömer Özgür Çaldıran

GitHub: https://github.com/ozgurcldrn05
