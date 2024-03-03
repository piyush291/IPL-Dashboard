package com.ipl.ipldashboard.data;

import java.time.LocalDate;

import org.springframework.batch.item.ItemProcessor;

import com.ipl.ipldashboard.model.Match;

public class MatchDataProcessor implements ItemProcessor<MatchInput, Match> {
    
    @Override
    public Match process(final MatchInput matchInput){
        Match match = new Match();
        match.setID(Long.parseLong(matchInput.getID()));
        match.setCity(matchInput.getCity());
        match.setDate(LocalDate.parse(matchInput.getDate()));
        match.setVenue(matchInput.getVenue());
        match.setPlayerOfMatch(matchInput.getPlayer_of_Match());

        String firstInningsTeam, secondInningsTeam;
        if("bat".equals(matchInput.getTossDecision())){
            firstInningsTeam = matchInput.getTossWinner();
            secondInningsTeam = (matchInput.getTossWinner()==matchInput.getTeam1()) ? matchInput.getTeam2() : matchInput.getTeam1();
        }
        else {
            secondInningsTeam = matchInput.getTossWinner();
            firstInningsTeam = (matchInput.getTossWinner()==matchInput.getTeam1()) ? matchInput.getTeam2() : matchInput.getTeam1();
        }

        match.setTeam1(firstInningsTeam);
        match.setTeam2(secondInningsTeam);

        match.setTossDecision(matchInput.getTossDecision());
        match.setTossWinner(matchInput.getTossWinner());

        match.setMatchWinner(matchInput.getWinningTeam());

        match.setMargin(matchInput.getMargin());
        match.setUmpire1(matchInput.getUmpire1());
        match.setUmpire2(matchInput.getUmpire2());

        System.out.println("########Piyush777777###########");
        return match;
    }
}
