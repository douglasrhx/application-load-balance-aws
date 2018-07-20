package com.douglasrhx.awsLoadBalancer.controller;

import java.util.List;
import java.util.Map;

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
		Map<String, String> mapObjectRootNameAction = ApplicationLoadBalancerUtils.createMapObjectRootNameAction();
		
		ApplicationLoadBalancerUtils applicationLoadBalancerUtils = new ApplicationLoadBalancerUtils();
		
		ApplicationLoadBalancerRequest applicationLoadBalancerRequest = new ApplicationLoadBalancerRequest();
		
		for (Map.Entry<String, String> object : mapObjectRootNameAction.entrySet()) 
		{
			String OBJECT_ROOT_NAME = object.getKey();
			
			List<AbstractALBObject> abstractALBObjects = applicationLoadBalancerUtils.recoverObjectFromJson(body, OBJECT_ROOT_NAME);
			
			for (AbstractALBObject abstractALBObject : abstractALBObjects) 
			{
				applicationLoadBalancerRequest.createObjectALB(abstractALBObject, object.getValue());
			}
		}
	}
}
