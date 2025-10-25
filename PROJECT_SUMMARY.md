# Heart API Game - Project Summary

## Overview
A JavaFX-based timed puzzle game that demonstrates key distributed systems concepts through gameplay. Players count hearts or carrots in images fetched from an external API, with authentication and leaderboard persistence via Firebase.

## Key Enhancements Implemented

### 1. Software Design Principles (Low Coupling & High Cohesion)

#### High Cohesion Examples:
- **ApiService**: All HTTP/API communication logic in one class
- **FirebaseService**: All Firebase operations (auth + database) encapsulated
- **GameManager**: Single responsibility - manage game state (score, timer, difficulty)
- **ScreenManager**: Single responsibility - handle all navigation
- **GameSession**: Single responsibility - manage user session data

#### Low Coupling Examples:
- **Controllers → Services**: Controllers call `ApiService.fetchGameData()` without knowing HTTP/JSON implementation details
- **Controllers → Navigation**: Use `ScreenManager.switchScene()` instead of directly managing FXML/Scene objects
- **Controllers → Controllers**: No direct dependencies between controllers; communication via shared services (GameManager, GameSession)

### 2. Event-Driven Programming

**PlayScreenController** demonstrates three types of events:

1. **User-Triggered Events**: 
   - Button clicks: `@FXML handleAnswer1()`, `handleAnswer2()`, etc.
   - Mouse hovers (HomeScreen): Play sound on button hover

2. **Time-Triggered Events**:
   - `Timeline` fires every second to update countdown timer
   - Automatically triggers game over when time reaches 0

3. **Asynchronous Events**:
   - API responses: `fetchGameData()` in background thread → `Platform.runLater()` updates UI
   - Firebase operations: Save score in background, doesn't block gameplay

### 3. Interoperability

**Three Distinct Interoperability Examples**:

1. **Java ↔ PHP (Heart Game API)**:
   - Protocol: HTTP GET requests
   - Data Format: JSON
   - Contract: `{"question": "url", "solution": hearts, "carrots": count}`
   - Implementation: `ApiService.fetchGameData()`

2. **Java ↔ Firebase Authentication**:
   - Protocol: HTTPS via Firebase Admin SDK
   - Purpose: User registration and login
   - Implementation: `FirebaseService.registerUser()`, `loginUser()`

3. **Java ↔ Cloud Firestore (Database)**:
   - Protocol: HTTPS via Firebase Admin SDK
   - Purpose: Persistent leaderboard storage
   - Data Format: NoSQL documents
   - Implementation: `FirebaseService.saveScore()`, `getTopScores()`

### 4. Virtual Identity

**Multiple Identity Mechanisms**:

1. **Authenticated Users** (FirebaseService):
   - Register with email, password, and display name
   - Firebase assigns unique UID
   - Identity persists across sessions

2. **Guest Users** (GameSession):
   - Temporary identity: "Guest4721"
   - No account created
   - Can still play and appear on leaderboard

3. **Personalization**:
   - Welcome message: "Welcome, [DisplayName]!"
   - Leaderboard shows player names
   - Scores linked to virtual identity

## Game Mechanics

### Difficulty Settings (Updated Timer Values):
- **Easy**: 45 seconds, 1 point per correct answer, no penalty
- **Medium**: 30 seconds, 3 points per correct answer, -1 penalty
- **Hard**: 20 seconds, 5 points per correct answer, -1 penalty

### Core Challenge:
Each round randomly asks for either **HEARTS ❤** or **CARROTS 🥕**. Players must read the prompt carefully as it changes unpredictably!

### Audio-Visual Feedback:
- ✅ Correct answer: Green notification "Correct! ✓ +X points" + sound effect
- ❌ Incorrect answer: Orange notification showing correct answer + sound effect
- 🎵 Background music on Home Screen
- 🔊 Button hover sounds

## Application Flow

1. **Loading Screen** → Auto-transitions to Login
2. **Login Screen** → Options: Login, Register, Play as Guest
3. **Register Screen** → Create account with Firebase Authentication
4. **Home Screen** → Navigation hub (Play, Leaderboard, Credits, Logout)
5. **Difficulty Screen** → Select Easy/Medium/Hard
6. **Play Screen** → Main game with timer, score, and API-fetched images
7. **Leaderboard Screen** → Top 10 scores from Firestore (auto-shown after game over)
8. **Credits Screen** → Developer information

## Technical Architecture

