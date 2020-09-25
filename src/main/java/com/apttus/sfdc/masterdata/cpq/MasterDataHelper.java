package com.apttus.sfdc.masterdata.cpq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.apttus.customException.ApplicationException;
import com.apttus.sfdc.rudiments.utils.SFDCRestUtils;
import com.apttus.sfdc.rudiments.utils.URLGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

public class MasterDataHelper {
	SFDCRestUtils sfdcRestUtils;
	URLGenerator urlGenerator;
	
	public MasterDataHelper(String instanceUrl, SFDCRestUtils restUtils) throws Exception {
		this.urlGenerator = new URLGenerator(instanceUrl);
		this.sfdcRestUtils = restUtils;
	}
	/**
	 * This method created to help in creation of json structure
	 * @param objectName API name of the object
	 * @param recordId id of the record from the existing org
	 * @throws Exception
	 */
	public void createJsonBody(String objectName,String recordId) throws Exception {
		Response response = sfdcRestUtils.getData(urlGenerator.commonSFDCURL+objectName+"/describe");
		if (response.getStatusCode() != 200) {
			throw new ApplicationException(
					"Failure while fetching fields of object using the API :"
					+ " The response code was:" + response.getStatusCode()
							+ "and the response body received is: " + response.getBody().asString());
		}
		//getting whole json string
		JSONObject jsonObj = new JSONObject(response.getBody().asString());

		//extracting fields from json string
		JSONArray ja_data = jsonObj.getJSONArray("fields");
		 List<String> allfields=new ArrayList<String>();
		 Map<String,String> allFieldsType=new HashMap<String,String>();
		 Map<String,String> allFieldsRelationshipName=new HashMap<String,String>();

		 String fields=null;
		//loop to get all fields from data json array
		for(int i=0; i<ja_data .length(); i++) 
		{
			if(!ja_data.getJSONObject(i).getString("name").equals("Id") 
				&& !ja_data.getJSONObject(i).getString("name").equalsIgnoreCase("APTS_Ext_ID__c")
				&& !ja_data.getJSONObject(i).getString("name").equals("IsDeleted")
				&& !ja_data.getJSONObject(i).getString("name").equals("OwnerId")
				&& !ja_data.getJSONObject(i).getString("name").equals("CreatedDate")
				&& !ja_data.getJSONObject(i).getString("name").equals("LastViewedDate")
				&& !ja_data.getJSONObject(i).getString("name").equals("LastReferencedDate")
				&& !ja_data.getJSONObject(i).getString("name").equals("CreatedById")
				&& !ja_data.getJSONObject(i).getString("name").equals("LastModifiedDate")
				&& !ja_data.getJSONObject(i).getString("name").equals("LastModifiedById")
				&& !ja_data.getJSONObject(i).getString("name").equals("SystemModstamp")) {
				allfields.add(ja_data.getJSONObject(i).getString("name").toString());
				allFieldsType.put(ja_data.getJSONObject(i).getString("name"),ja_data.getJSONObject(i).getString("type").toString());
				allFieldsRelationshipName.put(ja_data.getJSONObject(i).getString("name"),ja_data.getJSONObject(i).getString("relationshipName").toString());
				fields += fields==null ? ja_data.getJSONObject(i).getString("name").toString() : ","+ja_data.getJSONObject(i).getString("name").toString();
			}
		}

		String selectQuery = "Select "+fields.replace("null", "")+ " from " +objectName+ " where id='"+recordId+"'";
		Response responseWithData = this.sfdcRestUtils.getWithQueryParams(this.urlGenerator.queryURL, selectQuery);
		if (responseWithData.getStatusCode() != 200) {
			throw new ApplicationException(
					"Failure while fetching data with query using the API :"
							+ this.urlGenerator.queryURL + ". The response code was:" + responseWithData.getStatusCode()
							+ "and the response body received is: " + responseWithData.getBody().asString());
		}
		
		JsonObject jsonObject = new Gson().fromJson(responseWithData.getBody().asString(), JsonObject.class);
		String responseDataFields = responseWithData.getBody().asString();
		Gson gson = new GsonBuilder().create();
		responseDataFields = gson.toJson(jsonObject);
		
		JSONObject bodyObject = new JSONObject(responseDataFields);
		JSONArray recordsArray = bodyObject.getJSONArray("records");
		JSONObject recordsObject = recordsArray.getJSONObject(0);
        JSONArray recordFields = recordsObject.names();
			    
		 JSONObject urlObject = new JSONObject();
		 urlObject.put("objectName", objectName);
		 urlObject.put("APTS_EXT_ID__c", "");
		 
		 JSONObject jsonRequestObject = new JSONObject();
		 jsonRequestObject.put("url", urlObject);
		 JSONObject bodyRequestObject = new JSONObject();
		 JSONObject externalObject = new JSONObject();
		
		for(int i=0;i<recordFields.length();i++) {
			if(!recordFields.get(i).toString().equalsIgnoreCase("attributes")) {
				if(allFieldsType.get(recordFields.get(i)).equals("reference")) {
					externalObject.put("APTS_EXT_ID__c", "EXTERNAL_ID");
					bodyRequestObject.put(allFieldsRelationshipName.get(recordFields.getString(i)), externalObject);
				}
				else 
					bodyRequestObject.put(recordFields.getString(i), recordsObject.getString(recordFields.getString(i)));//recordValues.get(i));
			}
		}
		jsonRequestObject.put("body", bodyRequestObject);
		
		Gson gsonPrettyPrint = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElem = jsonParser.parse(jsonRequestObject.toString());
		String prettyJsonString = gsonPrettyPrint.toJson(jsonElem);
		
		System.out.println(prettyJsonString);
	}
	
