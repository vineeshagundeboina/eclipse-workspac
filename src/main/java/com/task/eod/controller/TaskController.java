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

import com.task.eod.model.Task;
import com.task.eod.repository.TaskRepo;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/task")
public class TaskController {
	
	@Autowired
	private TaskRepo taskRepo;
	
	@GetMapping("/getAll")
	private List<Task> getAllTasks(){
		return taskRepo.findAll();
	}

	
	@PostMapping("/create")
	private Task craeteTask(@RequestBody Task task) {
		return taskRepo.save(task);
	}
	
	@PutMapping("/update/{id}")
	private Task updateTask(@RequestBody Task task,@PathVariable int  id) throws Exception{
		Optional<Task> existingTask=taskRepo.findById(id);
		if(existingTask.isEmpty()) {
			throw new Exception("task not existed");
		}
		else {
			existingTask.get().setTaskName(task.getTaskName());
			
			existingTask.get().setEodDate(task.getEodDate());
			existingTask.get().setTaskStatus(task.getTaskStatus());
			
			existingTask.get().setTotalHoursSpent(task.getTotalHoursSpent());
		}
		return taskRepo.save(existingTask.get());
			
	}
	
	@DeleteMapping("/delete/{id}")
	private void deleteTask(@PathVariable int id) {
		taskRepo.deleteById(id);
	}
	
	
}

