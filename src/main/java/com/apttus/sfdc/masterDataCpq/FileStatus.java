package com.apttus.sfdc.masterDataCpq;

import java.io.File;

public class FileStatus  {
	   public boolean getStatus(String srcPath,String objectName){    
		   File file = new File(srcPath+"/"+objectName);
		   if(file.exists())
	         return true;         
	      return false;
	   }
}