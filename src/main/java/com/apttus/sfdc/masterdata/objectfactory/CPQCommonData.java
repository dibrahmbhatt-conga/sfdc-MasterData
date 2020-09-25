package com.apttus.sfdc.masterdata.objectfactory;

import java.io.BufferedReader;
import java.io.FileReader;

import org.json.JSONArray;
import org.json.JSONObject;

import com.apttus.sfdc.masterdata.cpq.MasterDataHelper;
import com.apttus.sfdc.masterdata.generator.FactoryProducer;

public class CPQCommonData {
	MasterDataHelper apiHelper;
	FactoryProducer factoryProducer;
	String productObjectName = "Product2";
	String accountObjectName = "Account";
	String bundleComponentViewObjectName = "Apttus_Config2_BundleComponentView";
	String classificationHierarchyObjectName = "Apttus_Config2__ClassificationHierarchy";
	String classificationNameObjectName = "Apttus_Config2__ClassificationName";
	String priceListObjectName = "Apttus_Config2__PriceList";
	String priceListItemObjectName = "Apttus_Config2__PriceListItem";
	String productClassificationObjectName = "Apttus_Config2__ProductClassification";
	String productOptionComponentObjectName = "Apttus_Config2__ProductOptionComponent";
	String productOptionGroupObjectName = "Apttus_Config2__ProductOptionGroup";
	String deleteCategoryHierarchyObjectName = "DeleteCategoryHierarchy";
	String opportunityObjectName = "Opportunity";
	String pricebookObjectName = "Pricebook2";
	String filePath = null;
	
	public CPQCommonData(MasterDataHelper masterDataHelper) throws Exception {
		this.apiHelper = masterDataHelper;	
		this.factoryProducer = new FactoryProducer();
		this.filePath = factoryProducer.getFilePath();
	}
	
	public enum objects {
		Product2,
		Account,
		Apttus_Config2_BundleComponentView,
		Apttus_Config2__ClassificationHierarchy,
		Apttus_Config2__ClassificationName,
		Apttus_Config2__PriceList,
		Apttus_Config2__PriceListItem,
		Apttus_Config2__ProductClassification,
		Apttus_Config2__ProductOptionComponent,
		Apttus_Config2__ProductOptionGroup,
		DeleteCategoryHierarchy,
		Opportunity,
		Pricebook2
	}
	
	public void upsertData(objects product2) throws Exception{
		String payloadOpportunity = updateURLValues(filePath+product2.toString()+".json");
		 apiHelper.multiUpsertData(factoryProducer.getFileStatus(product2.toString()),payloadOpportunity,product2.toString());		
	}
	
	public String updateURLValues(String fileName) throws Exception {
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
	public String jsonToString(String fileName) throws Exception {
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