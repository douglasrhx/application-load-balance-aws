package com.douglasrhx.awsLoadBalancer.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("pools")
public class TargetGroup implements Serializable
{
	private static final long serialVersionUID = 4658507537282380278L;

	private Integer Id;
	
	private String name;
	
	private TargetGroupProperties targetGroupProperties;

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
	
	public TargetGroupProperties getTargetGroupProperties() {
		return targetGroupProperties;
	}

	public void setTargetGroupProperties(TargetGroupProperties targetGroupProperties) {
		this.targetGroupProperties = targetGroupProperties;
	}
}
