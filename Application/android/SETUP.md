## Setup Instructions

### Prerequisites

- Android Studio (latest version)
- Android SDK 26+ (target 34)
- Kotlin 1.9.22+
- Gradle 8.2.0+

### Building the Project

1. **Clone or download this project**
   ```bash
   cd android
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the `android` directory

3. **Build the project**
   - Click Build → Make Project (or Ctrl+F9)
   - Wait for Gradle to sync and build

4. **Run on an emulator or device**
   - Click Run → Run 'app' (or Shift+F10)
   - Select a target emulator or connected device

## Permissions

The app requires the following permissions:

- **CAMERA**: To capture photos of food items
- **READ_EXTERNAL_STORAGE**: To read photos from device storage
- **WRITE_EXTERNAL_STORAGE**: To save captured photos

These permissions are declared in `AndroidManifest.xml` and requested at runtime on Android 6.0+.
