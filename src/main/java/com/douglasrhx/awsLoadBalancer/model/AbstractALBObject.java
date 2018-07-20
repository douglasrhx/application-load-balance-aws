package com.douglasrhx.awsLoadBalancer.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AbstractALBObject implements Serializable
{
	private static final long serialVersionUID = -6909973417979022286L;

	private String id;
	
	private String name;

	@JsonProperty("properties")
	private AbstractALBObjectProperties objectProperties;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AbstractALBObjectProperties getObjectProperties() {
		return objectProperties;
	}

	public void setObjectProperties(AbstractALBObjectProperties objectProperties) {
		this.objectProperties = objectProperties;
	}
}
