package com.douglasrhx.awsLoadBalancer.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.douglasrhx.awsLoadBalancer.model.TargetGroup;
import com.douglasrhx.awsLoadBalancer.model.TargetGroupProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping(value = "/aws")
@RestController
public class ApplicationLoadBalancerController 
{	
	@RequestMapping(value = "/targetGroup", method = RequestMethod.POST)
	@ResponseBody
	public TargetGroup postTargetGroup(@RequestBody String body) throws IOException
	{
		TargetGroup targetGroup = new TargetGroup();
		
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		JsonNode node = mapper.readTree(body);
		
		TargetGroupProperties targetGroupProperties = recoverTargetGroupPropertiesFromJson(node);
		
		targetGroup = mapper.treeToValue(node.findValue("pools"), TargetGroup.class);
		
		targetGroup.setTargetGroupProperties(targetGroupProperties);
		
		return targetGroup;
	}
	
	@SuppressWarnings("unchecked")
	private TargetGroupProperties recoverTargetGroupPropertiesFromJson(JsonNode node) throws JsonProcessingException
	{
		Map<String, String> properties = new HashMap<String, String>();
		
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		properties = mapper.treeToValue(node.findValue("properties"), HashMap.class);
		
		TargetGroupProperties targetGroupProperties = new TargetGroupProperties();
		
		targetGroupProperties.setProperties(properties);
		
		return targetGroupProperties;
	}
}
