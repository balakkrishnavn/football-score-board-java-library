package org.sport;

import org.sport.exception.GameNotFoundException;
import org.sport.exception.TeamAlreadyPlayingException;
import org.sport.impl.ScoreBoardAdapter;
import org.sport.model.Match;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ScoreBoard {

    private static ScoreBoard INSTANCE;

    private final ScoreBoardAdapter scoreBoardAdapter;

    private ScoreBoard(ScoreBoardAdapter scoreBoardAdapter) {
        this.scoreBoardAdapter = scoreBoardAdapter;
    }

    public static ScoreBoard getInstance(ScoreBoardAdapter scoreBoardAdapter) {
        if (INSTANCE == null) {
            INSTANCE = new ScoreBoard(scoreBoardAdapter);
        }
        return INSTANCE;
    }

    public static void reset() {
        INSTANCE = null;
    }

    public Match startGame(String homeTeam, String awayTeam) {
        List<Match> currentMatches = scoreBoardAdapter.findPlayingGamesByTeamNames(homeTeam, awayTeam);
        if (!currentMatches.isEmpty()) {
            throw new TeamAlreadyPlayingException(String.format("Following teams are already playing: %s",
                    currentMatches.stream().map(g ->
                            String.format("%s vs %s, ", g.getHomeTeam(), g.getAwayTeam())).collect(Collectors.joining())));
        }
        Match match = new Match(homeTeam, awayTeam, 0, 0, LocalDateTime.now());
        return scoreBoardAdapter.save(match);
    }

    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        Optional<Match> currentGames = scoreBoardAdapter.findGameByTeams(homeTeam, awayTeam);
        if (currentGames.isEmpty()) {
            throw new GameNotFoundException(String.format("%s vs %s, ", homeTeam, awayTeam));
        }
        scoreBoardAdapter.updateScore(homeTeam, awayTeam, homeScore, awayScore);
    }

    public void finishGame(String homeTeam, String awayTeam) {
        Optional<Match> currentGames = scoreBoardAdapter.findGameByTeams(homeTeam, awayTeam);
        if (currentGames.isEmpty()) {
            throw new GameNotFoundException(String.format("%s vs %s, ", homeTeam, awayTeam));
        }
        scoreBoardAdapter.removeGame(homeTeam, awayTeam);
    }

    public List<Match> getSummary() {
        List<Match> matches = scoreBoardAdapter.findAllByStartDate();
        Comparator<Match> comparator = Comparator.comparing(game -> game.getHomeScore() + game.getAwayScore());
        comparator = comparator.thenComparing(Match::getStartDate).reversed();
        return matches.stream().sorted(comparator).collect(Collectors.toList());
    }

}
