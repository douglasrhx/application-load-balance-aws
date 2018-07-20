package com.douglasrhx.awsLoadBalancer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.douglasrhx.awsLoadBalancer.model.AbstractALBObject;

public class ApplicationLoadBalancerRequest 
{
	public void createObjectALB(AbstractALBObject abstractALBObject, String action) throws Exception
	{		
		String url = prepareURLCreateAction(abstractALBObject, action);

		URL obj = new URL(url);
		
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		int responseCode = con.getResponseCode();
		
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) 
		{
			response.append(inputLine);
		}
		in.close();

		System.out.println(response.toString());
	}
	
	private String prepareURLCreateAction(AbstractALBObject abstractALBObject, String action)
	{
		String urlWithoutProperties = "elasticloadbalancing.amazonaws.com/?Action=" + action + 
				"&Name=" + abstractALBObject.getName();
		
		Map<String, String> properties = abstractALBObject.getObjectProperties().getProperties();
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		
	    for (Entry<String, String> entry : properties.entrySet()) 
	    {
	        params.add(entry.getKey(), entry.getValue());
	    }

	    UriComponents uriComponents = UriComponentsBuilder.newInstance()
	            .scheme("https").host(urlWithoutProperties)
	            .queryParams(params).build();
	    
	    return uriComponents.toUriString();	    
	}
}
