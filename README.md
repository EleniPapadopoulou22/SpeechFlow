# SpeechFlow

## Overview

SpeechFlow is an Android app that converts spoken language into text and converts typed text into speech. It uses Jetpack Compose for the UI, Room for local transcription storage, and Android speech/TTS APIs for voice features. The main language of this app is in Greek but it can work for english spoken language too.

## What this app does

- Launches with onboarding screens until the user completes onboarding.
- Shows a home screen with two main actions:
  - `Ομιλία` (Listen): records speech, converts it to text, and saves transcriptions.
  - `Πληκτρολόγηση` (Speak): accepts typed text and reads it aloud using text-to-speech.
- Stores transcription history in a local Room database.
- Provides settings for:
  - recognition language (`el-GR` or `en-US`)
  - display theme (`light`, `dark`, `system`)
  - font size scaling
  - speech rate
  - vibration toggle

## Entry point

- Launcher activity: `MainActivity`
- Application class: `SpeechFlowApplication`

## Build system

- Gradle wrapper: `gradle-9.3.1-bin.zip`
- Android Gradle Plugin: `8.5.0`
- Kotlin: `2.0.21`
- One module: `:app`

## Android SDK / JDK

- `compileSdk` = 34
- `targetSdk` = 34
- `minSdk` = 24
- Kotlin/JVM target = 17

## Required permissions

- `RECORD_AUDIO`
- `VIBRATE`

## Project structure

- `app/src/main/AndroidManifest.xml` — app manifest, permissions, launcher activity
- `app/src/main/java/com/eleni/speechflow/MainActivity.kt` — main activity and navigation setup
- `app/src/main/java/com/eleni/speechflow/SpeechFlowApplication.kt` — app service container
- `app/src/main/java/com/eleni/speechflow/ui/` — Compose screens and navigation
- `app/src/main/java/com/eleni/speechflow/data/` — speech, TTS, database, settings
- `gradle/libs.versions.toml` — dependency versions and plugin definitions
- `gradle/wrapper/gradle-wrapper.properties` — Gradle wrapper version

## Setup

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd SpeechFlow
   ```

2. Open Android Studio.

3. In Android Studio, choose:
   - `Open` and select the `SpeechFlow` project root.

4. Allow Android Studio to sync the Gradle project.

5. If Android Studio prompts for SDK components, install the Android SDK for API 34.

## Running

1. Connect an Android device or start an emulator.

2. In Android Studio, select the `app` configuration.

3. Click `Run` or press `Shift+F10`.

4. Grant microphone permission when prompted.

## Notes

- The app uses the Android speech recognition API and text-to-speech API.
- On first launch, onboarding screens appear and are skipped on future launches after completion.
- Transcriptions are saved locally and shown in the history screen.
  
##App preview-Screenshots

  <img width="712" height="547" alt="image" src="https://github.com/user-attachments/assets/16052fcc-8aa5-4f30-bcad-de554bd9e997" />

