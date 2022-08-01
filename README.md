# bpai-kt
This app's name is Ceritaku, a story app to tell you a success story of Dicoding's students. This app built to complete Belajar Pengembangan Aplikasi Android Intermediate submissions by Dicoding using Kotlin.

# gitser-kt

[![Platform](https://img.shields.io/badge/platform-Android-green)](https://github.com/yumtaufikhidayat/gitser-kt/blob/main/build.gradle)
[![Language](https://img.shields.io/badge/language-Kotlin-blue)](https://github.com/yumtaufikhidayat/gitser-kt/blob/main/build.gradle)
[![MinSdk](https://img.shields.io/badge/minsdk-23-red)](https://github.com/yumtaufikhidayat/gitser-kt/blob/main/build.gradle)

> This app built to accomplish Belajar Pengembangan Aplikasi Android Intermediate class which held by [dicoding.com]. Please be wise to clone and learn the flow of app. You are not allowed to copy/create derivatives/steal/sell this program codes.

This application is a simple Github user search app using Kotlin. Built using these technologies:
- Material Design
- Kotlin
- View Model (MVVM)
- LiveData
- View Binding
- Coroutine Flow
- DataStore
- Retrofit
- Paging 3
- Google Maps API

This application has some features:
- Submission 1
  - Login, register, and logout -> Using [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) (Like shared preferences but better) to store user data.
  - Animation -> Using property animation. See [https://developer.android.com/guide/topics/graphics/prop-animation](https://developer.android.com/guide/topics/graphics/prop-animation)
  - List of story -> Using recyclerview.
  - Upload image to server -> Using Retrofit with Multipart.

- Submission 2
  - Infinite scroll -> Using [paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-migration)
  - Show users location -> Using [Google Maps API](https://developers.google.com/maps/documentation/android-sdk)
