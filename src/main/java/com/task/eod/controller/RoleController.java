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
import com.task.eod.repository.RoleRepo;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/role")
public class RoleController {
	@Autowired
	private RoleRepo roleRepo;
	
	@GetMapping("/getAll")
	private List<Role> getAllroles(){
		return roleRepo.findAll();
	}
	
	@PostMapping("/create")
	private Role createRole(@RequestBody Role role) {
		return roleRepo.save(role);
	}
	
	@PutMapping("/update/{id}")
	private Role updateRole(@RequestBody Role role,@PathVariable int id) throws Exception {
		Optional<Role> existingRole=roleRepo.findById(id);
		if(existingRole.isEmpty()) {
			throw new Exception("role not found");
			}
		else {
			existingRole.get().setRoleName(role.getRoleName());
		}
		return roleRepo.save(existingRole.get());
	}
	
	@DeleteMapping("/delete/{id}")
	private void deleteRole(@PathVariable int id) {
		roleRepo.deleteById(id);
	}
	
	
}
