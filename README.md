# Smart Crossword Android App

This repository contains a simple smart crossword puzzle application built with Java for Android. The bundled puzzle is based on the classic Latin *Sator* word square and showcases a responsive interface, live correctness feedback, and optional hints for individual letters.

## Features

- 5×5 crossword grid rendered with a RecyclerView-based UI.
- Across and down clue lists that update automatically when answers are solved.
- Smart verification that highlights correct, incorrect, and hinted letters.
- Hint button that reveals the next unresolved letter, plus a check button to validate all entries.
- Material Design styling compatible with Android Studio and modern Android builds.

## Getting Started

1. Open the project in Android Studio.
2. Allow Gradle to sync; the app module targets Android SDK 34 with a minimum SDK of 24.
3. Deploy the application to an emulator or physical device to start solving the bundled puzzle.

Feel free to expand the provided puzzle data or integrate a backend to serve fresh crosswords.
