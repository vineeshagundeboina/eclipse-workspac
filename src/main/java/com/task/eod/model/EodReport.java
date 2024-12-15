package com.task.eod.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class EodReport {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String eodDate;
	
	@OneToMany
	private List<Task> tasks;

	public int getId() {
		return id;
	}

	public String getEodDate() {
		return eodDate;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setEodDate(String eodDate) {
		this.eodDate = eodDate;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	

}
