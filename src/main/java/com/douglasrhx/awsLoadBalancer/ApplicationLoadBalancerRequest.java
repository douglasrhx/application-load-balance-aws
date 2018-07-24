package com.douglasrhx.awsLoadBalancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.douglasrhx.awsLoadBalancer.model.AbstractALBObject;

public class ApplicationLoadBalancerRequest 
{
	private String awsAccessKeyID;
	private String awsSecretAccessKey;
	private String regionName;
	private String serviceName;
	private String httpMethod;
	private String canonicalURI;
	private String host;
	
	public void createObjectALB(AbstractALBObject abstractALBObject, String action) throws Exception 
	{		
		prepareAuthorizationParameters();
		
		String url = prepareURLWithQueryString(abstractALBObject, action);
		
		Map<String, String> awsHeaders = new TreeMap<String, String>();

		awsHeaders.put("content-type", "application/x-www-form-urlencode");
		awsHeaders.put("host", host);
		
		AwsCanonicalRequest awsCanonicalRequest = new AwsCanonicalRequest.Builder(awsAccessKeyID, awsSecretAccessKey)
					                .regionName(regionName)
					                .serviceName(serviceName)
					                .httpMethod(httpMethod)
					                .canonicalURI(canonicalURI)
					                .queryParameters(null)
					                .awsHeaders(awsHeaders)
					                .payload(null)
					                .build();

		Map<String, String> header = awsCanonicalRequest.prepareAuthorizationHeader();

		HttpGet httpGet = new HttpGet(url);

		for (Map.Entry<String, String> entrySet : header.entrySet())
		{
			httpGet.addHeader(entrySet.getKey(), entrySet.getValue());
		}

		HttpClient httpClient = HttpClientBuilder.create().build();

		HttpResponse response = httpClient.execute(httpGet);

		System.out.println("\n=====> Sending requesst to URL: " + url);
		System.out.println("\n=====> Executing request... ");
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

		BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String inputLine;
		StringBuffer result = new StringBuffer();

		while ((inputLine = in.readLine()) != null)
		{
			result.append(inputLine);
		}
		
		in.close();

		System.out.println(result.toString());
	}
	
	public String prepareURLWithQueryString(AbstractALBObject abstractALBObject, String action)
	{
		StringBuilder url = new StringBuilder();
		
		url.append("https://" + host + "/?Action=" + action);
		
		Map<String, String> properties = abstractALBObject.getObjectProperties().getProperties();
		
		for (Map.Entry<String,String> entrySet : properties.entrySet()) 
		{
			url.append("&").append(entrySet.getKey()).append("=").append(entrySet.getValue());
		}
		
		return url.toString();
	}
	
	private void prepareAuthorizationParameters() throws IOException
	{
		List<String> lines = Files.readAllLines(Paths.get("src/main/resources/data.txt"));
		
		awsAccessKeyID = lines.get(0);
		awsSecretAccessKey = lines.get(1);
		regionName = lines.get(2);
		serviceName = lines.get(3);
		httpMethod = lines.get(4);
		canonicalURI = lines.get(5);
		host = lines.get(6);
	}
}
