package com.apttus.sfdc.masterDataCpq;

public abstract class AbstractFactory {
	   abstract FileStatus getStatus(String srcPath,String objectName) ;
	}