package com.douglasrhx.awsLoadBalancer.utils;

import java.util.HashMap;
import java.util.Map;

import com.douglasrhx.awsLoadBalancer.model.TargetGroupProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApplicationLoadBalancerUtils 
{
	@SuppressWarnings("unchecked")
	public TargetGroupProperties recoverTargetGroupPropertiesFromJson(JsonNode node) throws JsonProcessingException
	{
		Map<String, String> properties = new HashMap<String, String>();
		
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		properties = mapper.treeToValue(node.findValue("properties"), HashMap.class);
		
		TargetGroupProperties targetGroupProperties = new TargetGroupProperties();
		
		targetGroupProperties.setProperties(properties);
		
		return targetGroupProperties;
	}
}
