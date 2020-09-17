package com.appviewx.intune.scepvalidation.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.appviewx.intune.scepvalidation.validator.IntuneRequestValidator;

@RestController
public class ValidateIntuneRequestAction {

	@Autowired
	private IntuneRequestValidator requestValidator;

	@PostMapping("/validate-intune-request")
	protected Map<String, Object> execute(@RequestBody Map<String, Object> payload) throws Exception {

		Map<String, Object> responseMap = new HashMap();
		
		responseMap.put("response", requestValidator.validate(payload));
		return responseMap;
	}

}
