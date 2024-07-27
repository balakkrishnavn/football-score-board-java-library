package org.sports.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Game {
    private String homeTeam;
    private String awayTeam;
    private int homeScore = 0;
    private int awayScore = 0;
    private LocalDateTime startTime;

    public int getTotalScore() {
        return homeScore + awayScore;
    }
}
