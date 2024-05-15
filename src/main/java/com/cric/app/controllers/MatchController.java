package com.cric.app.controllers;

import com.cric.app.models.Match;
import com.cric.app.services.MatchService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/match")
public class MatchController {

    public MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }
     @GetMapping("/live")
    public ResponseEntity<List<Match>> getLiveMatches(){
        return new ResponseEntity<>(this.matchService.getLiveMatches(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches(){
        return new ResponseEntity<>(this.matchService.getAllMatches(), HttpStatus.OK);
    }

    @GetMapping("/point-table")
    public ResponseEntity<?> getPointsTable(){
        return new ResponseEntity<>(this.matchService.getPointTable(),HttpStatus.OK);
    }
}
