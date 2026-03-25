# LocationAppBasics

A robust Android application demonstrating the integration of **Google Play Services Location API** to fetch and display real-time geographic data.

## 🚀 Features
* **Real-time Tracking:** Utilizes `FusedLocationProviderClient` for high-accuracy location data.
* **Reactive UI:** Built with **Jetpack Compose**, featuring lifecycle-aware state management via **ViewModel**.
* **Permission Handling:** Implements dynamic runtime permissions for `ACCESS_FINE_LOCATION` and `ACCESS_COARSE_LOCATION`.
* **Reverse Geocoding:** Integration to convert latitude and longitude coordinates into human-readable addresses.

## 🛠 Tech Stack
* **Language:** [Kotlin](https://kotlinlang.org/)
* **UI Framework:** Jetpack Compose
* **Architecture:** MVVM (Model-View-ViewModel)
* **API:** Google Play Services (Location)

## 📖 What I Learned
Through this project, I mastered the Android lifecycle, specifically how to handle location updates without draining the device battery. I also gained experience in modern declarative UI patterns using Compose.

## 📥 Installation
1. Clone the repository: `git clone https://github.com/bhavyadesai12/LocationAppBasics.git`
2. Open the project in **Android Studio**.
3. Build and run on an emulator or physical device with Location Services enabled.
