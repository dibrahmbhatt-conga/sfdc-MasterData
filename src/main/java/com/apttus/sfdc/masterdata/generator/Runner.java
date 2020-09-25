package com.apttus.sfdc.masterdata.generator;

import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.apttus.helpers.Efficacies;
import com.apttus.sfdc.masterdata.cpq.MasterDataHelper;
import com.apttus.sfdc.masterdata.objectfactory.CPQCommonData;
import com.apttus.sfdc.rudiments.utils.SFDCRestUtils;

public class Runner {
	public static MasterDataHelper apiHelper;
	public static SFDCRestUtils sfdcRestUtils;
	public static String instanceURL;
	public static CPQCommonData setObjectsFactory;
	public static Properties properties=new Properties(); ;
	public static String moduleName;
	
	public static void main(String args[]) throws Exception  
	{
		moduleName = "abo"; //args[0];
		FactoryProducer factoryProducer = new FactoryProducer();
		Map<String, String> testData = new HashMap<String, String>();
		sfdcRestUtils = new SFDCRestUtils();
		
	    FileReader reader=new FileReader(factoryProducer.getFilePath()+"config.properties"); 
	    properties.load(reader);
	    properties.forEach((k, v) -> testData.put(String.format("%s",k),String.format("%s",v)));
	
		instanceURL = sfdcRestUtils.generateSFDCAccessToken(testData);
		apiHelper = new MasterDataHelper(instanceURL,sfdcRestUtils);
		
		setObjectsFactory = new CPQCommonData(apiHelper);	
		factoryProducer.setFileStatus();
		String className = "com.apttus.sfdc.masterdata.cpq."+moduleName.toUpperCase()+"MasterData";	
		System.out.println(className);
		factoryProducer.invokeTestMethodClass(className, "upsertData", null);
	}  
}