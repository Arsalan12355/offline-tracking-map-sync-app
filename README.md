# offline-tracking-map-sync-app
# Offline Tracking & Map Sync Application

## Overview

This project is an **offline-first Android tracking application** that records user location continuously, renders the route on a map, supports offline usage, and synchronizes data with a server when the device comes back online.

The main goal of the implementation is **reliability in poor or no-network conditions**, while keeping the architecture clean, scalable, and test-friendly.

---

## Architecture

The app follows **MVVM (Model–View–ViewModel)** architecture:

* **View (Fragment / UI)**

  * Displays the map and route
  * Observes LiveData from ViewModel
  * Handles user actions (Start / Stop / Clear)

* **ViewModel**

  * Holds UI state
  * Exposes `LiveData<List<LocationEntity>>`
  * Communicates with Repository
  * Survives configuration changes

* **Repository**

  * Single source of truth
  * Handles Room DB operations
  * Provides data to ViewModel

* **Data Layer (Room DB)**

  * Stores all tracked locations locally
  * Ensures offline persistence

This separation keeps the UI simple and makes the logic easier to test and maintain.

---

## Offline Strategy

The app is designed with an **offline-first approach**:

* Location tracking continues even when the device is offline
* All location points are saved in **Room Database** immediately
* Route rendering always uses **local DB data**, not network data
* Offline points are marked with `syncStatus = PENDING`

This guarantees that **no data is lost** due to network issues.

---

## Offline Map Support

The app uses **OSMDroid** as the map library.

### Why OSMDroid?

* Open-source
* No Google Maps API key required
* Built-in tile caching
* Strong offline map support

### Offline Map Handling

* Map tiles are cached automatically in local storage
* When the device is offline:

  * Internet tile loading is disabled
  * Cached tiles are used instead
* A limited region (e.g. Karachi) remains usable offline

This allows the user to view their route even without internet access.

---

## Sync Logic

Data synchronization is handled using **WorkManager**:

* Locations are saved locally with `PENDING` sync status
* WorkManager runs only when:

  * Network is available
* Pending locations are uploaded to a **mock server**
* After successful upload:

  * Each location is marked as `SYNCED`

### Key Sync Guarantees

* No duplicate points
* Correct ordering using timestamps
* No missing route segments

WorkManager ensures reliable background sync even if the app is closed.

---

## Map Rendering Logic

* Route is drawn using a **Polyline overlay**
* Polyline points come directly from Room DB
* Data is ordered by timestamp
* When DB is cleared, map overlays are also removed

This ensures that the map always reflects the actual stored data.

---

## Handling 16KB Page Size Consideration

To remain efficient with modern Android memory and paging constraints:

* Room entities are lightweight (primitive data types only)
* Location points are stored individually (no large blobs)
* Database reads are incremental and ordered
* No heavy in-memory caching of route data

This design minimizes memory pressure and works well with Android's paging and memory management model.

---

## Tech Stack

* **Language:** Kotlin
* **Architecture:** MVVM
* **Database:** Room
* **Map:** OSMDroid
* **Location:** FusedLocationProviderClient
* **Background Sync:** WorkManager
* **Networking:** Mock API (Retrofit-style)

---

## Conclusion
This project demonstrates a robust offline-first tracking solution with clean architecture, reliable data persistence, and seamless background synchronization. It is suitable for real-world scenarios where connectivity cannot be guaranteed.

This project demonstrates a robust offline-first tracking solution with clean architecture, reliable data persistence, and seamless background synchronization. It is suitable for real-world scenarios where connectivity cannot be guaranteed.
