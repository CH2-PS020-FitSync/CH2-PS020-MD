# Mobile Development Team CH2-PS020
Mobile Development part of FitSync

Documentation of FitSync application in Bangkit Capstone Project 2023 Batch 2

![Frame 10](https://github.com/CH2-PS020-FitSync/CH2-PS020-MD/assets/113676521/c7f9d85e-9036-4675-8cca-23cf70ef4461)

## _About Project_
![FitSync Logo Red](https://github.com/CH2-PS020-FitSync/CH2-PS020-MD/assets/113676521/11a077ee-13e2-4e57-af86-e98848b25768)
Say the Name Fit Synchronize aka FitSync,
FitSync is an application that helps users maintain their health by tracking their healthy lifestyle by allowing users to monitor their Weight and Workouts Journey according to their preferences.
FitSync’s workout catalogue mainly focuses on Home Workouts.

This app able to give you some Workouts Recommendation and Nutrition needs for your body by Machine Learning model, and it will store into database via Cloud Computing. 

# Mobile Development Documentation

Source code of the application of FitSync using Kotlin Language to complete Bangkit Capstone Project

* Features
  - **Splash Screen**, it will appear FitSync logo before you set up on Landing Page
  - **Login**, Enterance for user to access an application by inserting _Email_ and _Password_ and after login, User must fill _**Body Profile Form**_ for applications need
  - **Register**, if user don't have any account yet, user can make the new one to access the application
  - **Home**, The main first main page that is displayed when you logged in to application, it appears user **Body Profile** such as Username, Gender, Age, Weight, Height, BMI, Favorited Workout, Nutritions: Protein, Carbo , etc. Also using **SharedPreferences** to make it easier for user if already logged before.
  - **All Workouts**, this second page that will display **All Workouts** related by body profile such as **Gender**, **Level**, **Muscle Targets**
  - **Start Workouts**, if user click the All Workouts, it will appear timer that related by Workout minutes data.
  - **Tracker**, this third page allow user to input their **Body Weight** to Weight Chart, and show user _Body Mass Index_
  - **Account**, last page, this page related to Account Preferences, user can change their profile photo or Weight and Height, Enable Notification, Change Dark Theme, and also Logout from the application.
How to run this project:

* Dependencies
  - [Retrofit 2](https://square.github.io/retrofit/)
  - [Lottie Files](https://lottiefiles.com/)
  - [OkHttp](https://square.github.io/okhttp/)
  - [Material Design 3](https://m3.material.io/)
  - [Lifecycle](https://developer.android.com/guide/components/activities/activity-lifecycle?hl=id)
  - [Android KTX](https://developer.android.com/kotlin/ktx?hl=id)
  - [Glide](https://github.com/bumptech/glide)
  - [PinView](https://github.com/mukeshsolanki/android-otpview-pinview)
  - [Android Coroutine](https://developer.android.com/kotlin/coroutines?hl=id)
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata?hl=id)
  - [Work Manager KTX](https://developer.android.com/jetpack/androidx/releases/work?hl=id)
  - [DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore?hl=id)
  - [Calendar View](https://github.com/kizitonwose/Calendar)
  - [AAChartCore](https://github.com/AAChartModel/AAChartCore)
  - [Number Picker](https://github.com/ShawnLin013/NumberPicker)
 
### Getting Started Application
* Prerequisites
  - Tools Software
    - [Android Studio](https://developer.android.com/studio?hl=id)
    - JRE (Java Runtime Enviroment) or JDK (Java Development-Kit).
  - Installation
    - Base Url for API
      - Development = https://fitsync-main-api-k3bfbgtn5q-et.a.run.app
      - Production = https://prod-fitsync-main-api-k3bfbgtn5q-et.a.run.app
    - Get an `api-key` from this [Cloud Computing Repository](https://github.com/CH2-PS020-FitSync/CH2-PS020-CC?tab=readme-ov-file#-api-documentation)
    - Clone this repository using `git clone`
      `https://github.com/CH2-PS020-FitSync/CH2-PS020-MD.git`
    - Done, you can access the FitSync Application or access the FitSync API.
   
## APK FitSync Link 
[FitSync GDrive](https://drive.google.com/drive/folders/1EShBxUigNsQTUJ4Of7mKjrjdmuSyGO9c?hl=id)

## FitSync Demo App
[Demo Application](https://youtu.be/HZVKdfYzABs?si=xwEBEsubdmPlD7-i)

