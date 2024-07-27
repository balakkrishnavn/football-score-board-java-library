# Football World Cup Score Board

## Description
Java library to manage a live football World Cup score board.
## Features
1. Start a game with initial score 0-0.
2. Finish a game and remove it from the scoreboard.
3. Update the score of a game.
4. Get a summary of games ordered by total score and the most recently added.

## Requirements
- Java 17 or above
- JUnit 5 for testing

## Installation
Clone the repository and build the project using maven build tool.

## Usage
```java
ScoreBoard scoreBoard = new ScoreBoardImpl();
scoreBoard.startGame("Mexico", "Canada");
scoreBoard.updateScore("Mexico", "Canada", 0, 5);
scoreBoard.finishGame("Mexico", "Canada");
List<Game> summary = scoreBoard.getSummary();
```

## Testing
Run the tests using maven build tool.
- mvn test