package com.udemy.application1;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class CourseController {
	
	@GetMapping("/courses")
	public List<Course> findAllCourses(){
		return Arrays.asList(new Course(1,"vineesha","deew"),new Course(2,"ran","fed"));
	}

}
