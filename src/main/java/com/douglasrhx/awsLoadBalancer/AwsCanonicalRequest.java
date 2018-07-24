package com.douglasrhx.awsLoadBalancer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class AwsCanonicalRequest 
{
    private String awsAccessKeyID;
    private String awsSecretAccessKey;
    private String regionName;
    private String serviceName;
    private String httpMethod;
    private String canonicalURI;
    private Map<String, String> queryParameters;
    private Map<String, String> awsHeaders;
    private String payload;

    private final String HMACAlgorithm = "AWS4-HMAC-SHA256";
    private final String aws4Request = "aws4_request";
    private String signedHeader;
    
    private String xAmzDate = getTimeStamp();
    private String currentDate = getDate();
    
    public static class Builder 
    {
        private String awsAccessKeyID;
        private String awsSecretAccessKey;
        private String regionName;
        private String serviceName;
        private String httpMethod;
        private String canonicalURI;
        private Map<String, String> queryParameters;
        private Map<String, String> awsHeaders;
        private String payload;

        public Builder(String awsAccessKeyID, String awsSecretAccessKey) {
            this.awsAccessKeyID = awsAccessKeyID;
            this.awsSecretAccessKey = awsSecretAccessKey;
        }

        public Builder regionName(String regionName) {
            this.regionName = regionName;
            return this;
        }

        public Builder serviceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public Builder httpMethod(String httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public Builder canonicalURI(String canonicalURI) {
            this.canonicalURI = canonicalURI;
            return this;
        }

        public Builder queryParameters(Map<String, String> queryParameters) {
            this.queryParameters = queryParameters;
            return this;
        }

        public Builder awsHeaders(Map<String, String> awsHeaders) {
            this.awsHeaders = awsHeaders;
            return this;
        }

        public Builder payload(String payload) {
            this.payload = payload;
            return this;
        }

        public AwsCanonicalRequest build() {
            return new AwsCanonicalRequest(this);
        }
    }
    
    public AwsCanonicalRequest() {	
    }
    
    public AwsCanonicalRequest(Builder builder ) 
    {
		super();
		
		awsAccessKeyID = builder.awsAccessKeyID;
		awsSecretAccessKey = builder.awsSecretAccessKey;
		regionName = builder.regionName;
		serviceName = builder.serviceName;
		httpMethod = builder.httpMethod;
		canonicalURI = builder.canonicalURI;
		queryParameters = builder.queryParameters;
		awsHeaders = builder.awsHeaders;
		payload = builder.payload;
	}

	//Task 1
    private String prepareCanonicalRequest() 
    {
    	/* 1.1 */
        StringBuilder canonicalURL = new StringBuilder("");

        canonicalURL.append(httpMethod).append("\n");

        /* 1.2*/
        canonicalURI = canonicalURI == null || canonicalURI.trim().isEmpty() ? "/" : canonicalURI;   
        canonicalURL.append(canonicalURI).append("\n");

        /* 1.3 */
        StringBuilder queryString = new StringBuilder("");
        
        if (queryParameters != null && !queryParameters.isEmpty()) 
        {
            for (Map.Entry<String, String> entrySet : queryParameters.entrySet()) 
            {
                String key = entrySet.getKey();
                String value = entrySet.getValue();
                queryString.append(key).append("=").append(encodeParameter(value)).append("&");
            }

            queryString.deleteCharAt(queryString.lastIndexOf("&"));

            canonicalURL.append(queryString).append("\n");
            
        } else {
            queryString.append("\n");
        }
        
        /* 1.4 */
        StringBuilder signedHeaders = new StringBuilder("");
        
        if (awsHeaders != null && !awsHeaders.isEmpty()) 
        {
            for (Map.Entry<String, String> entrySet : awsHeaders.entrySet())
            {
                String key = entrySet.getKey().toLowerCase();
                String value = entrySet.getValue().toLowerCase();
                
                signedHeaders.append(key).append(";");
                canonicalURL.append(key).append(":").append(value).append("\n");
            }

            canonicalURL.append("\n");
            
        } else {
            canonicalURL.append("\n");
        }

        /* 1.5 */
        signedHeader = signedHeaders.substring(0, signedHeaders.length() - 1);
        
        canonicalURL.append(signedHeader).append("\n");

        /* 1.6 */
        payload = payload == null ? "" : payload;
        
        canonicalURL.append(generateHex(payload));
        
        System.out.println("\n###### Canonical Request ######\n" + canonicalURL.toString());

        return canonicalURL.toString();
    }

	//Task 2
    private String prepareStringToSign(String canonicalURL) 
    {
        String stringToSign = "";
        
        /* 2.1 */
        stringToSign = HMACAlgorithm + "\n";

        /* 2.2 */
        stringToSign += xAmzDate + "\n";
        
        /* 2.3 */
        stringToSign += currentDate + "/" + regionName + "/" + serviceName + "/" + aws4Request + "\n";

        /* 1.8 */
        stringToSign += generateHex(canonicalURL);
        
        System.out.println("\n###### String to Sign ######\n" + stringToSign);

        return stringToSign;
    }

    //Task 3
    private String calculateSignature(String stringToSign) 
    {
        try 
        {
        	/* 3.1 */
            byte[] signatureKey = getSignatureKey(awsSecretAccessKey, currentDate, regionName, serviceName);

            /* 3.2 */
            byte[] signature = HmacSHA256(signatureKey, stringToSign);

            String hexSignature = convertBytesToHex(signature);
            
            System.out.println("\n###### Signature ######\n" + hexSignature);
            
            return hexSignature;
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return null;
    }

    //Task 4
    public Map<String, String> prepareAuthorizationHeader() 
    {
        awsHeaders.put("x-amz-date", xAmzDate);

        String canonicalURL = prepareCanonicalRequest();

        String stringToSign = prepareStringToSign(canonicalURL);

        String signature = calculateSignature(stringToSign);
        
        Map<String, String> authorizationHeader = new HashMap<String, String>();
        
        authorizationHeader.put("x-amz-date", xAmzDate);
        authorizationHeader.put("Authorization", buildAuthorizationString(signature));

        System.out.println("\n###### Header for Request ######\n");
        
        for (Map.Entry<String, String> entrySet : authorizationHeader.entrySet()) 
        {
        	System.out.println(entrySet.getKey() + " = " + entrySet.getValue());
        }
        
        return authorizationHeader;
        
    }

    private String buildAuthorizationString(String strSignature)
    {
        return HMACAlgorithm + " "
                + "Credential=" + awsAccessKeyID + "/" + getDate() + "/" 
        		+ regionName + "/" + serviceName + "/" + aws4Request + ","
                + "SignedHeaders=" + signedHeader + ","
                + "Signature=" + strSignature;
    }

    private String generateHex(String data) 
    {
        MessageDigest messageDigest;
        
        try 
        {
            messageDigest = MessageDigest.getInstance("SHA-256");
            
            messageDigest.update(data.getBytes("UTF-8"));
            
            byte[] digest = messageDigest.digest();
            
            return String.format("%064x", new java.math.BigInteger(1, digest));
            
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    private byte[] HmacSHA256(byte[] key, String data) throws Exception 
    {
        String algorithm = "HmacSHA256";
        Mac mac = Mac.getInstance(algorithm);
        
        mac.init(new SecretKeySpec(key, algorithm));
        
        return mac.doFinal(data.getBytes("UTF8"));
    }

    private byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws Exception 
    {
        byte[] kSecret = ("AWS4" + key).getBytes("UTF8");
        byte[] kDate = HmacSHA256(kSecret, dateStamp);
        byte[] kRegion = HmacSHA256(kDate, regionName);
        byte[] kService = HmacSHA256(kRegion, serviceName);
        byte[] kSigning = HmacSHA256(kService, aws4Request);
        return kSigning;
    }

    private String convertBytesToHex(byte[] bytes)
    {
    	String hex = DatatypeConverter.printHexBinary(bytes);
    	
    	return hex.toLowerCase();
    }

    private String getTimeStamp() 
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        return dateFormat.format(new Date());
    }

    private String getDate() 
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        return dateFormat.format(new Date());
    }

	private String encodeParameter(String param) 
	{
		try 
		{
			return URLEncoder.encode(param, "UTF-8");
			
		} catch (UnsupportedEncodingException e)  {
			e.printStackTrace();
		}
		
		return null;
	}
}
