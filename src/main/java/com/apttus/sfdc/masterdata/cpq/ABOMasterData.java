package com.apttus.sfdc.masterdata.cpq;

import com.apttus.customException.ApplicationException;
import com.apttus.sfdc.masterdata.objectfactory.CPQCommonData.objects;
import com.apttus.sfdc.masterdata.generator.Runner;
public class ABOMasterData {
	public boolean upsertData() throws Exception {
		try {
			Runner.setObjectsFactory.upsertData(objects.Account);
			return true;
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}
	}
}