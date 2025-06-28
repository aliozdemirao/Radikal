# 📰 Radikal News App
Radikal News App is a modern Android news application developed with **Jetpack Compose** and **MVI** architectural principles.

## 📸 Screenshots
<div align="center">
  <img src="https://github.com/user-attachments/assets/8b7f63dc-6178-47dc-959b-76fcb9c22173" width="19%"/>
  <img src="https://github.com/user-attachments/assets/64697915-8825-488d-a9c0-9b90d661a0be" width="19%"/>
  <img src="https://github.com/user-attachments/assets/f997e7ce-98b6-4ad9-aae3-7b7eaa10dcdf" width="19%"/>
  <img src="https://github.com/user-attachments/assets/ba9cc385-c82c-48b7-874c-39c7eb2fb2e4" width="19%"/>
  <img src="https://github.com/user-attachments/assets/18c7ad4d-ea49-4f6f-a788-58d8551e0559" width="19%"/>
</div>

<div align="center">
  <img src="https://github.com/user-attachments/assets/f9106e16-f343-43d3-acf2-11a805811b8a" width="19%"/>
  <img src="https://github.com/user-attachments/assets/f33f2dda-96f5-4e5d-b1a1-03661c5a74a9" width="19%"/>
  <img src="https://github.com/user-attachments/assets/fdb9bfcb-571a-434f-8328-03ee1b1849db" width="19%"/>
  <img src="https://github.com/user-attachments/assets/b5203b9a-d5c4-4905-b445-e05ab7c0f393" width="19%"/>
  <img src="https://github.com/user-attachments/assets/e7c34ee0-0511-42b9-b0d5-ac1fb8bd1aef" width="19%"/>
</div>

## ✨ Features
* **Latest News:** Explore the most recent headlines across various categories.
* **News Search:** Search for news articles using specific keywords.
* **Advanced Filtering:** Filter search results by language, source, domain, date range, and sort order.
* **Category-Based Browsing:** Easily navigate and browse news by category.
* **Article Details:** View the full content and source information of each article.
* **Local Bookmarks:** Save your favorite articles for offline access or later reading.
* **Open in Browser:** Open articles directly in an external web browser.
* **Share Articles:** Share interesting articles with other applications.
* **Dark/Light Theme Support:** Enjoy dynamic theming for a better user experience.
* **User-Friendly Interface:** Intuitive and modern UI built with Jetpack Compose.

## 📂 Project Structure
```plaintext
com.aliozdemir.radikal
│
├── data
│   ├── local
│   │   ├── dao
│   │   │   └── ArticleDao.kt
│   │   ├── database
│   │   │   └── AppDatabase.kt
│   │   └── entity
│   │       ├── ArticleEntity.kt
│   │       └── SourceEntity.kt
│   ├── mapper
│   │   ├── DtoMappers.kt
│   │   └── EntityMappers.kt
│   ├── remote
│   │   ├── api
│   │   │   └── NewsApi.kt
│   │   └── dto
│   │       ├── ArticleDto.kt
│   │       ├── ErrorDto.kt
│   │       ├── NewsDto.kt
│   │       └── SourceDto.kt
│   └── repository
│       └── NewsRepositoryImpl.kt
│
├── di
│   ├── DatabaseModule.kt
│   ├── NetworkModule.kt
│   └── RepositoryModule.kt
│
├── domain
│   ├── model
│   │   ├── Article.kt
│   │   ├── News.kt
│   │   └── Source.kt
│   ├── repository
│   │   └── NewsRepository.kt
│   └── usecase
│       ├── DeleteAllBookmarkedArticlesUseCase.kt
│       ├── DeleteArticleByUrlUseCase.kt
│       ├── GetAllBookmarkedArticlesUseCase.kt
│       ├── GetEverythingUseCase.kt
│       ├── GetTopHeadlinesUseCase.kt
│       ├── InsertArticleUseCase.kt
│       └── IsArticleBookmarkedUseCase.kt
│
├── navigation
│   ├── BottomNavigationBar.kt
│   ├── BottomNavItem.kt
│   ├── CustomNavType.kt
│   ├── NavigationGraph.kt
│   └── Screen.kt
│
├── ui
│   ├── bookmark
│   │   ├── BookmarkContract.kt
│   │   ├── BookmarkScreen.kt
│   │   └── BookmarkViewModel.kt
│   ├── detail
│   │   ├── DetailContract.kt
│   │   ├── DetailScreen.kt
│   │   └── DetailViewModel.kt
│   ├── home
│   │   ├── HomeContract.kt
│   │   ├── HomeScreen.kt
│   │   └── HomeViewModel.kt
│   ├── search
│   │   ├── SearchContract.kt
│   │   ├── SearchScreen.kt
│   │   └── SearchViewModel.kt
│   ├── theme
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   └── MainActivity.kt
│
├── util
│   ├── DateFormatter.kt
│   └── Resource.kt
│
└── MainApp.kt
```

