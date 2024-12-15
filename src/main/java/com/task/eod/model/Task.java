package com.task.eod.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Task {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTotalHoursSpent() {
		return totalHoursSpent;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public void setTotalHoursSpent(String totalHoursTaken) {
		this.totalHoursSpent = totalHoursTaken;
	}
	public String getEodDate() {
		return eodDate;
	}
	public void setEodDate(String eodDate) {
		this.eodDate = eodDate;
	}
	private String taskName;
	private String totalHoursSpent;
	private String eodDate;
	private String taskStatus;
	

}