### Dependencies:
- **JavaFX**: UI framework (controls, fxml, media)
- **Jackson**: JSON parsing for API responses
- **Firebase Admin SDK**: Authentication and Firestore database
- **ControlsFX**: Enhanced UI notifications

### Key Design Patterns:
- **Singleton**: GameManager, ApiService, FirebaseService, ScreenManager, GameSession
- **MVC**: FXML views + Controller classes
- **Record (Java 16+)**: GameData, LeaderboardEntry (immutable data classes)

## Documentation Highlights

All key classes now include comprehensive Javadoc comments that explicitly reference the four assignment themes:

- **ApiService**: INTEROPERABILITY, HIGH COHESION, LOW COUPLING
- **FirebaseService**: INTEROPERABILITY, VIRTUAL IDENTITY
- **PlayScreenController**: EVENT-DRIVEN PROGRAMMING, INTEROPERABILITY, LOW COUPLING
- **GameManager**: HIGH COHESION, Singleton Pattern
- **GameSession**: VIRTUAL IDENTITY, HIGH COHESION
- **ScreenManager**: LOW COUPLING, HIGH COHESION
- **GameData**: INTEROPERABILITY (data contract)

## Video Presentation Guide

When creating your 10-minute video, cover these points:

### 1. Software Design Principles (2-3 minutes)
- Show class diagram or package structure
- Explain high cohesion in GameManager (one responsibility)
- Explain low coupling via ScreenManager (controllers don't talk to each other)
- Demo: Change navigation without touching controllers

### 2. Event-Driven Programming (2-3 minutes)
- Show Timeline code for countdown timer
- Show button click handlers (@FXML annotations)
- Show async API call with Platform.runLater()
- Demo: Play game, point out events as they happen

### 3. Interoperability (2-3 minutes)
- Show ApiService making HTTP GET to PHP server
- Show JSON response parsing with Jackson
- Show Firebase SDK communicating with Google cloud
- Demo: Network inspector showing actual HTTP requests

### 4. Virtual Identity (2-3 minutes)
- Show registration flow (creating virtual identity)
- Show guest user flow (temporary identity)
- Show how identity is used (welcome message, leaderboard)
- Demo: Register, play, see name on leaderboard

## Testing Checklist

- [ ] Register new user (Firebase Authentication)
- [ ] Login existing user
- [ ] Play as guest
- [ ] Select each difficulty level
- [ ] Complete full game (timer runs out)
- [ ] Verify score saved to Firebase Firestore
- [ ] View leaderboard (top 10 scores)
- [ ] Test audio (background music, sound effects)
- [ ] Test visual notifications (correct/incorrect answers)
- [ ] Test API error handling (disconnect internet)
- [ ] Test all navigation flows
- [ ] Logout and verify session cleared

## Files Modified/Enhanced

1. ✅ **GameManager.java**: Updated timer values (45/30/20 seconds), added comprehensive documentation
2. ✅ **PlayScreenController.java**: Added ControlsFX notifications, audio feedback, enhanced documentation
3. ✅ **ApiService.java**: Enhanced documentation highlighting interoperability
4. ✅ **FirebaseService.java**: Enhanced documentation for virtual identity and interoperability
5. ✅ **GameSession.java**: Enhanced documentation for virtual identity management
6. ✅ **ScreenManager.java**: Enhanced documentation for low coupling principle
7. ✅ **GameData.java**: Added documentation explaining interoperability contract

## Next Steps

1. **Add sound effect files** to `src/main/resources/`:
   - `correct.wav` - Play when answer is correct
   - `incorrect.wav` - Play when answer is incorrect
   - `background_music.mp3` - Already referenced in HomeScreenController
   - `click.wav` & `hover.wav` - Already referenced

2. **Firebase Setup** (if not done):
   - Create Firebase project
   - Download `serviceAccountKey.json`
   - Place in `src/main/resources/` or project root
   - Enable Authentication and Firestore

3. **Build & Run**:
   ```bash
   mvn clean javafx:run
   ```

4. **Record Video**:
   - Use screen recording software (QuickTime, OBS)
   - Show code + running application side-by-side
   - Clearly reference the four themes throughout

## Code Quality

- ✅ Clear separation of concerns
- ✅ Comprehensive Javadoc comments
- ✅ Explicit theme references in documentation
- ✅ Consistent naming conventions
- ✅ Error handling for network operations
- ✅ Singleton pattern for shared services
- ✅ Immutable data models (Records)

---

**Your project now fully demonstrates all four required themes with clear, documented examples throughout the codebase!**

