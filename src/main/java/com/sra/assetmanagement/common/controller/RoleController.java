package com.sra.assetmanagement.common.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sra.assetmanagement.common.entity.Role;
import com.sra.assetmanagement.common.repository.RoleRepo;

@RequestMapping("/api/role")
@CrossOrigin(origins = "*")
@RestController
public class RoleController {
	
	@Autowired
	
	private RoleRepo roleRepo;
	
	@GetMapping("/getAll")
	public ResponseEntity<?> getRoles(){
		return ResponseEntity.ok().body(roleRepo.findAll());
	}
	
	
	@PostMapping("/create")
	public ResponseEntity<Role> create(@RequestBody Role role){
		return ResponseEntity.ok().body(roleRepo.save(role));
	}
	
	
	@GetMapping("/findByOne/{id}")
	public ResponseEntity<?> findByOne(@PathVariable long id){
		return ResponseEntity.ok(roleRepo.findById(id));
	}
	
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@RequestBody Role role,@PathVariable long id) throws Exception{
		Optional<Role> roles=roleRepo.findById(id);
		if(roles.isEmpty()) {
			throw new Exception("role not present");
		}
		else {
			return ResponseEntity.ok(roleRepo.save(role));
		}
	}
	
	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable long id){
		roleRepo.deleteById(id);
	}
	
	
		
	

}
