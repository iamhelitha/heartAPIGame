# Heart API Game

A JavaFX-based card game that integrates with Firebase for authentication and leaderboard functionality.

## Prerequisites

- Java 11 or higher
- Maven
- Firebase account with a service account key

## Setup

### 1. Firebase Configuration

This project requires Firebase credentials to run. The credentials file is **NOT** included in the repository for security reasons.

To set up Firebase:

1. Create a `config` directory in the project root (if it doesn't exist):
   ```bash
   mkdir config
   ```

2. Download your Firebase service account key from the Firebase Console:
   - Go to Project Settings > Service Accounts
   - Click "Generate New Private Key"
   - Save the downloaded JSON file as `config/firebase-credentials.json`

3. The `config/` directory is already in `.gitignore` to prevent accidental commits of credentials.

### 2. Running the Application

```bash
# Build the project
./mvnw clean package

# Run the application
./mvnw javafx:run
```

Or run through your IDE by executing the `Launcher` class.

## Project Structure

- `src/main/java/org/helitha/heartapigame/` - Main application code
  - `controllers/` - JavaFX controllers for each screen
  - `managers/` - Game and screen management
  - `models/` - Data models
  - `services/` - Firebase and API services
- `src/main/resources/` - FXML files and resources
- `config/` - Firebase credentials (git-ignored)

## Security Note

⚠️ **IMPORTANT**: Never commit the `config/firebase-credentials.json` file to version control. It contains sensitive private keys.

## Features

- User authentication (register/login)
- Card game with difficulty levels
- Online leaderboard
- Integration with Deck of Cards API

