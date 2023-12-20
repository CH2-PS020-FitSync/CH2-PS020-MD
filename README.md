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
  _ **Splash Screen**, it will appear FitSync logo before you set up on Landing Page
  _ **Login**, Enterance for user to access an application by inserting _Email_ and _Password_ and after login, User must fill _**Body Profile Form**_ for applications need
  _ **Register**, if user don't have any account yet, user can make the new one to access the application
  _ **Home**, The main first main page that is displayed when you logged in to application, it appears user **Body Profile** such as Username, Gender, Age, Weight, Height, BMI, Favorited Workout, Nutritions: Protein, Carbo , etc. Also using **SharedPreferences** to make it easier for user if already logged before.
  _ **All Workouts**, this second page that will display **All Workouts** related by body profile such as **Gender**, **Level**, **Muscle Targets**
  _ **Start Workouts**, if user click the All Workouts, it will appear timer that related by Workout minutes data.
  _ **Tracker**, this third page allow user to input their **Body Weight** to Weight Chart, and show user _Body Mass Index_
  _ **Account**, last page, this page related to Account Preferences, user can change their profile photo or Weight and Height, Enable Notification, Change Dark Theme, and also Logout from the application.
How to run this project:

* Dependencies
  _ [Retrofit 2](https://square.github.io/retrofit/)
  _ [Lottie Files](https://lottiefiles.com/)
  _ [OkHttp](https://square.github.io/okhttp/)
  _ [Material Design 3](https://m3.material.io/)
1.Copy the git link "https://github.com/CH2-PS020-FitSync/CH2-PS020-MD.git"


2.Open Android Studio


3.Pick New Project from version control


4.Paste the git link


5.Ok
