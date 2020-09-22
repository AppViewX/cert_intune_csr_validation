package com.appviewx.intune.scepvalidation.validator;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.appviewx.intune.scepvalidation.util.IntuneClientUtil;
import com.microsoft.intune.scepvalidation.IntuneScepServiceClient;
import com.microsoft.intune.scepvalidation.IntuneScepServiceException;

@Service
public class IntuneRequestValidator {
	
	private IntuneScepServiceClient intuneScepServiceClient;

	private static final long HRESULT = 0x8000ffff;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IntuneRequestValidator.class);
	
	@SuppressWarnings("unchecked")
	public boolean validate(Map<String, Object> request) {
		boolean isValid = false; 
		String base64Request = "";
		String transactionId = "";
		try {
			Map<String, Object> payload = (Map<String, Object>) request.get("payload");
			base64Request = (String) payload.get("certificateRequest");
			transactionId = (String) payload.get("transactionId");
			String intuneClientId = (String) payload.get("intuneClientId");
			String intuneClientSecret = new String(Base64.decodeBase64((String) payload.get("intuneClientSecret")));
			String intuneTenantId = (String) payload.get("intuneTenantId");
			String provider = (String) payload.get("provider");

			intuneScepServiceClient = new IntuneScepServiceClient(
					IntuneClientUtil.getIntuneProperty(intuneClientId, intuneTenantId, intuneClientSecret, provider));

			intuneScepServiceClient.ValidateRequest(transactionId, base64Request);
			isValid = true;
		} catch (IntuneScepServiceException e) {
			LOGGER.error("Request is not valid: {}", e);
			try {
				intuneScepServiceClient.SendFailureNotification(transactionId, base64Request, HRESULT,
						"CSR Failed validation: " + e.getMessage());
			} catch (Exception e2) {
				LOGGER.error("Error while sending failure notification: {}", e.getMessage());
			}
		} catch (Exception e) {
			LOGGER.error("Error when validating request: {}", e);
			try {
				intuneScepServiceClient.SendFailureNotification(transactionId, base64Request, HRESULT,
						"Error when validating CSR: " + e.getMessage());
			} catch (Exception ex) {
				LOGGER.error("Error while sending failure notification: {}", ex);
			}
		}
		return isValid;
	}
}
