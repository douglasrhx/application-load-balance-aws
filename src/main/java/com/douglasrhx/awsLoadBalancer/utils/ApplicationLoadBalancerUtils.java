package com.douglasrhx.awsLoadBalancer.utils;

import java.io.IOException;

import com.douglasrhx.awsLoadBalancer.model.TargetGroup;
import com.douglasrhx.awsLoadBalancer.model.TargetGroupProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApplicationLoadBalancerUtils 
{
	public TargetGroup recoverTargetGroupFromJson(String body, String objectRootNameInJson) throws IOException
	{
		String targetGroupJson = extractJsonObjectFromBody(body, objectRootNameInJson);
				
		TargetGroup targetGroup = new TargetGroup();
		
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		JsonNode node = mapper.readTree(targetGroupJson);
		
		targetGroup = mapper.readValue(node.toString(), TargetGroup.class);
		
		TargetGroupProperties targetGroupProperties = new TargetGroupProperties();
		
		targetGroupProperties = mapper.treeToValue(node.findParent("properties"), TargetGroupProperties.class);
		
		targetGroup.setTargetGroupProperties(targetGroupProperties);
		
		return targetGroup;
	}
	
	private String extractJsonObjectFromBody(String body, String objectRootNameInJson) throws IOException
	{
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		JsonNode node = mapper.readTree(body);
		
		String objectJson = node.findValue(objectRootNameInJson).toString();
		
		return objectJson;
	}
}