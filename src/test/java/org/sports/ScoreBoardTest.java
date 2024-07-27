package org.sports;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sports.model.Game;
import org.sports.service.ScoreBoard;
import org.sports.service.ScoreBoardImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScoreBoardTest {
    private ScoreBoard scoreBoard;

    @BeforeEach
    void setUp() {
        scoreBoard = new ScoreBoardImpl();
    }

    @Test
    void testStartGame1() {
        scoreBoard.startGame("Mexico", "Canada");
        List<Game> games = scoreBoard.getSummary();
        assertEquals(1, games.size());
        assertEquals("Mexico", games.get(0).getHomeTeam());
        assertEquals("Canada", games.get(0).getAwayTeam());
    }

    @Test
    void testStartGame2() {
        scoreBoard.startGame("Mexico", "Canada");
        scoreBoard.startGame("Spain", "Canada");
        List<Game> games = scoreBoard.getSummary();
        assertEquals(2, games.size());
    }

    @Test
    void testFinishGame() {
        scoreBoard.startGame("Mexico", "Canada");
        scoreBoard.finishGame("Mexico", "Canada");
        List<Game> games = scoreBoard.getSummary();
        assertTrue(games.isEmpty());
    }

    @Test
    void testUpdateScore() {
        scoreBoard.startGame("mexico", "canada");
        scoreBoard.updateScore("mexico", "canada", 2, 3);
        List<Game> games = scoreBoard.getSummary();
        assertEquals(2, games.get(0).getHomeScore());
        assertEquals(3, games.get(0).getAwayScore());
    }

    @Test
    void testGetSummary() {
        scoreBoard.startGame("Mexico", "Canada");
        scoreBoard.startGame("Spain", "Brazil");
        scoreBoard.startGame("Germany", "France");
        scoreBoard.startGame("Uruguay", "Italy");
        scoreBoard.startGame("Argentina", "Australia");

        scoreBoard.updateScore("Mexico", "Canada", 0, 5);
        scoreBoard.updateScore("Spain", "Brazil", 10, 2);
        scoreBoard.updateScore("Germany", "France", 2, 2);
        scoreBoard.updateScore("Uruguay", "Italy", 6, 6);
        scoreBoard.updateScore("Argentina", "Australia", 3, 1);

        List<Game> games = scoreBoard.getSummary();
        assertEquals("Uruguay", games.get(0).getHomeTeam());
        assertEquals("Italy", games.get(0).getAwayTeam());

        assertEquals("Spain", games.get(1).getHomeTeam());
        assertEquals("Brazil", games.get(1).getAwayTeam());

        assertEquals("Mexico", games.get(2).getHomeTeam());
        assertEquals("Canada", games.get(2).getAwayTeam());

        assertEquals("Argentina", games.get(3).getHomeTeam());
        assertEquals("Australia", games.get(3).getAwayTeam());

        assertEquals("Germany", games.get(4).getHomeTeam());
        assertEquals("France", games.get(4).getAwayTeam());

    }
}
