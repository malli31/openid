package com.ca.openid.uimopenid;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.SerializeException;
import com.nimbusds.oauth2.sdk.auth.ClientSecretPost;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.openid.connect.sdk.AuthenticationRequest;
import com.nimbusds.openid.connect.sdk.Nonce;





@RestController
public class MainController {
	
	@Autowired
	 Environment env ;
	  
	
	
	@RequestMapping("/hello")
	    public String homepage(){
	        return "index";
	}
	
	
	@RequestMapping("/oauth")
    public void outhstart(HttpServletResponse httpServletResponse) throws IOException{
		String clientid=env.getProperty("google.client.id");
	    
	    //Getting code authorization code from Google
			
	    String uri="https://accounts.google.com/o/oauth2/v2/auth?"+
	    	 "client_id="+clientid+"&"+
	    	 "response_type=code&"+
	    	// "scope=openid&"+
	    	 "scope=openid%20email%20profile&"+
	    	 "redirect_uri="+env.getProperty("google.redirect.uri")+
	    	// "state=security_token&"+
	    	"&nonce=0394852-3190485-2490358&";
	    System.out.println("uri is "+uri);
	    //https://accounts.google.com/o/oauth2/v2/auth?
	    //client_id=182078948132-u64n4soqj9qq5g99k6hqjj04u6kvgelu.apps.googleusercontent.com&
	    //response_type=code&
	    //scope=openid&
	    //redirect_uri=https://nunma09-e7440.ca.com:8443/oauthredirect&
	    //nonce=0394852-3190485-2490358&
	    	
	    
	  //  RestTemplate restTemplate = new RestTemplate();
	  //String result = restTemplate.getForObject(uri, String.class);
	    
	    httpServletResponse.sendRedirect(uri);
	   //Create an anti-forgery state token
	    
	  //  return result;
       // return "goole cred: "+clientid+"Sec"+clientsec;
}
	
	
	@RequestMapping(value= "/oauthredirect" ,produces = {MediaType.APPLICATION_JSON_VALUE })
    public String oauthredirect(HttpServletRequest request) throws IOException{
		//Forwarding code and gettign access token 
		//output will be access_token,id_token
		String clientid=env.getProperty("google.client.id");
	    String clientsec=env.getProperty("google.client.secret");
		String code = request.getParameter("code");
//		if(code!=null&&code!="")
//			return code;
//        return "Code not  recieved";
		
		
		if(code!=null&&code!="") {
		String token_endpoint="https://www.googleapis.com/oauth2/v4/token";
		
		                  /* 
		                   * post request
		                   * \
		                       "&code"+code+
		                       "&client_id"+clientid+
		                       "&client_secret"+clientsec+
		                       "&redirect_uri="+"https://nunma09-e7440.ca.com:8443/oauthredirect"+
		                       "&grant_type=authorization_code";*/
		  RestTemplate   restTemplate = new RestTemplate();
		  
		  HttpHeaders headers = new HttpHeaders();
		  
		  headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		  MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		  
		  map.add("code", code);
		  map.add("client_id", clientid);
		  map.add("client_secret", clientsec);
		  map.add("redirect_uri", env.getProperty("google.redirect.uri"));
		  map.add("grant_type", "authorization_code");

		  //Get the access token id _token with below post call
		  HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		  ResponseEntity<String> response = restTemplate.postForEntity( token_endpoint, requestEntity , String.class );
		  String body = response.getBody();
		     ObjectMapper mapper1 = new ObjectMapper();		

				Map<String, Object> map1 = new HashMap<String, Object>();				
				map1 = mapper1.readValue(body, new TypeReference<Map<String, String>>(){});
				//Retrive id token from post response by converting to map and decodying encrypt
				return decodeIDTokenJWT((String)map1.get("id_token"));
		  
		//return body;
		}
	return code;	
}
	
