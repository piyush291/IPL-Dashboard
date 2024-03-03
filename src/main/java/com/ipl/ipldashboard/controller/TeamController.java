package com.ipl.ipldashboard.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipl.ipldashboard.model.Match;
import com.ipl.ipldashboard.model.Team;
import com.ipl.ipldashboard.respository.MatchRepository;
import com.ipl.ipldashboard.respository.TeamRepository;

@RestController

// This cross origin is for handling that problem which got aroused while calling the Rest API from react
@CrossOrigin
public class TeamController {

    // dependancy injection
    private TeamRepository teamRepository;
    private MatchRepository matchRepository;
    
    // This controller is somewhere autowiring teamRepository and matchRepository
    public TeamController(TeamRepository teamRepository, MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }


    @GetMapping("/team")
    public Iterable<Team> getAllTeam() {
        // function provided by JPA repository
        return this.teamRepository.findAll();
    }

    @GetMapping("/team/{teamName}")
    public Team getTeam(@PathVariable String teamName) {
        Team team = this.teamRepository.findByTeamName(teamName);
        team.setMatches(matchRepository.findLatestMatchesbyTeam(teamName,4));
            
        return team;
    }

    @GetMapping("/team/{teamName}/matches")
    public List<Match> getMatchesForTeam(@PathVariable String teamName, @RequestParam int year) {
        // For example somebody passes 2019, then startDate will have 1st January 2019
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year + 1, 1, 1);
        return this.matchRepository.getMatchesByTeamBetweenDates(
            teamName,
            startDate,
            endDate
            );
    }

}    
