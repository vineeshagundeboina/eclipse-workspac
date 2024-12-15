package properties.practice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

public class PropPractice {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
/* writing the data to the properties file */
		File file=new File("DbConfig.properties");
		if(!file.exists())
			file.createNewFile();
		Properties prop=new Properties();
		prop.setProperty("url", "localhost");
		prop.setProperty("username", "vineesha");
		prop.setProperty("password", "vinni");
		
		FileOutputStream fos=new FileOutputStream(file);
		prop.store(fos, "new properties file created");
	
/* reading the data from the properties file */		
		
		FileInputStream fis=new FileInputStream(file);
		prop.load(fis);
	//	prop.remove("url");
	/* reading all keys */	
		//Set<String> keys=prop.stringPropertyNames();
		Set<Object> keys=prop.keySet();
		System.out.println(keys);
		
	/* reading all values */	
		Collection<Object> values=prop.values();
		System.out.println(values);
	/* reading value by giving key */	
		System.out.println(prop.getProperty("machineName"));
		System.out.println(prop.getProperty("machineName", "lenovo"));//edaina key lekapothe dani yokka default value use cheskovalanukunte ee method use cheyali
	/* reading all the data from properties file */			
		System.out.println(prop);
	/* using keyset */	
		for(Object key:prop.keySet()) {
			System.out.println(key+" >>>>>> "+prop.getProperty((String) key));
		}
	/* using entryset */	
		Set<Entry<Object,Object>> entries=prop.entrySet();
		for(Entry<Object,Object> entry:entries) {
			System.out.println(entry.getKey()+" = "+entry.getValue());
		}
		
//	/*  updating data of properties file */
//		
//		FileInputStream fis=new FileInputStream(file);
//		prop.load(fis);
//		prop.setProperty("url", "10.251.111.55");
//		
		
	/* removing data from properties file */
		
		prop.remove("url");
		
	 
	}
	

}
