package com.federal.fedmobilesmecore.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class RecordLog {

	public static void writeLogFile(String fileContent)
    {
	 
	 
	 try
        {
		 
		 	System.out.println( fileContent);	
			
		 	if(System.getenv("log").equalsIgnoreCase("true")) {
		 
				 	String logPath = System.getenv("logPath").toString();

		 			Date date = new Date();
		 			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		 	        String curDateTime = sdf.format(date);
			        String dateTime = ""; 
			        String[] dateTimeFormat = null;
			        java.util.Calendar c1 = new GregorianCalendar();
					dateTime = c1.getTime().toString();
					dateTimeFormat = dateTime.split(" ");
					dateTime = "";
					dateTime = dateTimeFormat[0] + dateTimeFormat[1] + dateTimeFormat[2] + dateTimeFormat[5];

					c1.add(Calendar.DAY_OF_MONTH, -10);
					String dateTime10 = c1.getTime().toString();
					String[] min10 = dateTime10.split(" ");
					String dateTime10Final = min10[0] + min10[1] + min10[2] + min10[5];
					  File file2=new File(logPath+dateTime10Final+".txt");
					 
				      if(file2.exists()) {
				    	  file2.delete();
				      }
				       
					File directory = new File(logPath);
					if (! directory.exists()){
						try {
                         directory.mkdirs();
			        	}catch (Exception e) {
							// TODO: handle exception
			        		e.printStackTrace();
						}
			            
			        }
					
			        String logFile = logPath+"//" + dateTime + ".txt";
			        File inFile = new File(logFile);
			        if (inFile.createNewFile()) {
			        }
		            FileWriter logFileWrite = new FileWriter(inFile,true);
		            BufferedWriter bufferWrite = new BufferedWriter(logFileWrite);
		            if(inFile.exists())
		            {
		            	bufferWrite.write(curDateTime + " " + fileContent);
		                bufferWrite.newLine();
		                bufferWrite.close();
		            }
		 	}
        }
        catch(Exception e)
        {  
        	e.fillInStackTrace();
        }
    }
	
	
	 
}
