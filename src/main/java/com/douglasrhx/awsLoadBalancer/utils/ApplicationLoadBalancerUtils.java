package com.douglasrhx.awsLoadBalancer.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglasrhx.awsLoadBalancer.model.AbstractALBObject;
import com.douglasrhx.awsLoadBalancer.model.AbstractALBObjectProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApplicationLoadBalancerUtils 
{
	public List<AbstractALBObject> recoverObjectFromJson(String body, String objectRootNameInJson) throws Exception
	{
		String objectJson = extractJsonObjectFromBody(body, objectRootNameInJson);

		List<AbstractALBObject> abstractALBObjects = recoverAbstractALBObjectFromJson(objectJson);
				
		return abstractALBObjects;
	}
	
	private List<AbstractALBObject> recoverAbstractALBObjectFromJson(String objectJson) throws Exception
	{				
		List<AbstractALBObject> abstractALBObjects = new ArrayList<AbstractALBObject>();
		
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		JsonNode node = mapper.readTree(objectJson);
				
		List<JsonNode> listOfNodes = node.findParents("id");

		for (JsonNode jsonNode : listOfNodes) 
		{
			AbstractALBObject abstractALBObject = new AbstractALBObject();
			
			abstractALBObject = mapper.readValue(jsonNode.toString(), AbstractALBObject.class);
			
			AbstractALBObjectProperties objectProperties = new AbstractALBObjectProperties();
			
			objectProperties = mapper.treeToValue(jsonNode.findParent("properties"), AbstractALBObjectProperties.class);
			
			abstractALBObject.setObjectProperties(objectProperties);
			
			abstractALBObjects.add(abstractALBObject);
		}
		
		return abstractALBObjects;
	}
	
	private String extractJsonObjectFromBody(String body, String objectRootNameInJson) throws IOException
	{
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		JsonNode node = mapper.readTree(body);
		
		System.out.println("Key: " + objectRootNameInJson + "." + node);
		
		String objectJson = node.findValue(objectRootNameInJson).toString();
		
		return objectJson;
	}

	public static Map<String, String> createMapObjectRootNameAction() 
	{
		Map<String, String> objectRootNameAction = new HashMap<String, String>();
		
		objectRootNameAction.put("pools", "CreateTargetGroup");
		objectRootNameAction.put("targets", "RegisterTargets");
		objectRootNameAction.put("rules", "CreateRule");
		
		return objectRootNameAction;
	}
}