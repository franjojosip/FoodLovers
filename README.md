<p align="center">
  <img src="https://github.com/user-attachments/assets/9770994f-2b0a-48c0-9a46-86d9b60d771e" alt="FoodLovers Banner" width="100%">
</p>

<br>

# 🍽️ **FoodLovers – Recipe & Cooking App built with Kotlin**
Your personalized recipe companion — offline cooking, favorites, custom recipes, and a full user profile experience.

![Android](https://img.shields.io/badge/Android-Kotlin-green)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-blue)
![DI-Koin](https://img.shields.io/badge/DI-Koin-purple)
![Room](https://img.shields.io/badge/Database-Room-orange)
![Firebase](https://img.shields.io/badge/Firebase-Integrated-yellow)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

---

## ✨ **Overview**

FoodLovers is a modern, modular Kotlin mobile application designed for anyone who loves cooking, discovering recipes, and customizing their culinary experience.

The app supports **full offline mode** — once recipes are downloaded, everything is stored locally in a **Room database**.  
You can search, save favorites, create your own recipes, edit them, and personalize your profile.

This project demonstrates clean architecture, modular design, dependency injection using **Koin**, and best practices for real-world Android development.

---

## 🍳 **Features**

### 📥 **Offline Mode**
- Local Room database  
- Recipes available without internet  
- Automatic caching after first use  

### 🔍 **Recipe Search**
- Search by name, ingredients, category  
- Fast & responsive UI  
- Works offline once cached  

### ❤️ **Favorites**
- Add/remove favorite recipes  
- Persisted locally  
- Synced with user profile  

### ✏️ **Add & Edit Recipes**
- Create your own recipes  
- Edit existing ones  
- Store privately or publicly  

### 👤 **User Profile**
- Customizable user account  
- Avatar, name, preferences  
- Profile stored locally + Firebase Storage support  

### 🌎 **Google Maps Integration**
- Locate recipes from specific regions  
- Map preview inside the app  

### 🌐 **Localization**
- Multi-language ready  

### 🔔 **Notifications**
- Recipe reminders  
- Personalized suggestions  

---

## 🛠 **Tech Stack**

| Layer | Technology |
|-------|------------|
| Architecture | MVVM |
| DI | Koin |
| Database | Room |
| Storage | Firebase Storage |
| Crash Reports | Firebase Crashlytics |
| Analytics | Firebase Analytics |
| Networking | Retrofit |
| Concurrency | Coroutines |
| Navigation | Jetpack Navigation |
| UI | Material Design 3 |
| Maps | Google Maps SDK |
| State | LiveData + ViewModel |
| Android Features | Notifications, Edge-to-Edge |

---

## 🧩 **Module Structure**

```
FoodLovers/
├── main/          → Prelogin + Main Activity
├── common/        → Database, API, repository, shared views
├── auth/          → Login / Register flows
├── account/       → User profile management
└── recipe/        → Recipe list, details, search, edit/create
```

---

## 🚀 **Getting Started**

### 1️⃣ **Download the project**
```bash
git clone https://github.com/franjojosip/FoodLovers.git
```

### 2️⃣ Open in Android Studio  
Use Android Studio **Arctic Fox or newer**.

### 3️⃣ Rename the package
- Update Manifest package:  
  `ht.ferit.fjjukic.foodlovers`
- Update `applicationId` in **app/build.gradle**
- Ensure refactor applies to all modules

### 4️⃣ Configure app resources
- Set app name in `strings.xml`
- Insert your **Google Maps API key**
- Insert **Notification Channel ID**
- Set theme in **Theme.kt** & **Color.kt**

### 5️⃣ Replace icons  
Add your launcher icons to `/mipmap`.

### 6️⃣ Check dependencies  
```bash
./gradlew dependencyUpdates
```

### 7️⃣ Run the app 🎉

---

## 🔑 **Important Notes**
- Registration is required (Auth module)  
- To use **profile location mode**, you must insert your own **Google Maps API key**  
- Firebase features require your Firebase project  

---

## ❤️ **About the Developer**

Hi! I'm **Franjo**, an Android developer passionate about:

- Scalable mobile architecture  
- Material Design UI  
- Offline-first apps  
- Clean code, modularization, DI (Koin + Hilt)  
- Modern Kotlin development  

---

<p align="center">

## ⭐ **If you like this project, please star the repo!**  
Your support helps the project grow.  
Stay creative, stay hungry, stay inspired. 🍲✨

</p>
