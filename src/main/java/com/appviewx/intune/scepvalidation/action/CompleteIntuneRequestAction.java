package com.appviewx.intune.scepvalidation.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.appviewx.intune.scepvalidation.util.IntuneClientUtil;
import com.microsoft.intune.scepvalidation.IntuneScepServiceClient;

@RestController
public class CompleteIntuneRequestAction {

	private IntuneScepServiceClient intuneScepServiceClient;

	private static final Logger LOGGER = LoggerFactory.getLogger(CompleteIntuneRequestAction.class);

	@SuppressWarnings("unchecked")
	@PostMapping("/complete-intune-request")
	protected Map<String, Object> execute(@RequestBody Map<String, Object> request) throws Exception {
		boolean notificationStatus = false;
		Map<String, Object> payload = (Map<String, Object>) request.get("payload");

		Map<String, Object> responseMap = new HashMap<String, Object>();
		String base64Request = (String) payload.get("certificateRequest");
		String transactionId = (String) payload.get("transactionId");
		String intuneClientId = (String) payload.get("intuneClientId");
		String intuneClientSecret = new String(Base64.decodeBase64((String) payload.get("intuneClientSecret")));
		String intuneTenantId = (String) payload.get("intuneTenantId");
		String provider = (String) payload.get("provider");
		String thumbprint = (String) payload.get("thumbprint");
		String serialnumber = (String) payload.get("serialnumber");
		String expiredate = (String) payload.get("expiredate");
		String certificateAuthority = (String) payload.get("certificateAuthority");

		intuneScepServiceClient = new IntuneScepServiceClient(
				IntuneClientUtil.getIntuneProperty(intuneClientId, intuneTenantId, intuneClientSecret, provider));
		try {
			intuneScepServiceClient.SendSuccessNotification(transactionId, base64Request, thumbprint, serialnumber,
					expiredate, certificateAuthority);
			notificationStatus = true;
			LOGGER.info("Success notification sent successfully!");
		} catch (Exception ex) {
			LOGGER.error("Error when sending success notification: {}", ex);
		}
		responseMap.put("response", notificationStatus);
		return responseMap;
	}
}
