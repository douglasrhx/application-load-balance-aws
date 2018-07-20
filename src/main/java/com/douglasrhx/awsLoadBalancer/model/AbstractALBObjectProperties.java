package com.douglasrhx.awsLoadBalancer.model;

import java.io.Serializable;
import java.util.Map;

public class AbstractALBObjectProperties implements Serializable
{
	private static final long serialVersionUID = 1510497250730884059L;
	
	private Map<String, String> properties;

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
}
