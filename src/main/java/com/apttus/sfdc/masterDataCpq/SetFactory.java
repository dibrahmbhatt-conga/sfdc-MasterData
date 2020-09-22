package com.apttus.sfdc.masterDataCpq;

import java.io.BufferedReader;
import java.io.FileReader;
import org.json.JSONArray;
import org.json.JSONObject;

public class SetFactory {
	static MasterDataHelper apiHelper;
	static String moduleName;
	
	public SetFactory(MasterDataHelper cpqRestHelper,String module) throws Exception {
		this.apiHelper = cpqRestHelper;	
		moduleName = module;
	}
	
	public static void createProduct() throws Exception {
		String payloadProduct = updateURLValues(FactoryProducer.getFilePath(moduleName)+"Product2.json");
		 apiHelper.multiUpsertData(FactoryProducer.getFileStatus("Product2"),payloadProduct);
	}
	
	public static void createAccount() throws Exception {
		String payloadProduct = updateURLValues(FactoryProducer.getFilePath(moduleName)+"Account.json");
		 apiHelper.multiUpsertData(FactoryProducer.getFileStatus("Account"),payloadProduct);
	}
		
	public static String updateURLValues(String fileName) throws Exception {
		String sb = jsonToString(fileName);
        JSONObject obj_JSONObject = new JSONObject(sb);
		 JSONArray obj_JSONArray = obj_JSONObject.getJSONArray("compositeRequest");
        JSONObject obj_JSONObject3;
        for(int i =0;i<obj_JSONArray.length();i++) {
       	 obj_JSONObject3 = obj_JSONArray.getJSONObject(i);
	         JSONObject obj_JSONObject4 = obj_JSONObject3.getJSONObject("url");
	         obj_JSONObject3.remove("url");
	         obj_JSONObject3.put("url", "/services/data/v45.0/sobjects/"+obj_JSONObject4.getString("objectName")+"/APTS_EXT_ID__c/"+obj_JSONObject4.getString("APTS_EXT_ID__c"));
	         obj_JSONObject3.put("method", "PATCH");
	         obj_JSONObject3.put("referenceId", "ref"+obj_JSONObject4.getString("APTS_EXT_ID__c"));
        }
        return obj_JSONObject.toString();
	}
	
	/**
	 * 
	 * @param fileName 
	 * @return
	 * @throws Exception
	 */
	public static String jsonToString(String fileName) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
        StringBuilder sb = new StringBuilder();
        String jsonData = br.readLine();
        while (jsonData != null) {
            sb.append(jsonData);
            jsonData = br.readLine();
        }
        return sb.toString();
	}
}
