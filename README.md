# Football World Cup Score Board Java library 

## Available methods:

Create a new match with the given teams names
```
public Game startGame(String homeTeam, String awayTeam)
```
Updates the score for a match matching the team names
```
public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore)
```
Remove the match matching the given home and away teams
```
public void finishGame(String homeTeam, String awayTeam)
```
Get a summary of matches by total score. Those matches with the same total score will be returned ordered by the most recently added to our system.
```
public List<Game> getSummary()
```

## Tech stacks
- Java 17
- Maven
- Mockito
- jUnit 5

## How to run
run test/ScoreBoardTest.java
