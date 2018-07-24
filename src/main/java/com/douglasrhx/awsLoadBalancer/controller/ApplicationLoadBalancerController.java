package com.douglasrhx.awsLoadBalancer.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.douglasrhx.awsLoadBalancer.ApplicationLoadBalancerRequest;
import com.douglasrhx.awsLoadBalancer.model.AbstractALBObject;
import com.douglasrhx.awsLoadBalancer.utils.ApplicationLoadBalancerUtils;

@RequestMapping(value = "/aws")
@RestController
public class ApplicationLoadBalancerController 
{
	@RequestMapping(value = "/loadBalancer", method = RequestMethod.POST)
	@ResponseBody
	public void createApplicationLoadBalancer(@RequestBody String body) throws Exception 
	{
		createRequests(body);
	}

	private void createRequests(String body) throws Exception 
	{
		String TARGET_GROUP_ROOT_NAME = "pools";
		String TARGET_ROOT_NAME = "target";
		String RULE_ROOT_NAME = "rule";
		String LOAD_BALANCER_ROOT_NAME = "loadBalancer";
		
		ApplicationLoadBalancerUtils applicationLoadBalancerUtils = new ApplicationLoadBalancerUtils();
		ApplicationLoadBalancerRequest applicationLoadBalancerRequest = new ApplicationLoadBalancerRequest();

		AbstractALBObject targetGroup = applicationLoadBalancerUtils.recoverObjectFromJson(body, TARGET_GROUP_ROOT_NAME);
		applicationLoadBalancerRequest.createObjectALB(targetGroup, "CreateTargetGroup");
		
		AbstractALBObject target = applicationLoadBalancerUtils.recoverObjectFromJson(body, TARGET_ROOT_NAME);
		applicationLoadBalancerRequest.createObjectALB(target, "RegisterTargets");
		
		AbstractALBObject rule = applicationLoadBalancerUtils.recoverObjectFromJson(body, RULE_ROOT_NAME);
		applicationLoadBalancerRequest.createObjectALB(rule, "CreateRule");
		
		AbstractALBObject loadBalancer = applicationLoadBalancerUtils.recoverObjectFromJson(body, LOAD_BALANCER_ROOT_NAME);
		applicationLoadBalancerRequest.createObjectALB(loadBalancer, "CreateLoadbalancer");
	}
}