	@RequestMapping("/oauthyahoo")
    public void outhstartyahoo(HttpServletResponse httpServletResponse) throws IOException{
		String clientid=env.getProperty("yahoo.client.id");
	    
	    //Getting code authorization code from Google
			
	    String uri="https://api.login.yahoo.com/oauth2/request_auth?"+
	    	 "client_id="+clientid+"&"+
	    	 "response_type=code&"+
	    	// "scope=openid&"+
	    	 "scope=openid&"+
	    	 "redirect_uri="+env.getProperty("yahoo.redirect.uri")+
	    	// "state=security_token&"+
	    	"&nonce=0394852-3190485-24903581&";
	    System.out.println("uri is "+uri);
	    //https://accounts.google.com/o/oauth2/v2/auth?
	    //client_id=182078948132-u64n4soqj9qq5g99k6hqjj04u6kvgelu.apps.googleusercontent.com&
	    //response_type=code&
	    //scope=openid&
	    //redirect_uri=https://nunma09-e7440.ca.com:8443/oauthredirect&
	    //nonce=0394852-3190485-2490358&
	    	
	    
	  //  RestTemplate restTemplate = new RestTemplate();
	  //String result = restTemplate.getForObject(uri, String.class);
	    
	    httpServletResponse.sendRedirect(uri);
	   //Create an anti-forgery state token
	    
	  //  return result;
       // return "goole cred: "+clientid+"Sec"+clientsec;
}
	@RequestMapping(value= "/oauthredirectyahoo" ,produces = {MediaType.APPLICATION_JSON_VALUE })
    public String oauthredirectyahoo(HttpServletRequest request) throws IOException{
		//Forwarding code and gettign access token 
		//output will be access_token,id_token
		String clientid=env.getProperty("yahoo.client.id");
	    String clientsec=env.getProperty("yahoo.client.secret");
		String code = request.getParameter("code");
//		if(code!=null&&code!="")
//			return code;
//        return "Code not  recieved";
		
		
		if(code!=null&&code!="") {
		String token_endpoint="https://api.login.yahoo.com/oauth2/get_token";
		
		                  /* 
		                   * post request
		                   * \
		                       "&code"+code+
		                       "&client_id"+clientid+
		                       "&client_secret"+clientsec+
		                       "&redirect_uri="+"https://nunma09-e7440.ca.com:8443/oauthredirect"+
		                       "&grant_type=authorization_code";*/
		  RestTemplate   restTemplate = new RestTemplate();
		  
		  HttpHeaders headers = new HttpHeaders();
		  
		  headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		  MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		  
		  map.add("code", code);
		  map.add("client_id", clientid);
		  map.add("client_secret", clientsec);
		  map.add("redirect_uri", env.getProperty("yahoo.redirect.uri"));
		  map.add("grant_type", "authorization_code");

		  //Get the access token id _token with below post call
		  HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		  ResponseEntity<String> response = restTemplate.postForEntity( token_endpoint, requestEntity , String.class );
		  String body = response.getBody();
		     ObjectMapper mapper1 = new ObjectMapper();		

				Map<String, Object> map1 = new HashMap<String, Object>();				
				map1 = mapper1.readValue(body, new TypeReference<Map<String, String>>(){});
				//Retrive id token from post response by converting to map and decodying encrypt
				System.out.println("access_tokens "+map1.get("access_token"));
				return decodeIDTokenJWT((String)map1.get("id_token"));
		  
		//return body;
		}
	return code;	
}
	
	@RequestMapping("/oauthautho")
    public void outhstartautho(HttpServletResponse httpServletResponse) throws IOException{
		String clientid=env.getProperty("autho.client.id");
	    
	    //Getting code authorization code from Google
			
	    String uri="https://entity31.auth0.com/authorize?"+
	    	 "client_id="+clientid+"&"+
	    	 "response_type=code&"+
	    	// "scope=openid&"+
	    	 "scope=openid%20profile%20email&"+
	    	 "redirect_uri="+env.getProperty("autho.redirect.uri");
	    System.out.println("uri is "+uri);
	    
	    httpServletResponse.sendRedirect(uri);
}
	
	@RequestMapping(value= "/oauthredirectautho" ,produces = {MediaType.APPLICATION_JSON_VALUE })
    public String oauthredirectautho(HttpServletRequest request) throws IOException{
		//Forwarding code and gettign access token 
		//output will be access_token,id_token
		String clientid=env.getProperty("autho.client.id");
	    String clientsec=env.getProperty("autho.client.secret");
		String code = request.getParameter("code");
//		if(code!=null&&code!="")
//			return code;
//        return "Code not  recieved";
		
		
		if(code!=null&&code!="") {
		String token_endpoint="https://arjunme.auth0.com/oauth/token";
		
		                  /* 
		                   * post request
		                   * \
		                       "&code"+code+
		                       "&client_id"+clientid+
		                       "&client_secret"+clientsec+
		                       "&redirect_uri="+"https://nunma09-e7440.ca.com:8443/oauthredirect"+
		                       "&grant_type=authorization_code";*/
		  RestTemplate   restTemplate = new RestTemplate();
		  
		  HttpHeaders headers = new HttpHeaders();
		  
		  headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		  MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		  
		  map.add("code", code);
		  map.add("client_id", clientid);
		  map.add("client_secret", clientsec);
		  map.add("redirect_uri", env.getProperty("autho.redirect.uri"));
		  map.add("grant_type", "authorization_code");

		  //Get the access token id _token with below post call
		  HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		  ResponseEntity<String> response = restTemplate.postForEntity( token_endpoint, requestEntity , String.class );
		  String body = response.getBody();
		     ObjectMapper mapper1 = new ObjectMapper();		

				Map<String, Object> map1 = new HashMap<String, Object>();				
				map1 = mapper1.readValue(body, new TypeReference<Map<String, String>>(){});
				//Retrive id token from post response by converting to map and decodying encrypt
				System.out.println("access_tokens "+map1.get("access_token"));
				return decodeIDTokenJWT((String)map1.get("id_token"));
		  
		//return body;
		}
	return code;	
}
	
