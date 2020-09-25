package com.apttus.sfdc.masterdata.generator;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.apttus.customException.ApplicationException;
import com.apttus.sfdc.masterdata.generator.Runner;

public class FactoryProducer {
	String moduleName = Runner.moduleName;
	
	public List<String> objectNames=new ArrayList<String>(); ;
	
	/**
	 * 
	 * @param objectName objects api name
	 * @return true if file exist in resources
	 */
	public boolean getFileStatus(String objectName){
			if(objectNames.toString().contains(objectName))
				return true;  
			return false;
	   }
	
	/**
	 * 
	 * @param module takes module name as argument and set the file path for all objects file from resources 
	 */
	public void setFileStatus(){
		String srcFolderPath = getFilePath();
		   File folder = new File(srcFolderPath);
		   File[] listOfFiles = folder.listFiles();

		   for (int i = 0; i < listOfFiles.length; i++) {
			   String fileName = listOfFiles[i].getName();
			   fileName = fileName.replaceFirst("[.][^.]+$", "");
			   objectNames.add(fileName);
		   }
	   }
	
	/**
	 * 
	 * @param module take module name
	 * @return resources file path location
	 */
	public String getFilePath() {
		StackTraceElement packagePath = null;
	    StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
	    for (int i = 0; i < stacktrace.length; i++) {
	    	if(stacktrace[i].toString().contains("com.apttus.sfdc")) {
	    		packagePath = stacktrace[i];
	    		break;
	    	}
		}
	    String [] arrayPackageName = packagePath.toString().split("\\.");
	    String packageName = arrayPackageName[3];
		String fileName = System.getProperty("user.dir") + File.separator+"src"+File.separator+"main"+File.separator+
				"resources"+File.separator+ packageName.toLowerCase()+File.separator+moduleName+File.separator;
		return fileName;
	}	
	
	 public String invokeTestMethodClass(String aClass, String aMethod, Class<?>[] params) throws Exception {
	        try {
	            Class<?> classObj = Class.forName(aClass);
	            Object obj = classObj.newInstance();
	            Method methodObj = classObj.getDeclaredMethod(aMethod, params);      
	            Object result = methodObj.invoke(obj, null);
	            System.out.println(result.toString());
	            return result.toString();
	        } catch (Exception e) {
				throw new ApplicationException(e.getMessage());
	        } 
	  }  
}