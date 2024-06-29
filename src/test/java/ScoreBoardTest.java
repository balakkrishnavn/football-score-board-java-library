import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sport.ScoreBoard;
import org.sport.exception.GameNotFoundException;
import org.sport.exception.TeamAlreadyPlayingException;
import org.sport.impl.ScoreBoardAdapterImpl;
import org.sport.model.Match;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScoreBoardTest {

    private static final String HOME_TEAM = "Team 1";
    private static final String AWAY_TEAM = "Team 2";
    private ScoreBoard scoreBoard;

    @Mock
    private Match match;
    @Mock
    private ScoreBoardAdapterImpl scoreBoardAdapter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        scoreBoard = ScoreBoard.getInstance(scoreBoardAdapter);
    }

    @AfterEach
    void resetSingleton() {
        ScoreBoard.reset();
    }

    @Test
    void shouldCreateAGame() {
        scoreBoard.startGame(HOME_TEAM, AWAY_TEAM);
        ArgumentCaptor<Match> captor = ArgumentCaptor.forClass(Match.class);
        verify(scoreBoardAdapter).save(captor.capture());
        Match matchToSave = captor.getValue();
        assertEquals(HOME_TEAM, matchToSave.getHomeTeam());
        assertEquals(AWAY_TEAM, matchToSave.getAwayTeam());
        assertEquals(0, matchToSave.getHomeScore());
        assertEquals(0, matchToSave.getAwayScore());
        assertNotNull(matchToSave.getStartDate());
    }

    @Test
    void shouldNotCreateAGame() {
        when(scoreBoardAdapter.findPlayingGamesByTeamNames(HOME_TEAM, AWAY_TEAM)).thenReturn(List.of(match));
        assertThrows(TeamAlreadyPlayingException.class, () -> scoreBoard.startGame(HOME_TEAM, AWAY_TEAM));
        verify(scoreBoardAdapter, never()).save(match);
    }

    @Test
    void shouldFinishGame() {
        when(scoreBoardAdapter.findGameByTeams(HOME_TEAM, AWAY_TEAM)).thenReturn(Optional.of(match));
        scoreBoard.finishGame(HOME_TEAM, AWAY_TEAM);
        verify(scoreBoardAdapter).removeGame(HOME_TEAM, AWAY_TEAM);
    }

    @Test
    void shouldNotFinishGame() {
        when(scoreBoardAdapter.findPlayingGamesByTeamNames(HOME_TEAM, AWAY_TEAM)).thenReturn(Collections.emptyList());
        assertThrows(GameNotFoundException.class, () -> scoreBoard.finishGame(HOME_TEAM, AWAY_TEAM));
        verify(scoreBoardAdapter, never()).removeGame(HOME_TEAM, AWAY_TEAM);
    }

    @Test
    void shouldUpdate() {
        when(scoreBoardAdapter.findGameByTeams(HOME_TEAM, AWAY_TEAM)).thenReturn(Optional.of(match));
        scoreBoard.updateScore(HOME_TEAM, AWAY_TEAM, 2, 1);
        verify(scoreBoardAdapter).updateScore(HOME_TEAM, AWAY_TEAM, 2, 1);

    }

    @Test
    void shouldNotUpdate() {
        when(scoreBoardAdapter.findGameByTeams(HOME_TEAM, AWAY_TEAM)).thenReturn(Optional.empty());
        assertThrows(GameNotFoundException.class, () -> scoreBoard.updateScore(HOME_TEAM, AWAY_TEAM, 2, 1));
        verify(scoreBoardAdapter, never()).updateScore(HOME_TEAM, AWAY_TEAM, 2, 1);
    }

    @Test
    void shouldGetSummary() {
        Match matchA = new Match("mexico", "canada", 0, 5, LocalDateTime.now().minusHours(4));
        Match matchB = new Match("spain", "brazil", 10, 2, LocalDateTime.now().minusHours(3));
        Match matchC = new Match("germany", "france", 2, 2, LocalDateTime.now().minusHours(2));
        Match matchD = new Match("uruguay", "italy", 6, 6, LocalDateTime.now().minusHours(1));
        Match matchE = new Match("argentina", "australia", 3, 1, LocalDateTime.now());
        when(scoreBoardAdapter.findAllByStartDate()).thenReturn(List.of(matchA, matchB, matchC, matchD, matchE));
        List<Match> summary = scoreBoard.getSummary();
        verify(scoreBoardAdapter).findAllByStartDate();
        assertEquals(5, summary.size());
        assertEquals(matchD, summary.get(0));
        assertEquals(matchB, summary.get(1));
        assertEquals(matchA, summary.get(2));
        assertEquals(matchE, summary.get(3));
        assertEquals(matchC, summary.get(4));
    }

}