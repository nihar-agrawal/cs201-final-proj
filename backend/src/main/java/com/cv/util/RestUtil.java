package com.cv.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class RestUtil {
  public enum REQUEST_TYPE {
    POST,
    GET
  };

  private RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(15 * 1000).build();
  private HttpClient client ; //= HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();

  protected HttpPost generatePost(String endpoint) {
    throw new IllegalStateException("No default implementation for RestUtil.generatePost!");
  }

  protected HttpGet generateGet(String endpoint) {
    throw new IllegalStateException("No default implementation for RestUtil.generateGet!");
  }

  public JsonObject post(String endpoint) {
    return makeRequest(endpoint, REQUEST_TYPE.POST);
  }
  
  public JsonObject get(String endpoint) {
    System.out.println("GET: " + endpoint);
    return makeRequest(endpoint, REQUEST_TYPE.GET);
  }

  public JsonObject makeRequest(String endpoint, REQUEST_TYPE type) {
    try {
      HttpUriRequest uriRequest = null;
      if (type == REQUEST_TYPE.POST) {
        uriRequest = generatePost(endpoint);
      }
      else if (type == REQUEST_TYPE.GET) { 
        uriRequest = generateGet(endpoint); //cryptowatch API currently (for nihar)
      }
      else {
        throw new IllegalArgumentException("Invalid request type " + type);
      }

      //Nihar -- added
      this.client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
      System.out.println("Executing");
      HttpResponse response = client.execute(uriRequest);
      System.out.println("Done executing");
      
      if (response.getStatusLine().getStatusCode() == 200) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        StringBuilder builder = new StringBuilder();
        for (String line = null; (line = reader.readLine()) != null;) {
          builder.append(line).append("\n");
        }

        String responseStr = builder.toString();
        JsonObject convertedObject = new Gson().fromJson(responseStr, JsonObject.class);
        
        //Delete later
//        System.out.println("Received response in RestUtil: Object Received:");
//        System.out.println(convertedObject);

        return convertedObject;
        
      }
      else {
        return null;
      }
    }
    catch (Exception e) {
      return null;
    }
  }

}