	@RequestMapping("/oauthibmid")
    public void outhstartibmid(HttpServletResponse httpServletResponse) throws IOException{
		String clientid=env.getProperty("ibmid.client.id");
	    
	    //Getting code authorization code from Google
			
	    String uri="https://prepiam.toronto.ca.ibm.com/idaas/oidc/endpoint/default/authorize?"+
	    	 "client_id="+clientid+"&"+
	    	 "response_type=code&"+
	    	"scope=openid&"+
	    	// "scope=openid%20profile%20email&"+
	    	 "redirect_uri="+env.getProperty("ibmid.redirect.uri");
	    System.out.println("uri is "+uri);
	    
	    httpServletResponse.sendRedirect(uri);
}
	
	@RequestMapping(value= "/ibmidredirect" ,produces = {MediaType.APPLICATION_JSON_VALUE })
    public String oauthredirectibmid(HttpServletRequest request,HttpServletResponse httpServletResponse) throws IOException{
		//Forwarding code and gettign access token 
		//output will be access_token,id_token
		String clientid=env.getProperty("ibmid.client.id");
	    String clientsec=env.getProperty("ibmid.client.secret");
		String code = request.getParameter("code");
		
//		if(code!=null&&code!="")
//			return code;
//        return "Code not  recieved";
		
	    String uri="http://nunma09-i17047:8080/ess/oidc/redirect/00F0CBBD-699E-B3FA-8473-3930CB89449B"+
		    	 "code="+code;
		    System.out.println("uri is "+uri);
		    
		    httpServletResponse.sendRedirect(uri);
		
		
		if(code!=null&&code!="") {
		String token_endpoint="https://prepiam.toronto.ca.ibm.com/idaas/oidc/endpoint/default/token";
		
		                  /* 
		                   * post request
		                   * \
		                       "&code"+code+
		                       "&client_id"+clientid+
		                       "&client_secret"+clientsec+
		                       "&redirect_uri="+"https://nunma09-e7440.ca.com:8443/oauthredirect"+
		                       "&grant_type=authorization_code";*/
		  RestTemplate   restTemplate = new RestTemplate();
		  
		  HttpHeaders headers = new HttpHeaders();
		  
		  headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		  MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		  
		  map.add("code", code);
		  map.add("client_id", clientid);
		  map.add("client_secret", clientsec);
		  map.add("redirect_uri", env.getProperty("ibmid.redirect.uri"));
		  map.add("grant_type", "authorization_code");

		  //Get the access token id _token with below post call
		  HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		  ResponseEntity<String> response = restTemplate.postForEntity( token_endpoint, requestEntity , String.class );
		  String body = response.getBody();
		     ObjectMapper mapper1 = new ObjectMapper();		

				Map<String, Object> map1 = new HashMap<String, Object>();				
				map1 = mapper1.readValue(body, new TypeReference<Map<String, String>>(){});
				//Retrive id token from post response by converting to map and decodying encrypt
				System.out.println("access_tokens "+map1.get("access_token"));
				return decodeIDTokenJWT((String)map1.get("id_token"));
		  
		//return body;
		}
	return code;	
}
    public  String  decodeIDTokenJWT(String token){
        String jwtToken = token;
 //       System.out.println("------------ Decode JWT ------------");
        String[] split_string = jwtToken.split("\\.");
        String base64EncodedHeader = split_string[0];
        String base64EncodedBody = split_string[1];
        String base64EncodedSignature = split_string[2];

 //       System.out.println("~~~~~~~~~ JWT Header ~~~~~~~");
        Base64 base64Url = new Base64(true);
        String header = new String(base64Url.decode(base64EncodedHeader));
//        System.out.println("JWT Header : " + header);


 //       System.out.println("~~~~~~~~~ JWT Body ~~~~~~~");
        String body = new String(base64Url.decode(base64EncodedBody));
 //       System.out.println("JWT Body : "+body);   
        return body;
    }
    
    public JsonNode StringTOJson(String res) throws IOException {
    	ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node= objectMapper.readTree(res);
        return node;
    }
    
    @RequestMapping("/oauthnimbus")
   public String Numbusoidc1() throws SerializeException, URISyntaxException {
        ClientID clientId = new ClientID("182078948132-u64n4soqj9qq5g99k6hqjj04u6kvgelu.apps.googleusercontent.com");
        Secret secret = new Secret("ZTXbKfA3PdvgzjNj9SWDSuIl");
        ClientSecretPost clientAuth = new ClientSecretPost(clientId, secret);
        

        URI callback = new URI("https://nunma09-e7440.ca.com/oauthredirect");
		State state;
		AuthenticationRequest req = new AuthenticationRequest(
       		    new URI("https://accounts.google.com/o/oauth2/v2/auth"),
       		    new ResponseType("code"),
       		    Scope.parse("openid"),
       		    clientId,
       		    callback,
       		    new State() ,
       		    new Nonce());

       		//HTTPResponse httpResponse = req.toHTTPRequest().send();
		URI requestURI = req.toURI();
		
		return "hello";

	}
}
