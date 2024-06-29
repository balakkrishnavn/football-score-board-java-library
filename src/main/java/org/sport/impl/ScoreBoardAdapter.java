package org.sport.impl;

import org.sport.model.Match;

import java.util.List;
import java.util.Optional;

public interface ScoreBoardAdapter {

    Match save(Match match);

    List<Match> findPlayingGamesByTeamNames(String homeTeam, String awayTeam);

    List<Match> findAllByStartDate();

    Optional<Match> findGameByTeams(String homeTeam, String awayTeam);

    void removeGame(String homeTeam, String awayTeam);

    void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore);

}
