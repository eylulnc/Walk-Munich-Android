# Walk Munich – Android

Walk Munich is an Android app for discovering and exploring Munich.
It helps you find interesting places, browse curated walking routes, and navigate between stops — all without needing an account or API key.

It's still **a work in progress**, built as a personal project to learn and experiment with modern Android architecture, navigation, and UI patterns.

---

## 📍 Overview

Instead of juggling notes, maps, and screenshots, the goal is one app where you can:

- Discover places worth visiting, filtered by category
- Browse and follow curated walking routes
- View place details with descriptions and photos
- Save favorites and revisit recently viewed places
- See all places on an interactive map with category markers
- Navigate directly to any place via Google Maps

---

## ⚙️ Current Features

- **Explore** — Overview of highlights, recently viewed places, and category browsing
- **Place Detail** — Photo, description, highlights, and map coordinates for each stop
- **Routes** — Browse curated walking itineraries with a full route detail view
- **Map** — Interactive OpenStreetMap with color-coded category markers; tap a pin to preview a place and open walking directions in Google Maps
- **Favorites** — Save and manage favorite places with a list/grid toggle
- **Settings** — App preferences and About section

---

## 🧱 Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Navigation:** Navigation3 (`androidx.navigation3`)
- **Architecture:** MVVM, feature-based modular structure
- **Dependency Injection:** Koin
- **State Management:** `StateFlow` + `collectAsStateWithLifecycle`
- **Persistence:** DataStore Preferences
- **Maps:** OSMDroid (OpenStreetMap — no API key required)
- **Serialization:** Kotlinx Serialization

---

## 📸 Preview

| Explore | Search | Place Detail |Tour List | Tour Detail |
|---------|--------|--------------|-----------|-------------|
| ![Explore](/screenshots/explore.png) | ![Search](/screenshots/search.png) | ![Place Detail](/screenshots/detail.png) | ![Tours](/screenshots/tour.png) | ![Tour Detail](/screenshots/tour_detai.png) |

| Map | Map Detail | Favorites | Favorites Grid | Settings |
|-----|------------|-----------|----------------|----------|
| ![Map](/screenshots/map1.png) | ![Map Detail](/screenshots/map2.png) | ![Favorites](/screenshots/favorite1.png) | ![Favorites Grid](/screenshots/favorite2.png) | ![Settings](/screenshots/settings.png) |

---

## 🚧 Planned

- Custom route creation and saving
- Search routes

---

## ⚠️ Disclaimer

This project is developed for **educational and personal learning purposes** only.
It is not an official travel or navigation app and does not guarantee the accuracy of location or historical information.
All content and media are intended for non-commercial use as part of an ongoing learning project.

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).
You are free to use, modify, and distribute this project for learning or personal purposes, as long as proper credit is given.
