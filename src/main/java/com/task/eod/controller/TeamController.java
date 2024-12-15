package com.task.eod.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.task.eod.model.Team;
import com.task.eod.repository.TeamRepo;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/team")
public class TeamController {
	
	@Autowired
	private TeamRepo teamRepo;
	
	
	@GetMapping("/getAll")
	private List<Team> getAllTeams(){
		return teamRepo.findAll();
		
	}
	
	@PostMapping("/create")
	private Team createTeam(@RequestBody Team team) {
		return teamRepo.save(team);
	}

	@PutMapping("/update/{id}")
	private Team updateTeam(@RequestBody Team team,@PathVariable int id) throws Exception{
		Optional<Team> existingTeam=teamRepo.findById(id);
		if(existingTeam.isEmpty()) {
			throw new Exception("team not existed");
			
		}
		else {
			existingTeam.get().setTeamName(team.getTeamName());
		}
		return teamRepo.save(existingTeam.get());
	}
	
	@DeleteMapping("/delete/{id}")
	private void deleteTeam(@PathVariable int id) {
		teamRepo.deleteById(id);
	}
	
	
	
	
}