	/**
	 * 
	 * @param fileStatus to verify whether file exist or not
	 * @param payload takes files payload as api request body
	 * @throws ApplicationException
	 */
	public void multiUpsertData(boolean fileStatus,String payload, String objectName) throws ApplicationException {
		if(fileStatus) {
				Response response = sfdcRestUtils.postWithoutAppUrl(this.urlGenerator.multiUpsertData,payload);
				System.out.println(response.getBody().asString());
				System.out.println(objectName+" upsert operation done successfully");
		}
	}
	
	/**
	 * 
	 * @param requestBody pass request body for console
	 * @return api response
	 */
	public Response developerConsole(String requestBody) {
		return sfdcRestUtils.getData(urlGenerator.developerConcoleURL+requestBody);
	}
	
	/** 
	 * 
	 * @param requestBody pass request body for console
	 * @param fetchField pass the key which is passed in system.debug
	 * @return the value which is printed with fetchField key
	 * @throws ApplicationException
	 */
	public String developerConsoleWithResponse(String requestBody,String fetchField) throws ApplicationException {
		Response apexLogBodyResponse=null;
		for(int i=0;i<3;i++) {
			Response devloperReqResponse = sfdcRestUtils.getData(urlGenerator.developerConcoleURL+requestBody);
			if(devloperReqResponse.getStatusCode()!=200) throw new ApplicationException(devloperReqResponse.getBody().asString());
			String apexQuery="select id from ApexLog where Request = 'API' AND Operation like '%executeAnonymous%' ORDER By StartTime DESC";
			Response apexLogresponse = this.sfdcRestUtils.getWithQueryParams(this.urlGenerator.queryURL, apexQuery);

			if(apexLogresponse.getStatusCode()!=200) throw new ApplicationException(apexLogresponse.getBody().asString());
			JsonPath path = new JsonPath(apexLogresponse.getBody().asString());
			String apexLogId = path.getString("records[0].Id");
			apexLogBodyResponse = sfdcRestUtils.getData(this.urlGenerator.commonSFDCURL+"ApexLog/"+apexLogId+"/Body");
			if(apexLogBodyResponse.getStatusCode()!=200) throw new ApplicationException(apexLogBodyResponse.getBody().asString());
			if(apexLogBodyResponse.getBody().asString().contains(fetchField)) break;
		}
		return apexLogBodyResponse.getBody().asString().split("EXECUTION_STARTED")[1]
				.split(fetchField)[1]
				.split("\n")[0];
	}
	
	/**
	 * 
	 * @param query Provide full query to execute
	 * @return api Response
	 * @throws Exception
	 */
	public Response searchWithQuery(String query) throws Exception {
		try {
			Response response = this.sfdcRestUtils.getWithQueryParams(this.urlGenerator.queryURL, query);
			if (response.getStatusCode() != 200) {
				throw new ApplicationException(
						"Failure while searcging for with query using the API :"
								+ this.urlGenerator.queryURL + ". The response code was:" + response.getStatusCode()
								+ "and the response body received is: " + response.getBody().asString());
			}
			return response;
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}
	}

}
