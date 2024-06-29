package org.sport.model;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Setter
@Getter
public class Match {

    private String homeTeam;

    private String awayTeam;

    private Integer homeScore;

    private Integer awayScore;

    private LocalDateTime startDate;

}
