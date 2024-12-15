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

import com.task.eod.model.Role;
import com.task.eod.model.Team;
import com.task.eod.model.User;
import com.task.eod.repository.RoleRepo;
import com.task.eod.repository.TeamRepo;
import com.task.eod.repository.UserRepo;

@RestController

@CrossOrigin(origins="*")
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private RoleRepo roleRepo;
	
	@Autowired
	private TeamRepo teamRepo;
	
	@GetMapping("/getAll")
	private List<User> getAllUsers(){
		return userRepo.findAll();

	}
	
	@PostMapping("/create")
	private User createUser(@RequestBody User user) {
		System.out.println(user.toString());
		Role role=roleRepo.findById(user.getRole().getId()).get();
		
		Team team=teamRepo.findById(user.getTeam().getId()).get();
		//Task task=teamRepo.findById(user.getTask().getId()).get();
		user.setTeam(team);
		user.setRole(role);
		//user.setTask(task);
		return userRepo.save(user);
	}
	
	@PutMapping("/update/{id}")
	private User updateUser(@RequestBody User user,@PathVariable int id) throws Exception{
		
		Optional<User> existingUser=userRepo.findById(id);
		if(existingUser.isEmpty()) {
			throw new Exception("user not existed");
		}
		else {

           existingUser.get().setUserName(user.getUserName());
           existingUser.get().setUserMobile(user.getUserMobile());
           existingUser.get().setUserLocation(user.getUserLocation());
           existingUser.get().setTeam(user.getTeam());
           existingUser.get().setRole(user.getRole());
			}
		return userRepo.save(existingUser.get());
		
	}
	@DeleteMapping("/delete/{id}")
	private void deleteUser(@PathVariable int id) {
		userRepo.deleteById(id);
	}
}


