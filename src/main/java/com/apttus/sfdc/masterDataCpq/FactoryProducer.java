package com.apttus.sfdc.masterDataCpq;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FactoryProducer {
	public static List<String> objectNames=new ArrayList<String>(); ;
	
	/**
	 * 
	 * @param objectName objects api name
	 * @return true if file exist in resources
	 */
	public static boolean getFileStatus(String objectName){
			if(objectNames.toString().contains(objectName))
				return true;  
			return false;
	   }
	
	/**
	 * 
	 * @param module takes module name as argument and set the file path for all objects file from resources 
	 */
	public static void setFileStatus(String module){
		String srcFolderPath = getFilePath(module);
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
	static String getFilePath(String moduleName) {
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
				"resources"+File.separator+ packageName+File.separator+moduleName+File.separator;
		return fileName;
	}	
}