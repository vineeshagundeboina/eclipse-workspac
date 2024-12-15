package task;
import java.util.*;
public class Employee {
	private String employeeName;
	private String employeeAddress;
	private int employeeSalary;
	private String employeeMobile;
	
	public Employee(String name,String address,int salary,String mobile) {
		this.employeeName=name;
		this.employeeAddress=address;
		this.employeeSalary=salary;
		this.employeeMobile=mobile;
	}

	public static void main(String[] args) {
		Employee rEmployee=new Employee("Ramesh","marriguda",100000,"9000784494");
		Employee aEmployee=new Employee("Anil","mellavai",40000,"8000784478");
		Employee sEmployee=new Employee("Sunil","bchdf",8000,"78743268359");
	
	
	char startWith='A';
	switch(startWith) {
	case ('R'):
	{
		RanswerQuestions(rEmployee);
		break;
	}
	case('A'):
	{
		AanswerQuestions(aEmployee);
		break;
	}
	case('S'):{
		SanswerQuestions(sEmployee);
		break;
	}
	default:{
		break;
	}
	}
	}

public static void RanswerQuestions(Employee employee) {
	System.out.println("before update:"+employee.employeeMobile);
	employee.employeeMobile=null;
	System.out.println("after update:"+employee.employeeMobile);
}

public static void SanswerQuestions(Employee employee) {
	System.out.println("before update:"+employee.employeeSalary);
	employee.employeeSalary=(int)(employee.employeeSalary*0.9)+employee.employeeSalary;
	System.out.println("after update:"+employee.employeeSalary);
	System.out.println(employee.employeeName);
	System.out.println(employee.employeeAddress);
	System.out.println(employee.employeeMobile);

}
public static void AanswerQuestions(Employee employee) {
	System.out.println("before update:"+employee.employeeAddress);
	employee.employeeAddress="USA";
	System.out.println("after update:"+employee.employeeAddress);
}
	
}

