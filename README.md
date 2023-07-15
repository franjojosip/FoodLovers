
# FoodLovers Kotlin mobile application

This project is an example of a recipe mobile app.<br /> The goal is to work with recipes which are also available offline mode. Once the data is downloaded, everything will be persisted in a ROOM database. You can search recipes, add/edit recipes, choose your favorites, and customize your profile.

## Clean architecture with 5 main modules
- Main (for prelogin and main activity)
- Common (for database, API, repository, custom views etc.)
- Auth (for authentication)
- Account (for user profile logic)
- Recipe (for recipe screens)
    
## Useful features
- MVVM architecture
- Dependency injection ([Koin](https://github.com/InsertKoinIO))
- Room persistance ([Room](https://developer.android.com/jetpack/androidx/releases/room))
- Firebase Storage, Analytics and Crashlytics ([Firebase](https://firebase.google.com/))
- Concurrency design pattern ([Coroutines](https://developer.android.com/kotlin/coroutines))
- Jetpack Navigation ([Navigation](https://developer.android.com/jetpack))
- Google Maps ([Google Maps](https://developers.google.com/maps/documentation/android-sdk/))
- Localization ([Localization](https://developer.android.com/guide/topics/resources/localization))
- Network calls ([Retrofit](https://github.com/square/retrofit))
- Android architecture components to share ViewModels during configuration changes and LiveData
- Material Design [Material Design](https://material.io/blog/android-material-theme-color)
- Notifications [NotificationManager](https://developer.android.com/reference/android/app/NotificationManager)
- Edge To Edge Configuration

## Getting started

1. Download this repository extract and open the template folder on Android Studio
2. Rename the app package in Manifest `ht.ferit.fjjukic.foodlovers`
3. Check if the manifest package was renamed along with the package
5. On `app/build.gradle`, change the applicationId to the new app package
6. On `app/build.gradle`, update the dependencies Android Studio suggests
7. On `strings.xml`, set your application name, google maps key, notification channel id
8. On `Theme.kt` & `Color.kt` set your application style
9. Replace the App Icons
10. Run `./gradlew dependencyUpdates` and check for dependencies
11. Ready to Use

And you're ready to start working on your new app.

## App Design

<img src="https://github.com/franjojosip/FoodLovers/assets/52075105/86342411-3ec5-4fff-9d56-372c6266a6a1" width="300" height="620">&nbsp;&nbsp;
<img src="https://github.com/franjojosip/FoodLovers/assets/52075105/7d2c9970-5d30-4fdd-a1d0-01cf6cf0b14f" width="300" height="620">&nbsp;&nbsp;

<img src="https://github.com/franjojosip/FoodLovers/assets/52075105/cda2e42c-6b14-4064-b6a5-17c9f8ec5290" width="300" height="620">&nbsp;&nbsp;
<img src="https://github.com/franjojosip/FoodLovers/assets/52075105/5a54b1ae-a766-4629-96b2-dfeb4ab6cf96" width="300" height="620">&nbsp;&nbsp;

<img src="https://github.com/franjojosip/FoodLovers/assets/52075105/35e63960-3cc0-4e5f-874e-3746db8256cd" width="300" height="620">&nbsp;&nbsp;
<img src="https://github.com/franjojosip/FoodLovers/assets/52075105/16b0b997-9dba-4c6a-bf9c-5580daafc324" width="300" height="620">&nbsp;&nbsp;

## Notes
- Registration is required
- To use profile Location mode - use your own google maps api key
