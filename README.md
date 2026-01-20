# Walk Munich – Android

Walk Munich is an Android app I’m building to create and organize walking routes in Munich.  
The main idea is to make it easier to plan small itineraries for friends, family, and visitors when showing them around the city.

The app helps to find interesting places, group them into routes, and display them in a simple, map-based way — so it’s easier to explore Munich on foot.

It’s still **a work in progress**, and I’m gradually improving it as I learn and experiment with Android architecture, navigation, and data handling.

---

## 📍 Overview

**Walk Munich** started as a small side project to make personal sightseeing plans easier.  
Instead of keeping notes and maps separately, the goal is to have one app where I can:

- List places worth visiting
- Connect them into walking routes
- Add short stories and highlights related to each place

I’m using **Kotlin** and **modern Android tools** to keep the codebase clean and maintainable while learning new concepts along the way.

---

## ⚙️ Current Features

- Categories
- Add search and filtering by area or theme  
- See specially created itineraries  
- Add detailed view for each stop (photos, short stories, or history)
- Favorites
- Map support
- Improve UI/UX for smoother navigation

---

## 🚧 Planned Features

- Create and save custom walking routes
  
---

## 🧱 Tech Stack

- **Language:** Kotlin  
- **UI:** Jetpack Compose  
- **Navigation:** Jetpack Navigation (v3)  
- **Architecture:** Feature-based modular structure  

---

## 🗺️ Google Maps Setup

This project uses **Google Maps** to display places on an interactive map.  
To run the app locally, you must provide your own **Google Maps API key**.

### Create a Google Maps API key

1. Go to the **Google Cloud Console**
2. Create or select a project
3. Enable **Maps SDK for Android**
4. Create an **API key**
5. Restrict the key:
    - **Application restriction:** Android apps
    - Add the app’s **package name**
    - Add your **SHA-1 fingerprint** (debug SHA-1 is sufficient for development)

You can obtain the debug SHA-1 by running:

```bash
./gradlew signingReport
```
---

## 📸 Preview

Here are a few early design and app screens:

| Home Screen                    | Itineraries                                | Itineraries Detail                                                                                                  | Place Details                       |
|--------------------------------|--------------------------------------------|--------------------------------------------|-------------------------------------|
| ![Home](/screenshots/home.png) | ![Itineraries](/screenshots/itinerary.png) | ![Itineraries Detail](/screenshots/itinerary_detail1.png) | ![Details](/screenshots/detail.png) |

These screens show the current direction of the app and may change as development continues.

---

## ⚠️ Disclaimer

This project is developed for **educational and personal learning purposes** only.  
It is not an official travel or navigation app and does not guarantee the accuracy of location or historical information provided.  
All content and media used within the app are intended for non-commercial use as part of an ongoing learning project.

## 📄 License

This project is licensed under the [MIT License](LICENSE).  
You are free to use, modify, and distribute this project for learning or personal purposes, as long as proper credit is given.


