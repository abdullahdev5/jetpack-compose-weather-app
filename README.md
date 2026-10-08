# 🌤️ Reactive Weather & Environmental Metrics App — Jetpack Compose

[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Retrofit](https://img.shields.io/badge/Retrofit-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://square.github.io/retrofit/)
[![Android SDK](https://img.shields.io/badge/Android%20SDK-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)

A modern, native Android weather application built with **100% declarative UI in Jetpack Compose** and asynchronous REST API networking powered by **Retrofit**. Designed to deliver hyper-local weather tracking, extended 14-day forecasts, air quality index breakdown, and location search with clean state management.

---

## ✨ Key Features Breakdown

* **📍 Dual Location Architecture (GPS & Global Search):**
  * **Device GPS Tracking (`getCurrentLocation`):** Fetches the user's real-time physical position via Fused Location Provider to deliver hyper-local weather conditions automatically.
  * **Global City Search:** Built-in location search engine with quick-select shortcuts (Tokyo, London, New York, Paris, Islamabad, etc.) to fetch weather data for any global city on demand.
* **🌡️ Real-Time Environmental Metrics:** Displays current temperature, "feels like" heat index, daily high/low spans, humidity levels, and dynamic weather condition states.
* **⏱️ Multi-Tiered Forecast Engines:**
  * **Hourly Projections:** Scrollable 72-hour timeline tracking granular temperature shifts.
  * **Extended Daily Forecasts:** Integrated short-term (3-day) and extended (14-day) predictive outlooks.
* **🍃 Air Quality Index (AQI) Analysis:** Real-time atmospheric quality breakdown analyzing key air pollutants (PM2.5, CO, NO2, SO2, O3).
* **☀️ Atmospheric & Day/Night Metrics:** Tracks UV index exposure, wind speed/gusts, cloud coverage percentage, precipitation probability, sunrise/sunset cycles, and moon phases.

---

## 🛠 Tech Stack & Architecture

* **Language:** Kotlin
* **UI Framework:** Jetpack Compose (Material Design 3)
* **Networking:** Retrofit 2 + Gson Converter
* **Architecture:** MVVM (Model-View-ViewModel) + Clean Architecture
* **Asynchronous Processing:** Kotlin Coroutines & StateFlow / LiveData
* **Local Caching / Security:** `local.properties` configuration for API keys

---

## 📷 Application Screenshots

<table>
  <tr>
    <td align="center"><img src="https://github.com/user-attachments/assets/4fe5118a-ef83-4e3a-9f97-366e032d8d57" width="220" alt="Main Dashboard" /></td>
    <td align="center"><img src="https://github.com/user-attachments/assets/4fafadf4-c9b7-455b-b365-2721fa64fdbf" width="220" alt="AQI & Map Details" /></td>
    <td align="center"><img src="https://github.com/user-attachments/assets/d1565db8-6a66-4a10-a9ea-3688975815ae" width="220" alt="14-Day Forecast" /></td>
    <td align="center"><img src="https://github.com/user-attachments/assets/6565ecc1-0ffb-4bb9-9170-e39134b6637b" width="220" alt="Day Night Details" /></td>
  </tr>
  <tr>
    <td align="center"><img src="https://github.com/user-attachments/assets/98afca86-eaf3-4b18-b089-03840508d1d3" width="220" alt="City Search" /></td>
    <td align="center"><img src="https://github.com/user-attachments/assets/aa784a4f-0b5b-40e2-ab0d-416ba135c4b0" width="220" alt="Hourly View" /></td>
    <td align="center"><img src="https://github.com/user-attachments/assets/2bfc77e8-a948-4051-8e8c-178de0daa0c0" width="220" alt="Location Forecast" /></td>
    <td></td>
  </tr>
</table>

---

## 🚀 Getting Started

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/abdullahdev5/jetpack-compose-weather-app.git](https://github.com/abdullahdev5/jetpack-compose-weather-app.git)
