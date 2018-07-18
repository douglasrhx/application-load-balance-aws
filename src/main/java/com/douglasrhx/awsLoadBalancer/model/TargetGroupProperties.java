package com.douglasrhx.awsLoadBalancer.model;

import java.io.Serializable;
import java.util.Map;

public class TargetGroupProperties implements Serializable
{
	private static final long serialVersionUID = -6908282873374231009L;
	
	private Map<String, String> properties;

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
}
