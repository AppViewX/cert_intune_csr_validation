package com.appviewx.intune.scepvalidation.util;

import java.util.Properties;

public final class IntuneClientUtil {
	
	private IntuneClientUtil() {
		
	}

	public static Properties getIntuneProperty(String intuneClientId, String intuneTenantId, 
			String intuneClientSecret, String provider) {
		Properties properties = new Properties();
		properties.setProperty("AAD_APP_ID", intuneClientId);
		properties.setProperty("AAD_APP_KEY", intuneClientSecret);
		properties.setProperty("TENANT", intuneTenantId);
		properties.setProperty("PROVIDER_NAME_AND_VERSION", provider);
		return properties;
	}
}
