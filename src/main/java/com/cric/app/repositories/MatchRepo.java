package com.cric.app.repositories;

import com.cric.app.models.Match;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepo extends JpaRepository<Match,Integer> {

    // we will fetch match by team heading
    Optional<Match> findByTeamHeading(String teamHeading);
}
