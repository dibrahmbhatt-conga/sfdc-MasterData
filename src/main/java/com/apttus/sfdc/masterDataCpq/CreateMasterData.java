package com.apttus.sfdc.masterDataCpq;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.apttus.sfdc.rudiments.utils.SFDCRestUtils;

public class CreateMasterData {
	public static MasterDataHelper apiHelper;
	public static SFDCRestUtils sfdcRestUtils;
	public static String instanceURL;
	public static SetFactory setFactory;
	
	public static void main(String args[]) throws Exception  
	{  
		Map<String, String> testData = new HashMap<String, String>();
		testData.put("url", "https://login.salesforce.com/services/oauth2/token?grant_type=password");
		testData.put("client_id", "3MVG9ZF4bs_.MKujCjTaonDe5IvAsXMkIpA4U4F465MEA6ngljBukluvBaFLJy.8lp13e6FEuX5yybTg0UdlZ");
		testData.put("client_secret", "37FFB7E9DEF298CA9437D9A5713F252429944F98635AA95E735A49EE6F09A925");
		testData.put("username", "automation@apttus.com.billing.auto"); 
		testData.put("password", "@pttus201903"); 
		
		sfdcRestUtils = new SFDCRestUtils();
		instanceURL = sfdcRestUtils.generateSFDCAccessToken(testData);
		apiHelper = new MasterDataHelper(instanceURL,sfdcRestUtils);
		
		String moduleName = "abo"; //args[0]
		setFactory = new SetFactory(apiHelper,moduleName);	
		FactoryProducer.setFileStatus(moduleName);
		String className = "com.apttus.sfdc.masterDataCpq."+moduleName.toUpperCase()+"Tests";	
		invokeTestMethodClass(className, "createData", null);
	}  
	
	 public static String invokeTestMethodClass(String aClass, String aMethod, Class<?>[] params) throws Exception {
	        try {
	            Class<?> classObj = Class.forName(aClass);
	            Object obj = classObj.newInstance();
	            Method methodObj = classObj.getDeclaredMethod(aMethod, params);      
	            Object result = methodObj.invoke(obj, null);
	            System.out.println(result.toString());
	            return result.toString();
	        } catch (Exception e) {
	            return null;
	        } 
	  }  
}