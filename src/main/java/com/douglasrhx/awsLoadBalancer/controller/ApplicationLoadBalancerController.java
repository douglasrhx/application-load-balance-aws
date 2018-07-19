package com.douglasrhx.awsLoadBalancer.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.douglasrhx.awsLoadBalancer.model.TargetGroup;
import com.douglasrhx.awsLoadBalancer.model.TargetGroupProperties;
import com.douglasrhx.awsLoadBalancer.utils.ApplicationLoadBalancerUtils;
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
		String TARGET_GROUP_ROOT_NAME = "pools";
		
		ApplicationLoadBalancerUtils applicationLoadBalancerUtils = new ApplicationLoadBalancerUtils();
		
		TargetGroup targetGroup = new TargetGroup();
		
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		JsonNode node = mapper.readTree(body);
		
		TargetGroupProperties targetGroupProperties = applicationLoadBalancerUtils.recoverTargetGroupPropertiesFromJson(node);
		
		targetGroup = mapper.treeToValue(node.findValue(TARGET_GROUP_ROOT_NAME), TargetGroup.class);
		
		targetGroup.setTargetGroupProperties(targetGroupProperties);
		
		return targetGroup;
	}
}