## ⚙️ Build Configuration
`app/buildgradle.kts`: This file contains the build configuration for the Radikal Android application. It specifies the plugins, dependencies, and other settings required for building the app.
```kotlin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // Hilt
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)

    // Serialization
    alias(libs.plugins.kotlin.serialization)

    // Room
    alias(libs.plugins.room)
}

android {
    namespace = "com.aliozdemir.radikal"
    compileSdk = 36

    val localProperties = Properties().apply {
        val localPropertiesFile = project.rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { input ->
                load(input)
            }
        }
    }
    val newsApiKey = localProperties["NEWS_API_KEY"]

    defaultConfig {
        applicationId = "com.aliozdemir.radikal"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "NEWS_API_KEY",
            "\"${newsApiKey}\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Hilt
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)

    // Navigation - Serialization
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    // OkHttp - Retrofit
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.converter.kotlinx.serialization)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Room
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
}
```

## 🔨 Top-level Build Configuration
`buildgradle.kts`: This file serves as the top-level build configuration for the Radikal Android project. It applies common settings and plugins for all sub-projects/modules within the application.
```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // Hilt
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false

    // Serialization
    alias(libs.plugins.kotlin.serialization) apply false

    // Room
    alias(libs.plugins.room) apply false
}
```

## 📝 Dependency Versions
`libs.versions.toml`: This file defines the versions of the dependencies used in the Radikal Android application. It helps manage and update library versions in a centralized manner.
```kotlin
[versions]
agp = "8.11.0"
kotlin = "2.2.0"
coreKtx = "1.16.0"
junit = "4.13.2"
junitVersion = "1.2.1"
espressoCore = "3.6.1"
lifecycleRuntimeKtx = "2.9.1"
activityCompose = "1.10.1"
composeBom = "2025.06.01"

# Hilt
ksp = "2.1.21-2.0.1"
hilt = "2.56.2"
hiltNavigationCompose = "1.2.0"

# Navigation - Serialization
navigation = "2.9.0"
serialization = "1.8.1"

# OkHttp - Retrofit
okhttpBom = "4.12.0"
retrofit = "3.0.0"

# Coil
coilCompose = "3.2.0"

# Room
room = "2.7.2"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntimeKtx" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }
androidx-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
androidx-material3 = { group = "androidx.compose.material3", name = "material3" }

# Hilt
hilt-android = { module = "com.google.dagger:hilt-android", version.ref = "hilt" }
hilt-android-compiler = { module = "com.google.dagger:hilt-android-compiler", version.ref = "hilt" }
androidx-hilt-navigation-compose = { module = "androidx.hilt:hilt-navigation-compose", version.ref = "hiltNavigationCompose" }

# Navigation - Serialization
androidx-navigation-compose = { module = "androidx.navigation:navigation-compose", version.ref = "navigation" }
kotlinx-serialization-json = { module = "org.jetbrains.kotlinx:kotlinx-serialization-json", version.ref = "serialization" }

# OkHttp - Retrofit
okhttp-bom = { module = "com.squareup.okhttp3:okhttp-bom", version.ref = "okhttpBom" }
okhttp = { module = "com.squareup.okhttp3:okhttp" }
retrofit = { module = "com.squareup.retrofit2:retrofit", version.ref = "retrofit" }
converter-kotlinx-serialization = { module = "com.squareup.retrofit2:converter-kotlinx-serialization", version.ref = "retrofit" }

# Coil
coil-compose = { module = "io.coil-kt.coil3:coil-compose", version.ref = "coilCompose" }
coil-network-okhttp = { module = "io.coil-kt.coil3:coil-network-okhttp", version.ref = "coilCompose" }

# Room
androidx-room-compiler = { module = "androidx.room:room-compiler", version.ref = "room" }
androidx-room-ktx = { module = "androidx.room:room-ktx", version.ref = "room" }
androidx-room-runtime = { module = "androidx.room:room-runtime", version.ref = "room" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }

# Hilt
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }

# Serialization
kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }

# Room
room = { id = "androidx.room", version.ref = "room" }
```

## 📱 Tested Versions
The application has been tested on the following Android API levels:
- API 24 ("Nougat"; Android 7.0)
- API 25 ("Nougat"; Android 7.1.1)
- API 26 ("Oreo"; Android 8.0)
- API 27 ("Oreo"; Android 8.1)
- API 28 ("Pie"; Android 9.0)
- API 29 ("Q"; Android 10.0)
- API 30 ("R"; Android 11.0)
- API 31 ("S"; Android 12.0)
- API 32 ("Sv2"; Android 12L)
- API 33 ("Tiramisu"; Android 13.0)
- API 34 ("UpsideDownCake"; Android 14.0)
- API 35 ("VanillaIceCream"; Android 15.0)
- API 36 ("Baklava"; Android 16.0)
