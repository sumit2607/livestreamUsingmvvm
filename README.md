# VideoCallApp

Welcome to **My Video call stream app**! This repository hosts the source code for an amazing Android app.

### App Details

| **App Name** | **Description** | **Tech Stack** | **Google Play Link** |
|:------------:|:---------------:|:--------------:|:--------------------:|
| Video Call   | Video Call is an application where you can share and watch short videos, make friends globally, and connect with others via live streaming and video calls. | Android, Kotlin, Jetpac compose , video-stream sdk | [![Get it on Google Play](https://firebasestorage.googleapis.com/v0/b/snapchat-f2264.appspot.com/o/T9HnFlW.png?alt=media&token=b46055e4-3b02-424f-9e88-862543831a8b)](https://play.google.com/store/apps/details?id=com.angel.snapchat) |

## Features

- Connect with people via live streaming and video calls
- User can mute video & audio stream
- Video feed similar to TikTok in progress api created
- user can see dummy profle
- user can see list of live show
- you can watch live show
- user can pay gift to user using razor-pay payment link
- swipe to refesh in live show list

## Navigation structure
Currently, we are using two separate activities: one for "Go Live" and another for "Play Live Show."
However, in reality, we can manage both flows using a single activity, GoLiveActivityCompose, by displaying relevant details based on the user's role.

Additionally, we are using a MainActivity to handle all navigation-related work.
In MainActivity, we have implemented a Bottom Navigation Bar, and on click of any item, we redirect the user to the corresponding composable function.

## Why did we use an Activity for the Live Show instead of a Fragment?
As per the current best practices in Android development, if we want to open the Live Show screen via a deep link, it is much more straightforward and reliable to navigate directly to an Activity.
Deep links are typically handled at the Activity level because:

Lifecycle Management: Activities have a complete lifecycle, making them ideal for handling entry points like notifications, external URLs, and deep links.

Back Stack Control: Opening a Fragment directly from outside the app can cause back stack issues. An Activity gives better control over the navigation stack.

Isolation of Features: Live Show is a feature that can exist independently. Using an Activity allows us to fully isolate and manage resources, orientation, and streaming sessions without depending on the hosting Activity's state.

  Deep Link Support: Most deep link handlers (Intent Filters) work at the Activity level. Directly navigating to a Fragment would require additional manual handling, adding unnecessary complexity.

### Instructions to clone this project ✌

1. Open **Android Studio**.
2. Go to **File > New > Project from Version Control**.
3. Copy the link of this repository.
4. Paste the link in the **URL** box of the Android Studio window and click on the **Clone** button.

## Dependencies

### 1. AndroidX Libraries

- `androidx.core.ktx`: Provides Kotlin extensions for AndroidX core library, offering convenience methods and enhancements.
- `androidx.appcompat`: Supports backward compatibility with older versions of Android (i.e., AppCompatActivity).
- `androidx.material`: Implements Google's Material Design components and widgets.
- `androidx.activity`: Provides activity-related functionality such as lifecycle and context support.
- `androidx.constraintlayout`: Enables complex layouts using constraint-based design (e.g., ConstraintLayout).
- `androidx.navigation.fragment.ktx`: Kotlin extensions for Navigation component, simplifying navigation-related tasks.
- `androidx.navigation.ui.ktx`: Kotlin extensions for integrating the Navigation component with UI elements like toolbar and bottom navigation.
- `androidx.media3.exoplayer`: Provides media playback capabilities with ExoPlayer, handling different media formats and streams.
- `androidx.compose.bom`: Bill of Materials (BOM) for Jetpack Compose to manage versions for Compose libraries.
- `androidx.ui.graphics`: Provides graphical utilities, such as color and drawing APIs, for Compose.
- `androidx.ui.tooling.preview`: Tools for UI design and live previews in Android Studio using Jetpack Compose.
- `androidx.material3`: Material Design 3 library with new design components.
- `androidx.runtime.livedata`: LiveData extensions for Android runtime, managing UI-related data in a lifecycle-conscious way.

### 2. Testing Libraries

- `junit`: Unit testing framework.
- `androidx.junit`: Android-specific extensions for JUnit, allowing tests to be run on Android.
- `androidx.espresso.core`: Provides UI testing tools to interact with the UI and validate its state.

### 3. Android Lifecycle Components

- `lifecycle-livedata-ktx`: LiveData extensions for Kotlin, useful for observing and managing lifecycle-bound data.
- `lifecycle-viewmodel-ktx`: ViewModel extensions for Kotlin, supporting lifecycle-aware components.

### 4. Retrofit (Networking)

- `retrofit`: Type-safe HTTP client for Android, used for making network requests and interacting with REST APIs.
- `converter-gson`: Converter for Retrofit to handle JSON to Java/Kotlin object conversion using Gson.

### 6. ExoPlayer

- `exoplayer-core`: Core ExoPlayer functionalities for media playback.
- `exoplayer-ui`: UI components for integrating ExoPlayer with your app's user interface.

### 7. WebRTC

- `webrtc-android-framework`: Provides support for WebRTC (real-time communication) on Android for live streaming or video/audio calls.

### 8. Jetpack Compose (UI Framework)

- `activity-compose`: Support for integrating Jetpack Compose with the Activity lifecycle.
- `compose.ui`: Core UI components for Jetpack Compose.
- `compose.material`: Material Design components for Compose UI.
- `lifecycle-runtime-ktx`: Lifecycle components, allowing the use of lifecycle-aware components like ViewModel, LiveData, etc.

### 9. Accompanist (Jetpack Compose Extensions)

- `accompanist-permissions`: Simplifies handling of permissions in Jetpack Compose.
- `accompanist-swiperefresh`: Swipe-to-refresh functionality for Compose-based UIs.

### 10. Dagger & Hilt (Dependency Injection)

- `dagger`: A dependency injection framework to manage dependencies in a type-safe and modular way.
- `dagger-compiler`: Compiler plugin for generating Dagger code.
- `hilt-android`: Simplifies dependency injection with Dagger for Android apps.
- `hilt-compiler`: Compiler plugin for generating Hilt code.
- `hilt-navigation-compose`: Enables seamless integration of Hilt with Jetpack Compose.

### 11. Lottie

- `lottie-compose`: Lottie animation support for Jetpack Compose, used for adding high-quality animations to your app.

### 12. SwipeRefreshLayout

- `swiperefreshlayout`: Allows users to swipe down to refresh content, used in older Android apps (before Jetpack Compose support).

### 13. Razorpay (Payments)

- `razorpay-checkout`: Integration for Razorpay’s payment gateway, enabling payments within your app.

## Screenshots

Here are some screenshots of the cloned application:

![Screenshot 1](https://github.com/sumit2607/livestreamUsingmvvm/blob/main_mvi_jetpack_di_final_2.0/f1.png)

## Screenshots For Architecture Pattern for this app
![Screenshot 1](https://github.com/sumit2607/livestreamUsingmvvm/blob/main_mvi_jetpack_di_final_2.0/img.png)

## Contributing

Contributions are welcome! If you find any issues or want to enhance the app, feel free to submit a pull request.

## License

This project is free to use.

## Contact

Have questions or suggestions? Feel free to reach out to **Sumit**.
