package adapter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sport.impl.ScoreBoardAdapter;
import org.sport.impl.ScoreBoardAdapterImpl;
import org.sport.model.Match;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ScoreBoardAdapterImplTest {

    private ScoreBoardAdapter scoreBoardAdapter;

    @BeforeEach
    void setup() {
        scoreBoardAdapter = ScoreBoardAdapterImpl.getInstance();
    }

    @AfterEach
    void reset() {
        ScoreBoardAdapterImpl.reset();
    }

    @Test
    void shouldSave() {
        Match match = new Match("Home team 1", "Away team 2", 0, 0, LocalDateTime.now());
        scoreBoardAdapter.save(match);
        List<Match> matches = scoreBoardAdapter.findAllByStartDate();
        assertTrue(matches.stream().anyMatch(g -> g.equals(match)));
    }

    @Test
    void shouldUpdate() {
        saveGames();
        scoreBoardAdapter.updateScore("Home team 1", "Away team 2", 2, 1);
        Optional<Match> gameByTeams = scoreBoardAdapter.findGameByTeams("Home team 1", "Away team 2");
        assertTrue(gameByTeams.isPresent());
        Match match = gameByTeams.get();
        assertEquals(2, match.getHomeScore());
        assertEquals(1, match.getAwayScore());
    }

    @Test
    void shouldFindAllOrderedByStartDate() {
        saveGames();
        List<Match> gamesByDate = scoreBoardAdapter.findAllByStartDate();
        assertEquals(3, gamesByDate.size());
        assertEquals("Home team 5", gamesByDate.get(0).getHomeTeam());
        assertEquals("Home team 1", gamesByDate.get(1).getHomeTeam());
        assertEquals("Home team 3", gamesByDate.get(2).getHomeTeam());
    }

    @Test
    void shouldFindPlayingTeamByName() {
        saveGames();
        List<Match> playingMatches = scoreBoardAdapter.findPlayingGamesByTeamNames("Home team 3", "Away team 2");
        assertEquals(2, playingMatches.size());
        List<Match> anotherPlayingMatches = scoreBoardAdapter.findPlayingGamesByTeamNames("Home team 3", "Away team 4");
        assertEquals(1, anotherPlayingMatches.size());
    }

    @Test
    void shouldFindGameByTeamsNames() {
        saveGames();
        Optional<Match> gameOpt = scoreBoardAdapter.findGameByTeams("Home team 1", "Away team 2");
        assertTrue(gameOpt.isPresent());
        Match match = gameOpt.get();
        assertEquals("Home team 1", match.getHomeTeam());
        assertEquals("Away team 2", match.getAwayTeam());
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
        assertNotNull(match.getStartDate());
    }

    @Test
    void shouldRemoveGame() {
        saveGames();
        String homeTeam = "Home team 1";
        String awayTeam = "Away team 2";
        scoreBoardAdapter.removeGame(homeTeam, awayTeam);
        List<Match> matches = scoreBoardAdapter.findPlayingGamesByTeamNames(homeTeam, awayTeam);
        assertTrue(matches.stream().noneMatch(game -> homeTeam.equals(game.getHomeTeam())));
    }

    private void saveGames() {
        Match match = new Match("Home team 1", "Away team 2", 0, 0, LocalDateTime.now());
        Match anotherMatch = new Match("Home team 3", "Away team 4", 0, 0, LocalDateTime.now().plusHours(2));
        Match anotherOneMatch = new Match("Home team 5", "Away team 6", 0, 0, LocalDateTime.now().minusHours(5));
        scoreBoardAdapter.save(match);
        scoreBoardAdapter.save(anotherMatch);
        scoreBoardAdapter.save(anotherOneMatch);
    }
}