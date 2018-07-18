package com.douglasrhx.awsLoadBalancer.model;

import java.io.Serializable;
import java.util.List;

public class TargetGroupProperties implements Serializable
{
	private static final long serialVersionUID = -6908282873374231009L;
	
	private List<String> properties;

	public List<String> getProperties() {
		return properties;
	}

	public void setProperties(List<String> properties) {
		this.properties = properties;
	}
}
