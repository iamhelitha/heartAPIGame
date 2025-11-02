# Heart API Game 🎮

A retro-style JavaFX game that challenges players to count hearts in images fetched from an external API. Features user authentication, difficulty levels, leaderboards, and an 8-bit aesthetic.

## 📖 Overview

Heart API Game is an interactive counting game where players:
- Count hearts in randomly generated images
- Race against the clock based on difficulty level
- Compete on global leaderboards
- Choose between authenticated play or guest mode

## ✨ Features

### 🎯 Core Gameplay
- **Visual Counting Challenge**: Count hearts in images from the Heart Game API
- **Multiple Choice Answers**: Select from 4 possible answers
- **Three Difficulty Levels**:
  - **Easy**: 45 seconds per round, 1 point per correct answer
  - **Medium**: 30 seconds per round, 3 points per correct answer  
  - **Hard**: 20 seconds per round, 5 points per correct answer
- **Score Tracking**: Accumulate points for correct answers

### 👤 User System
- **User Registration**: Create account with email, password, and display name
- **User Login**: Secure authentication via Firebase
- **Guest Mode**: Play without creating an account (temporary identity like "Guest4721")
- **Virtual Identity**: Personalized welcome messages and leaderboard entries

### 🏆 Leaderboard
- **Global Rankings**: View top scores from all players
- **Persistent Storage**: Scores saved to Firebase Firestore
- **Real-time Updates**: See the latest high scores

### 🎵 Audio & Visuals
- **Background Music**: Retro 8-bit soundtrack that loops continuously
- **Sound Effects**: Click sounds for buttons, correct/incorrect answer feedback
- **Mute Toggle**: Turn audio on/off from any screen
- **Retro 8-bit Theme**: Custom pixel font and vintage game aesthetics
- **Consistent Styling**: Global CSS applied across all screens

## 🏗️ Architecture

### Design Patterns

#### **Singleton Pattern**
Used for managers and services to ensure single instances:
- `GameManager` - Game state management
- `GameSession` - User session tracking
- `ScreenManager` - Navigation control
- `SoundManager` - Audio management
- `ApiService` - API communication
- `FirebaseService` - Firebase operations

#### **MVC Pattern**
- **Models**: `GameData`, `LeaderboardEntry` - Data structures
- **Views**: FXML files - UI layouts
- **Controllers**: `*Controller.java` files - Handle user interactions

#### **Low Coupling & High Cohesion**
- Each class has a single, well-defined responsibility
- Controllers don't depend on each other directly
- Managers provide abstraction layers for common operations

### Event-Driven Programming
- **User Events**: Button clicks trigger navigation and game actions
- **Timer Events**: Countdown timer fires every second
- **Asynchronous Events**: API responses update UI via `Platform.runLater()`

### Interoperability
- **External API**: Fetches game data from `https://marcconrad.com/uob/heart/api.php`
- **JSON Parsing**: Uses Jackson library for JSON ↔ Java object conversion
- **Firebase Integration**: Cloud-based authentication and database via Firebase Admin SDK

## 📁 Project Structure

```
heartAPIGame/
├── pom.xml                          # Maven configuration
├── config/
│   └── firebase-credentials.json    # Firebase service account key (not in git)
└── src/main/
    ├── java/org/helitha/heartapigame/
    │   ├── Launcher.java            # Application entry point
    │   ├── Main.java                # JavaFX application initialization
    │   ├── controllers/             # UI controllers
    │   │   ├── LoadingScreenController.java
    │   │   ├── LoginScreenController.java
    │   │   ├── RegisterScreenController.java
    │   │   ├── HomeScreenController.java
    │   │   ├── DifficultyScreenController.java
    │   │   ├── PlayScreenController.java
    │   │   ├── LeaderboardScreenController.java
    │   │   └── CreditsScreenController.java
    │   ├── managers/                # Business logic managers
    │   │   ├── GameManager.java
    │   │   ├── GameSession.java
    │   │   ├── ScreenManager.java
    │   │   └── SoundManager.java
    │   ├── models/                  # Data models
    │   │   ├── GameData.java
    │   │   └── LeaderboardEntry.java
    │   └── services/                # External service integrations
    │       ├── ApiService.java
    │       └── FirebaseService.java
    └── resources/org/helitha/heartapigame/
        ├── *.fxml                   # FXML layout files
        ├── css/
        │   └── global-styles.css    # Global stylesheet
        ├── fonts/
        │   └── PressStart2P-Regular.ttf
        └── sounds/
            ├── Jeremy Blake - Powerup!  NO COPYRIGHT 8-bit Music.mp3
            └── Mouse Click Sound Effect.wav
```

