package com.apttus.sfdc.masterDataCpq;

import com.apttus.customException.ApplicationException;

public class ABOTests {
	public boolean createData() throws Exception {
		try {
			SetFactory.createProduct();
			SetFactory.createAccount();
			return true;
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}
	}
}