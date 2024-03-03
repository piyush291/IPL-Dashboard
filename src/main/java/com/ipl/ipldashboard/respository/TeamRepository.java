package com.ipl.ipldashboard.respository;

import org.springframework.data.repository.CrudRepository;

import com.ipl.ipldashboard.model.Team;

public interface TeamRepository extends CrudRepository<Team, Long>  {
    // so the crudRepository will look into the function and returns the information based on the 4 parameters defined in in Team.java
    Team findByTeamName(String teamName);  
}
