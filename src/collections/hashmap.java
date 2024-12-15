package collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class hashmap {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Employee employee=new Employee();
		employee.setEmpId("1185");
		employee.setEmpName("Vineesha");
		
		Employee employee1=new Employee();
		employee1.setEmpId("3224");
		employee1.setEmpName("gunde");
		
		
		ArrayList<Employee> employeeList=new ArrayList<Employee>();
		employeeList.add(employee);
		employeeList.add(employee1);
		//System.out.println(employeeList);
		for(Employee emp:employeeList) {
			System.out.println(emp.getEmpId());
			System.out.println(emp.getEmpName());
			//System.out.println("empname:"+emp.getEmpName());
			//System.out.println("empid:"+emp.getEmpId());
		}
		
		/*for(Employee emp:employeeList) {
			System.out.println("empname:"+emp.getEmpName());
			System.out.println("empid:"+emp.getEmpId());
		}*/
		
		//using HashMap
		HashMap<String,String> hm=new HashMap<String,String>();
		for(Employee emp:employeeList) {
			//hm.put("empName", emp.getEmpName());
			//hm.put("empId",emp.getEmpId());
			/*hm.put("empName1", emp.getEmpName());
			hm.put("empId1", emp.getEmpId());*/
			hm.put(emp.getEmpId(), emp.getEmpName());

			}
		//iterate hashmap
		for(Map.Entry<String,String> m:hm.entrySet()) {
			System.out.println("empId:"+m.getKey()+",empName:"+m.getValue());

		}
		System.out.println(hm);
		
		
		
	}

}
