# 📱 Digimon Explorer - Responsi Praktikum Pemrograman Mobile

Aplikasi Android modern untuk menjelajahi dan mengidentifikasi Digimon di Dunia Digital, dibangun menggunakan **100% Jetpack Compose (NO XML)**, arsitektur murni **MVVM (Model-View-ViewModel + Repository)**, serta **Material Design 3**.

---

## 📋 Daftar Isi
1. [Deskripsi Proyek](#-deskripsi-proyek)
2. [Tampilan Antarmuka (Screenshot Aplikasi)](#-tampilan-antarmuka-screenshot-aplikasi)
3. [Fitur Utama](#-fitur-utama)
4. [Tech Stack & Pustaka](#-tech-stack--pustaka)
5. [Arsitektur Proyek (MVVM)](#-arsitektur-proyek-mvvm)
6. [Struktur Folder](#-struktur-folder)
7. [Penanganan State (UI State)](#-penanganan-state-ui-state)
8. [Cara Menjalankan Proyek](#-cara-menjalankan-proyek)
9. [Panduan Penjelasan Kode untuk Video Responsi](#-panduan-penjelasan-kode-untuk-video-responsi)

---

## 📖 Deskripsi Proyek
Dunia digital sedang dilanda kekacauan akibat ulah tamer jahat. Aplikasi **Digimon Explorer** dibuat untuk membantu para tamer mengidentifikasi digimon musuh dengan menampilkan informasi mendalam mengenai nama, level, atribut, tipe, hingga jurus/kemampuan yang bersumber langsung dari **Digi-API (DAPI)** publik.

---

## 📸 Tampilan Antarmuka (Screenshot Aplikasi)

| 1. Home Screen (Data Grid) | 2. Detail Screen (Informasi Karakter) |
| :---: | :---: |
| ![Home Screen](screenshots/home_screen.png) | ![Detail Screen](screenshots/detail_screen.png) |

| 3. Loading State | 4. Error State & Retry Button |
| :---: | :---: |
| ![Loading State](screenshots/loading_state.png) | ![Error State](screenshots/error_state.png) |

> 💡 **Petunjuk Pengisian Screenshot:**
> Ambil tangkapan layar dari emulator/perangkat fisik Anda, simpan dengan format nama file di atas ke dalam folder [`screenshots/`](screenshots/) di root repositori ini.

---

## ✨ Fitur Utama
* **100% Jetpack Compose & Material 3**: UI modern, responsif, tanpa satu pun layout XML.
* **Integrasi Digi-API (DAPI)**: Mengambil data real-time dari endpoint RESTful `https://digi-api.com/api/v1/`.
* **Home Screen**:
  * Menampilkan grid daftar Digimon menggunakan `LazyVerticalGrid`.
  * Setiap kartu menampilkan: Nama, Level, Atribut (dengan badge warna dinamis), Tipe, dan Gambar karakter.
  * Penanganan UI State lengkap: **Loading Indicator**, **Error View (dengan tombol Coba Lagi/Retry)**, dan **Data Content**.
* **Detail Screen**:
  * Menampilkan informasi terperinci Digimon: Avatar gambar resolusi tinggi, ID Digimon, Level, Attribute, Type, Tahun Debut, Deskripsi ensiklopedia, serta daftar Jurus/Kemampuan (*Skills*).
  * Dilengkapi tombol navigasi kembali ke Home Screen.
* **Optimasi Performa**:
  * Pengambilan data detail paralel menggunakan Kotlin Coroutines (`async/awaitAll`).
  * In-memory caching di layer repository untuk pengalaman navigasi yang instan tanpa loading berulang.
* **Custom Theme & Typography**: Skema warna cyber-digital terinspirasi Digivice dengan kontras Material 3 dan tipografi yang terstruktur.

---

## 🛠 Tech Stack & Pustaka
* **Bahasa**: Kotlin (memanfaatkan *Data Classes*, *Null Safety*, *Coroutines*, *Flow*, *Lambdas*, & *Collections*).
* **UI Toolkit**: Jetpack Compose (BOM 2024.04.01) + Material Design 3.
* **Arsitektur**: MVVM (Model - View - ViewModel) + Repository Pattern.
* **Networking**:
  * Retrofit 2.11.0 + Gson Converter
  * OkHttp 4.12.0 + Logging Interceptor
* **Image Loading**: Coil Compose 2.7.0 (asynchronous image loader dengan crossfade).
* **Navigasi**: Jetpack Compose Navigation 2.8.5.

---

## 🏛 Arsitektur Proyek (MVVM)

```
       ┌────────────────┐
       │   Digi-API     │
       └───────▲────────┘
               │ HTTP / JSON
       ┌───────▼────────┐
       │ DigiApiService │ (Retrofit Client)
       └───────▲────────┘
               │ DTO & Domain Model
       ┌───────▼──────────────┐
       │ DigimonRepository    │ (Data Layer & In-Memory Cache)
       └───────▲──────────────┘
               │ Coroutine StateFlow
       ┌───────▼──────────────┐
       │ Home/Detail ViewModel│ (State Holder & Business Logic)
       └───────▲──────────────┘
               │ Observe State (Loading / Success / Error)
       ┌───────▼──────────────┐
       │ Jetpack Compose View │ (Home & Detail Screen UI)
       └──────────────────────┘
```

1. **Model (`data/model/`)**:
   * DTO (*Data Transfer Object*) untuk deserialisasi JSON dari Digi-API.
   * Domain Model `Digimon` yang bersih dan siap dipakai UI.
2. **Repository (`data/repository/`)**:
   * Abstraksi sumber data melalui `DigimonRepository`.
   * Menangani pengambilan data list sekaligus mengisi Level, Attribute, Type secara concurrent.
3. **ViewModel (`ui/screens/**/`)**:
   * Mengelola lifecycle dan mengekspos `StateFlow<UiState>` ke Composable.
4. **View (`ui/screens/**/`)**:
   * Fungsi Composable deklaratif yang merefleksikan state UI secara reaktif.

---

## 📁 Struktur Folder

```text
com.responsi.digimonexplorer/
│
├── MainActivity.kt                       # Entry point aplikasi (Edge-to-edge & Theme host)
│
├── data/
│   ├── api/
│   │   ├── ApiClient.kt                 # Inisialisasi Retrofit & OkHttpClient singleton
│   │   └── DigiApiService.kt            # Definisi endpoint RESTful Digi-API
│   ├── model/
│   │   └── DigimonModels.kt             # DTO JSON & Domain Model Digimon + Mapper
│   └── repository/
│       └── DigimonRepository.kt         # Interface & Implementasi Repository
│
└── ui/
    ├── navigation/
    │   ├── Screen.kt                    # Sealed class rute navigasi (Home & Detail)
    │   └── NavGraph.kt                  # NavHost pengaturan alur 2 layar
    ├── screens/
    │   ├── home/
    │   │   ├── HomeUiState.kt           # Sealed interface state (Loading, Success, Error)
    │   │   ├── HomeViewModel.kt         # ViewModel beranda & pemanggil repository
    │   │   └── HomeScreen.kt            # Tampilan grid Digimon, card item, loading & retry
    │   └── detail/
    │       ├── DetailUiState.kt         # Sealed interface state detail
    │       ├── DetailViewModel.kt       # ViewModel detail & retry logic
    │       └── DetailScreen.kt          # Tampilan detail Digimon & tombol kembali
    └── theme/
        ├── Color.kt                     # Skema warna Material 3 & badge atribut
        ├── Theme.kt                     # Konfigurasi Tema DigimonTheme
        └── Type.kt                      # Konfigurasi tipografi custom
```

---

## 🔄 Penanganan State (UI State)

Aplikasi menerapkan pola **Unidirectional Data Flow (UDF)** dengan 3 kondisi state utama:

```kotlin
sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val digimons: List<Digimon>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
```

* **Loading**: Menampilkan `CircularProgressIndicator` saat data sedang diunduh dari jaringan.
* **Success**: Menampilkan `LazyVerticalGrid` dengan daftar kartu Digimon yang interaktif.
* **Error**: Menampilkan ikon peringatan, pesan deskriptif kendala, serta tombol `Coba Lagi` (*Retry*) yang memicu pemuatan ulang data tanpa perlu membuka kembali aplikasi.

---

## 🚀 Cara Menjalankan Proyek

1. **Clone Repository**:
   ```bash
   git clone <URL_REPOSITORY_ANDA>
   cd ResponsiPraktikumPemmob_ShiftD
   ```
2. **Buka di Android Studio**:
   * Pilih menu **File > Open** lalu arahkan ke folder proyek ini.
   * Pastikan Gradle Sync berjalan sukses (menggunakan JDK 17 atau 21).
3. **Jalankan Aplikasi**:
   * Hubungkan perangkat Android fisik (via USB debugging) atau jalankan Android Emulator (API 26+).
   * Klik tombol **Run 'app'** (`Shift + F10`).
4. **Membangun APK**:
   ```bash
   ./gradlew assembleDebug
   ```
   File APK hasil build berada di folder:
   `app/build/outputs/apk/debug/app-debug.apk`

---

## 🎙 Panduan Penjelasan Kode untuk Video Responsi

Gunakan panduan berikut sebagai acuan berbicara saat merekam video penjelasan kode:

1. **Pembukaan**:
   * Perkenalkan diri (Nama, NIM, Shift Praktikum).
   * Jelaskan tujuan aplikasi: Aplikasi Eksplorasi Digimon yang mengambil data dari Digi-API menggunakan Jetpack Compose dan arsitektur MVVM murni.
2. **Layer Data (`data/`)**:
   * Buka [DigimonModels.kt](file:///d:/ResponsiPraktikumPemmob_ShiftD/app/src/main/java/com/responsi/digimonexplorer/data/model/DigimonModels.kt): Jelaskan penggunaan *data class* untuk memetakan respons JSON dari Digi-API (`content`, `levels`, `attributes`, `types`) dan fungsi mapper `toDomainModel()`.
   * Buka [DigiApiService.kt](file:///d:/ResponsiPraktikumPemmob_ShiftD/app/src/main/java/com/responsi/digimonexplorer/data/api/DigiApiService.kt) & [ApiClient.kt](file:///d:/ResponsiPraktikumPemmob_ShiftD/app/src/main/java/com/responsi/digimonexplorer/data/api/ApiClient.kt): Jelaskan integrasi Retrofit dengan base URL `https://digi-api.com/api/v1/`.
   * Buka [DigimonRepository.kt](file:///d:/ResponsiPraktikumPemmob_ShiftD/app/src/main/java/com/responsi/digimonexplorer/data/repository/DigimonRepository.kt): Jelaskan bagaimana data list diambil dan digabungkan secara paralel dengan Coroutine agar setiap kartu memiliki Level, Attribute, dan Type lengkap.
3. **Layer ViewModel & State (`ui/screens/`)**:
   * Buka [HomeUiState.kt](file:///d:/ResponsiPraktikumPemmob_ShiftD/app/src/main/java/com/responsi/digimonexplorer/ui/screens/home/HomeUiState.kt): Jelaskan 3 state wajib (Loading, Success, Error).
   * Buka [HomeViewModel.kt](file:///d:/ResponsiPraktikumPemmob_ShiftD/app/src/main/java/com/responsi/digimonexplorer/ui/screens/home/HomeViewModel.kt): Jelaskan pemanfaatan `StateFlow` dan fungsi `loadDigimons()` yang memperbarui UI state secara asinkron.
4. **Layer UI (Compose View & Navigasi)**:
   * Buka [HomeScreen.kt](file:///d:/ResponsiPraktikumPemmob_ShiftD/app/src/main/java/com/responsi/digimonexplorer/ui/screens/home/HomeScreen.kt): Tunjukkan komponen `LazyVerticalGrid`, penanganan `when (state)`, serta kartu `DigimonCardItem` yang memuat nama, level, atribut, tipe, dan gambar (Coil).
   * Buka [DetailScreen.kt](file:///d:/ResponsiPraktikumPemmob_ShiftD/app/src/main/java/com/responsi/digimonexplorer/ui/screens/detail/DetailScreen.kt): Tunjukkan tampilan detail dan tombol kembali (`IconButton` di `TopAppBar`).
   * Buka [NavGraph.kt](file:///d:/ResponsiPraktikumPemmob_ShiftD/app/src/main/java/com/responsi/digimonexplorer/ui/navigation/NavGraph.kt): Jelaskan bagaimana Jetpack Compose Navigation mengarahkan perpindahan antara Home Screen dan Detail Screen dengan melewatkan argumen `digimonId`.
5. **Tema & Desain (`ui/theme/`)**:
   * Tunjukkan [Theme.kt](file:///d:/ResponsiPraktikumPemmob_ShiftD/app/src/main/java/com/responsi/digimonexplorer/ui/theme/Theme.kt), [Color.kt](file:///d:/ResponsiPraktikumPemmob_ShiftD/app/src/main/java/com/responsi/digimonexplorer/ui/theme/Color.kt), dan [Type.kt](file:///d:/ResponsiPraktikumPemmob_ShiftD/app/src/main/java/com/responsi/digimonexplorer/ui/theme/Type.kt) yang membuktikan penerapan Custom Theme & Custom Typography Material 3 tanpa XML sama sekali.
6. **Penutup**:
   * Ringkas bahwa seluruh persyaratan teknis (Kotlin modern, Jetpack Compose, MVVM murni, Retrofit Digi-API, State Management 3 kondisi, 2 screens navigation) telah terpenuhi dan terkompilasi dengan sukses.
 

## Tampilan Aplikasi

Dark Mode

<img width="776" height="1600" alt="image" src="https://github.com/user-attachments/assets/3e662d9c-e2fc-4b39-a790-4888ed4ef5f4" />
<img width="776" height="1600" alt="image" src="https://github.com/user-attachments/assets/997b27db-42f6-4df7-83ca-720b7d8f26fc" />

Light Mode

<img width="776" height="1600" alt="image" src="https://github.com/user-attachments/assets/bc8860e4-b68f-4d7b-8ca8-eb54ae8ed0ac" />
<img width="776" height="1600" alt="image" src="https://github.com/user-attachments/assets/9109b509-2169-4bc2-a293-d4d54e0c1d7f" />




