package com.douglasrhx.awsLoadBalancer.utils;

import java.io.IOException;

import com.douglasrhx.awsLoadBalancer.model.AbstractALBObject;
import com.douglasrhx.awsLoadBalancer.model.AbstractALBObjectProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApplicationLoadBalancerUtils 
{
	public AbstractALBObject recoverObjectFromJson(String body, String objectRootNameInJson) throws Exception
	{
		String objectJson = extractJsonObjectFromBody(body, objectRootNameInJson);
		
		AbstractALBObject abstractALBObject = recoverAbstractALBObjectFromJson(objectJson);
		
		return abstractALBObject;
	}
	
	private AbstractALBObject recoverAbstractALBObjectFromJson(String objectJson) throws Exception
	{
		AbstractALBObject abstractALBObject;
		
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		JsonNode node = mapper.readTree(objectJson);
		
		abstractALBObject = mapper.readValue(node.toString(), AbstractALBObject.class);
		
		AbstractALBObjectProperties objectProperties = new AbstractALBObjectProperties();
		
		objectProperties = mapper.treeToValue(node.findParent("properties"), AbstractALBObjectProperties.class);
		
		abstractALBObject.setObjectProperties(objectProperties);
		
		return abstractALBObject;
	}
	
	private String extractJsonObjectFromBody(String body, String objectRootNameInJson) throws IOException
	{
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		JsonNode node = mapper.readTree(body);
		
		String objectJson = node.findValue(objectRootNameInJson).toString();
		
		return objectJson;
	}
}