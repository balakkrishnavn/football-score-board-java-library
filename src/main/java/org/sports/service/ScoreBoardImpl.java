package org.sports.service;

import lombok.extern.slf4j.Slf4j;
import org.sports.model.Game;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class ScoreBoardImpl implements ScoreBoard {
    private final List<Game> games = new ArrayList<>();

    @Override
    public void startGame(String homeTeam, String awayTeam) {
        List<Game> currentGames = checkIfTeamPlaying(homeTeam, awayTeam);
        if (currentGames.isEmpty()) {
            games.add(new Game(homeTeam, awayTeam, 0, 0, LocalDateTime.now()));
            log.info("Started game: {} {} vs {} {}", homeTeam, 0, awayTeam, 0);
        } else {
            log.error("Team is already playing: {} - {}", homeTeam, awayTeam);
        }
    }

    @Override
    public void finishGame(String homeTeam, String awayTeam) {
        List<Game> currentGames = checkIfTeamPlaying(homeTeam, awayTeam);
        if (!currentGames.isEmpty()) {
            games.removeIf(game -> game.getHomeTeam().equals(homeTeam) && game.getAwayTeam().equals(awayTeam));
            log.info("Finished game: {} vs {}", homeTeam, awayTeam);
        } else {
            log.error("Game not found: {} - {}", homeTeam, awayTeam);
        }
    }

    @Override
    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        Optional<Game> currentGames = findGameByTeams(homeTeam, awayTeam);
        if (currentGames.isPresent()) {
            findGameByTeams(homeTeam, awayTeam).ifPresent(game -> {
                game.setHomeScore(homeScore);
                game.setAwayScore(awayScore);
            });
            log.info("Updated score: {} {} - {} {}", homeTeam, homeScore, awayTeam, awayScore);
        } else {
            log.error("Unable to update score game were not found: {} - {}", homeTeam, awayTeam);
        }
    }

    private Optional<Game> findGameByTeams(String homeTeam, String awayTeam) {
        return games.stream().filter(game ->
                game.getHomeTeam().equals(homeTeam) && game.getAwayTeam().equals(awayTeam)).findFirst();
    }

    private List<Game> checkIfTeamPlaying(String homeTeam, String awayTeam) {
        return games.stream().filter(game -> (homeTeam.equalsIgnoreCase(game.getHomeTeam())
                && awayTeam.equalsIgnoreCase(game.getAwayTeam()))).collect(Collectors.toList());
    }

    @Override
    public List<Game> getSummary() {
        List<Game> sortedMatches = new ArrayList<>(games);
        sortedMatches.sort((m1, m2) -> {
            int comparison = Integer.compare(m2.getTotalScore(), m1.getTotalScore());
            if (comparison == 0) {
                return Integer.compare(games.indexOf(m2), games.indexOf(m1));
            }
            return comparison;
        });
        log.info("Retrieved game summary");
        return sortedMatches;
    }
}
