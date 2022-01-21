package com.ngx.cards.model;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class Properties {

	private String msg;
	private String buildVersion;
	private Map<String,String> mailDetails;
	private List<String> activeBranches;
	
	public Properties(String msg, String buildVersion, Map<String, String> mailDetails, List<String> activeBranches) {
		this.msg = msg;
		this.buildVersion = buildVersion;
		this.mailDetails = mailDetails;
		this.activeBranches = activeBranches;
	}

	
}
