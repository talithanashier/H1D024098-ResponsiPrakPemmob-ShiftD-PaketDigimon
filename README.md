Talitha Maharani Nashier_H1D024098_Shift D

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


## 📷 Tampilan Aplikasi

Dark Mode

<img width="776" height="1600" alt="image" src="https://github.com/user-attachments/assets/3e662d9c-e2fc-4b39-a790-4888ed4ef5f4" />
<img width="776" height="1600" alt="image" src="https://github.com/user-attachments/assets/997b27db-42f6-4df7-83ca-720b7d8f26fc" />

Light Mode

<img width="776" height="1600" alt="image" src="https://github.com/user-attachments/assets/bc8860e4-b68f-4d7b-8ca8-eb54ae8ed0ac" />
<img width="776" height="1600" alt="image" src="https://github.com/user-attachments/assets/9109b509-2169-4bc2-a293-d4d54e0c1d7f" />




