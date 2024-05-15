package com.cric.app.services.imp;

import com.cric.app.models.Match;
import com.cric.app.repositories.MatchRepo;
import com.cric.app.services.MatchService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class MatchServiceImpl implements MatchService {

    private MatchRepo matchrepo;

    public MatchServiceImpl(MatchRepo matchrepo) {
        this.matchrepo = matchrepo;
    }

    // Get match history from our database
    @Override
    public List<Match> getAllMatches() {
        return this.matchrepo.findAll();
    }

    @Override
    public List<Match> getLiveMatches() {
        List<Match> matches = new ArrayList<>();
        try {
            String url = "https://www.cricbuzz.com/cricket-match/live-scores";
            Document document = Jsoup.connect(url).get();
            Elements liveScoreElements = document.select("div.cb-mtch-lst.cb-tms-itm");
            for (Element match : liveScoreElements) {
                HashMap<String, String> liveMatchInfo = new LinkedHashMap<>();
                String teamsHeading = match.select("h3.cb-lv-scr-mtch-hdr").select("a").text();
                String matchNumberVenue = match.select("span").text();
                Elements matchBatTeamInfo = match.select("div.cb-hmscg-bat-txt");
                String battingTeam = matchBatTeamInfo.select("div.cb-hmscg-tm-nm").text();
                String score = matchBatTeamInfo.select("div.cb-hmscg-tm-nm+div").text();
                Elements bowlTeamInfo = match.select("div.cb-hmscg-bwl-txt");
                String bowlTeam = bowlTeamInfo.select("div.cb-hmscg-tm-nm").text();
                String bowlTeamScore = bowlTeamInfo.select("div.cb-hmscg-tm-nm+div").text();
                String textLive = match.select("div.cb-text-live").text();
                String textComplete = match.select("div.cb-text-complete").text();
                //getting match link
                String matchLink = match.select("a.cb-lv-scrs-well.cb-lv-scrs-well-live").attr("href").toString();

                Match match1 = new Match();
                match1.setTeamHeading(teamsHeading);
                match1.setMatchNumberVenue(matchNumberVenue);
                match1.setBattingTeam(battingTeam);
                match1.setBattingTeamScore(score);
                match1.setBowlTeam(bowlTeam);
                match1.setBowlTeamScore(bowlTeamScore);
                match1.setLiveText(textLive);
                match1.setMatchLink(matchLink);
                match1.setTextComplete(textComplete);
                match1.setMatchStatus();


                matches.add(match1);

//                update the match in database


               updateMatchinDB(match1);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matches;
    }

    private void updateMatchinDB(Match match1) {

        Match match = this.matchrepo.findByTeamHeading(match1.getTeamHeading()).orElse(null);
        if(match==null){
            matchrepo.save(match1);
        }else{
            match1.setMatch_id(match1.getMatch_id());
            matchrepo.save(match1);
            }
    }

    @Override
    public List<List<String>> getPointTable() {
        List<List<String>> pointTable = new ArrayList<>();
        String tableURL = "https://m.cricbuzz.com/cricket-series/6722/vijay-hazare-trophy-2023/points-table";

        try {
            Document document = Jsoup.connect(tableURL).get();
            Elements table = document.select("table.cb-col cb-col cb-col cb-col cb-col cb-col");
            Elements tableHeads = table.select("thead>tr>th");
            List<String> headers = new ArrayList<>();
            tableHeads.forEach(element -> {
                headers.add(element.text());
            });
            pointTable.add(headers);

            Elements bodyTrs = table.select("tbody>tr");
            bodyTrs.forEach(tr -> {
                List<String> points = new ArrayList<>();
                Elements tds = tr.select("td");
                points.add(tds.get(0).text()); // Assuming the first column contains team names

                tds.forEach(td -> {
                    if (!td.hasAttr("class")) {
                        points.add(td.text());
                    }
                });

                pointTable.add(points);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        return pointTable;
    }



}