## 🛠️ Technologies Used

- **Java 21** - Programming language
- **JavaFX 21** - GUI framework
- **Maven** - Build and dependency management
- **Firebase Admin SDK** - Authentication and Firestore database
- **Jackson** - JSON processing
- **ControlsFX** - Enhanced UI controls
- **HTTP Client** - API communication

## 📋 Prerequisites

- **Java Development Kit (JDK) 21** or higher
- **Maven 3.6+** (or use included Maven wrapper)
- **Firebase Project** with Firestore and Authentication enabled
- **Internet Connection** (for API and Firebase communication)

## 🚀 Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/iamhelitha/heartAPIGame.git
cd heartAPIGame
```

### 2. Configure Firebase

1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Enable **Authentication** → **Email/Password** sign-in method
3. Enable **Firestore Database** in production mode
4. Go to **Project Settings** → **Service Accounts**
5. Click **"Generate new private key"** to download `firebase-credentials.json`
6. Place the file in `config/firebase-credentials.json`

### 3. Build the Project
```bash
# Using Maven wrapper (recommended)
./mvnw clean install

# Or using system Maven
mvn clean install
```

### 4. Run the Application
```bash
# Using Maven wrapper
./mvnw javafx:run

# Or using system Maven
mvn javafx:run

# Or run the JAR directly
java -jar target/heartAPIGame-1.0-SNAPSHOT.jar
```

## 🎮 How to Play

1. **Launch the Game** - Start from the loading screen
2. **Login or Register** - Create an account or play as guest
3. **Select Difficulty** - Choose Easy, Medium, or Hard
4. **Count Hearts** - Look at the image and count the hearts
5. **Select Answer** - Click one of the four answer buttons
6. **Beat the Clock** - Answer correctly before time runs out
7. **View Leaderboard** - See how you rank against other players

## 🎯 Gameplay Tips

- **Start with Easy**: Get familiar with the game mechanics
- **Watch the Timer**: Different difficulties have different time limits
- **Count Carefully**: Take your time on Easy mode to practice
- **Challenge Yourself**: Progress to Hard mode for maximum points
- **Guest Mode**: Test the game without creating an account

## 🔊 Audio Controls

- **🔊/🔇 Button**: Available on all screens to mute/unmute audio
- **Background Music**: 8-bit retro soundtrack loops continuously
- **Sound Effects**: Button clicks and answer feedback

## 🏆 Scoring System

| Difficulty | Time Limit | Points per Correct Answer |
|------------|------------|---------------------------|
| Easy       | 45 seconds | 1 point                   |
| Medium     | 30 seconds | 3 points                  |
| Hard       | 20 seconds | 5 points                  |

## 🐛 Troubleshooting

### Firebase Connection Issues
- Verify `firebase-credentials.json` is in the `config/` directory
- Check Firebase console for project status
- Ensure Firestore and Authentication are enabled

### Sound Not Playing
- Check that audio files are in `src/main/resources/org/helitha/heartapigame/sounds/`
- Verify sound file formats (MP3 for music, WAV for effects)
- Try toggling the mute button

### API Connection Errors
- Ensure you have an active internet connection
- Check if `https://marcconrad.com/uob/heart/api.php` is accessible
- API may occasionally be unavailable - retry after a moment

### Build Errors
- Confirm Java 21+ is installed: `java -version`
- Clear Maven cache: `./mvnw clean`
- Delete `target/` folder and rebuild

## 📄 License

This project is developed as an educational assignment. All rights reserved.

## 🎨 Credits

- **Font**: [Press Start 2P](https://fonts.google.com/specimen/Press+Start+2P) by CodeMan38
- **Background Music**: "Powerup!" by Jeremy Blake (No Copyright 8-bit Music)
- **API**: Heart Game API by Marc Conrad (University of Bedfordshire)
- **Firebase**: Google Firebase for authentication and database services

## 👨‍💻 Developer

**Helitha**
- GitHub: [@iamhelitha](https://github.com/iamhelitha)

## 🙏 Acknowledgments

- University of Bedfordshire for project requirements
- Marc Conrad for the Heart Game API
- Google Firebase team for excellent documentation
- JavaFX community for tutorials and support

---

**Enjoy the game! 🎮❤️**
