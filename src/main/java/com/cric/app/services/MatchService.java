package com.cric.app.services;

import com.cric.app.models.Match;
import java.util.List;
import java.util.Map;

public interface MatchService {

      // get all matches
      List<Match> getAllMatches();

     // get live matches
     List<Match> getLiveMatches();

     // get points table
     List<List<String>> getPointTable();
}
