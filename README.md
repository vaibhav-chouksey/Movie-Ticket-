# 🎟️ Ticket - Smart Movie Booking App

![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-purple?style=for-the-badge&logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material3-blue?style=for-the-badge&logo=android)
![Hilt](https://img.shields.io/badge/DI-Hilt-orange?style=for-the-badge)
![Razorpay](https://img.shields.io/badge/Payment-Razorpay-blueviolet?style=for-the-badge)

**Ticket** is a cutting-edge, native Android application built to demonstrate modern mobile architecture. It features a seamless flow from movie discovery to seat selection and payment processing, utilizing the latest **Jetpack Compose** UI toolkit and **MVVM Clean Architecture**.

---

## 📱 App Preview

| Home & Discovery | Smart Search | Seat Selection | Ticket Confirmation |
|:---:|:---:|:---:|:---:|
| <img src="docs/home.png" width="200"/> | <img src="docs/search.png" width="200"/> | <img src="docs/seats.png" width="200"/> | <img src="docs/success.png" width="200"/> |

> *Experience a fluid UI with glassmorphism effects and smooth transitions.*

---

## ✨ Key Features

### 1. 🎬 **Immersive Movie Discovery**
* **Live Data:** Fetches "Now Playing" and "Upcoming" movies using the **TMDB API**.
* **Rich Details:** High-resolution posters, ratings, and synopsis in a clean Material3 layout.
* **Horizontal Genre Filter:** Quickly toggle between *Action, Comedy, Romance,* and more using reactive "Pill" tabs in the Search screen.

### 2. 💺 **Interactive Seat Booking System**
* **Custom Canvas Drawing:** The cinema screen curve and seat layout are drawn natively using Compose Canvas (no static images).
* **Smart State Management:**
    * **Available:** White (Clickable)
    * **Selected:** Primary Blue (updates price dynamically)
    * **Booked:** Grey (Disabled/Blocked)
* **Dynamic Pricing:** Real-time calculation of total cost based on the number of selected seats.

### 3. 💳 **Secure Payment Integration**
* **Razorpay Gateway:** Integrated Razorpay SDK to handle secure transactions.
* **Payment Simulation:** Robust handling of payment success and failure callbacks.
* **Architecture Flow:** Payments are decoupled from UI logic using ViewModels and StateFlow.

### 4. 🎟️ **Digital Ticketing**
* **Navigation Logic:** Intelligent back-stack handling ensures users cannot accidentally return to the booking screen after payment.

---

## 🛠️ Tech Stack

* **Language:** [Kotlin](https://kotlinlang.org/) (100%)
* **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material Design 3)
* **Architecture:** MVVM (Model-View-ViewModel)
* **Dependency Injection:** [Dagger Hilt](https://dagger.dev/hilt/)
* **Network:** [Retrofit2](https://square.github.io/retrofit/) & [OkHttp3](https://square.github.io/okhttp/)
* **Image Loading:** [Coil](https://coil-kt.github.io/coil/)
* **Async Programming:** Kotlin Coroutines & Flow
* **Navigation:** Jetpack Navigation Compose
* **Payment:** Razorpay Android SDK

---

## 🏗️ Architecture & Best Practices

The app follows the **Single Activity Architecture** and adheres to **SOLID principles**.

```text
com.example.ticket
├── di             # Hilt Modules (Network, Repository Injection)
├── model          # Data Classes (Movie, Seat, User)
├── navigation     # Centralized Navigation Graph (Routes & Arguments)
├── network        # API Service Interfaces & Interceptors
├── repository     # Data Layer (Mediates between API and ViewModel)
├── view
│   ├── component  # Reusable UI (MovieCard, SeatComposable, SearchBar)
│   └── screen     # Feature Screens (Auth, Home, Detail, Booking)
└── viewmodel      # State Holders (Business Logic & UI State)

