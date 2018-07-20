package com.douglasrhx.awsLoadBalancer.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.douglasrhx.awsLoadBalancer.model.TargetGroup;
import com.douglasrhx.awsLoadBalancer.utils.ApplicationLoadBalancerUtils;

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
		
		TargetGroup targetGroup = applicationLoadBalancerUtils.recoverTargetGroupFromJson(body, TARGET_GROUP_ROOT_NAME);
		
		return targetGroup;
	}
}
