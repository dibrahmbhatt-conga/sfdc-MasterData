package com.apttus.sfdc.rudiments.utils;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Properties;

public class GeneralHelper {

	public static synchronized Properties loadPropertyFile(String fileName) throws Exception {
		StackTraceElement packagePath = null;
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		for (int i = 0; i < stacktrace.length; i++) {
			if (stacktrace[i].toString().contains("com.apttus.aic")) {
				packagePath = stacktrace[i];
				break;
			} else if (stacktrace[i].toString().contains("com.apttus.sfdc")) {
				packagePath = stacktrace[i];
				break;
			}
		}
		String[] arrayPackageName = packagePath.toString().split("\\.");
		String packageName = arrayPackageName[3];
		fileName = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator
				+ "resources" + File.separator + packageName + File.separator + fileName;
		File file = new File(fileName);
		FileInputStream fileInput = null;
		fileInput = new FileInputStream(file);
		Properties properties = new Properties(); // load properties file
		properties.load(fileInput);
		return properties;
	}

	public static String getStartDate() {

		DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
		LocalDateTime expectedStartDate = LocalDateTime.now();
		String startDate = formatDate.format(expectedStartDate);
		return startDate;
	}

	public static String getEndDate() {

		DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
		LocalDateTime expectedEndDate = LocalDateTime.now().plusMonths(11);
		String endDate = formatDate.format(expectedEndDate);
		return endDate;
	}

	public static String getReadyForBillingDate() {

		DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
		LocalDateTime readyForBillingDate = LocalDateTime.now().plusDays(1);
		String billingDate = formatDate.format(readyForBillingDate);
		return billingDate;
	}

}
