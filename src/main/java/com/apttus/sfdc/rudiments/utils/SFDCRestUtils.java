package com.apttus.sfdc.rudiments.utils;

import java.util.Map;
import java.util.Properties;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

public class SFDCRestUtils {

	private String accessToken;
	private String instanceURL;
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return this.accessToken;
	}
	
	public String getInstanceURL() {
		return instanceURL;
	}

	public void setInstanceURL(String instanceURL) {
		this.instanceURL = instanceURL;
	}
	
	/**
	 * 
	 * @param testData Provide the valid testData as follows: client_id,
	 * client_secret,username,password,url
	 * @return Instance URL for the SFDC
	 */
	public String generateSFDCAccessToken(Map<String, String> testData) {
		Response response = RestAssured.given()
				.formParam("grant_type", "password")
				.formParam("client_id", testData.get("client_id"))
				.formParam("client_secret", testData.get("client_secret"))
				.formParam("username", testData.get("username"))
				.formParam("password", testData.get("password"))
				.post(testData.get("url"));
		JsonPath path = new JsonPath(response.getBody().asString());
		String token = path.get("access_token");
		setAccessToken(token);
		return path.get("instance_url");
	}
	
	/**
	 * 
	 * @param prop Provide the valid Properties as follows: client_id,
	 * client_secret,username,password,url
	 * @return Instance URL for the SFDC
	 */
	public String generateSFDCAccessToken(Properties prop) {
		Response response = RestAssured.given()
				.formParam("grant_type", "password")
				.formParam("client_id", prop.getProperty("client_id"))
				.formParam("client_secret", prop.getProperty("client_secret"))
				.formParam("username", prop.getProperty("username"))
				.formParam("password", prop.getProperty("password"))
				.post(prop.getProperty("url"));
		JsonPath path = new JsonPath(response.getBody().asString());
		String token = path.get("access_token");
		setAccessToken(token);
		return path.get("instance_url");
	}
	
	/**
	 * Fetches the data
	 * @param url Provide the APIURI
	 * @return response
	 */
	public Response getData(String url) {
		return RestAssured.given()
				.header("Authorization", "Bearer " + accessToken)
				.header("Content-Type", "application/json")
				.get(url);
	}
	
	/**
	 * Fetches the data
	 * @param url Provide the APIURI
	 * @param queryParams Provide the SQL Query
	 * @return response
	 */
	public Response getWithQueryParams(String url, String queryParams) {
		return RestAssured.given()
				.header("Authorization", "Bearer " + accessToken)
				.header("Content-Type", "application/json")
				.queryParam("q",queryParams)
				.get(url);
	}

	/**
	 * Creates the data with payload
	 * @param url Provide the APIURI
	 * @param body Provide the Request Body
	 * @return response
	 */
	public Response postWithoutAppUrl(String url, String body) {
		return RestAssured.given()
				.header("Authorization", "Bearer " + accessToken)
				.header("Content-Type", "application/json")
				.body(body)
				.post(url);
	}
	
	/**
	 * Updates the data with payload
	 * @param url Provide the APIURI
	 * @param body Provide the Request Body
	 * @return response
	 */
	public Response putWithBody(String url, String body) {
		return RestAssured.given()
				.header("Authorization", "Bearer " + accessToken)
				.header("Content-Type", "application/json")
				.body(body)
				.put(url);
	}
	
	/**
	 * Updates the data with payload
	 * @param url Provide the APIURI
	 * @param body Provide the Request Body
	 * @return response
	 */
	public Response patchWithoutAppUrl(String url, String body) {
		return RestAssured.given()
				.header("Authorization", "Bearer " + accessToken)
				.header("Content-Type", "application/json")
				.body(body)
				.patch(url);
	}
	
	/**
	 * Deletes the data without payload
	 * @param url Provide the APIURI
	 * @return response
	 */
	public Response deleteWithoutPayload(String url) {
		return RestAssured.given()
				.header("Authorization", "Bearer " + accessToken)
				.header("Content-Type", "application/json")
				.delete(url);
	}

}
