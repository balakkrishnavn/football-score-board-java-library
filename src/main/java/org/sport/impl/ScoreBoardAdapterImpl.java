package org.sport.impl;

import org.sport.model.Match;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ScoreBoardAdapterImpl implements ScoreBoardAdapter {

    private final List<Match> scoreBoard = new ArrayList<>();

    private ScoreBoardAdapterImpl() {
    }

    private static ScoreBoardAdapterImpl OBJECT;
    public static ScoreBoardAdapterImpl getInstance() {
        if (OBJECT == null) {
            OBJECT = new ScoreBoardAdapterImpl();
        }
        return OBJECT;
    }

    public static void reset() {
        OBJECT = null;
    }

    @Override
    public Match save(Match match) {
        scoreBoard.add(match);
        return match;
    }

    @Override
    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        findGameByTeams(homeTeam, awayTeam).ifPresent(game -> {
            game.setHomeScore(homeScore);
            game.setAwayScore(awayScore);
        });
    }

    @Override
    public List<Match> findAllByStartDate() {
        List<Match> auxList = new ArrayList<>(scoreBoard);
        auxList.sort(Comparator.comparing(Match::getStartDate));
        return auxList;
    }

    @Override
    public Optional<Match> findGameByTeams(String homeTeam, String awayTeam) {
        return scoreBoard.stream().filter(game ->
                game.getHomeTeam().equals(homeTeam) && game.getAwayTeam().equals(awayTeam)).findFirst();
    }

    @Override
    public List<Match> findPlayingGamesByTeamNames(String homeTeam, String awayTeam) {
        return scoreBoard.stream().filter(game -> (homeTeam.equalsIgnoreCase(game.getHomeTeam())
                || awayTeam.equalsIgnoreCase(game.getAwayTeam()))).collect(Collectors.toList());
    }

    @Override
    public void removeGame(String homeTeam, String awayTeam) {
        scoreBoard.removeIf(game -> homeTeam.equals(game.getHomeTeam()) && awayTeam.equals(game.getAwayTeam()));
    }

}
