
/* This controller class is developed for conducting the performance tests on Hazelcast 
 *   
 */


package com.batria;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.*;
import com.google.gson.JsonArray;

import java.util.Map;
import java.util.List;

import java.io.IOException;
import com.google.gson.Gson;
import com.batria.Connection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;
import org.json.JSONException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;


@RestController
public class RestAPIController {

    private static final Logger logger = LogManager.getLogger(RestAPIController.class);

    private static String LOCAL_FOLDER = "";
    private static String serverIp = "10.128.0.4:5701";

 /* This method is called for getting the order details from the orderMap.
  * The caller needs to call with 'orderId'
  */

    @RequestMapping( method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/hazelcast/order/get")
    public Object getData(@RequestParam(value="orderId", defaultValue="1") String orderId)
    {
         long startTimeMs = System.currentTimeMillis();	
	 HazelcastInstance client = null;
	 String orderData = null;
	 try
	 {
	 	Connection conn = new Connection(serverIp);
	 	client = conn.getClient();
	
		IMap<String, String> mapOrders = client.getMap("ordersMap");
		orderData = mapOrders.get(orderId);

	 }
	 catch(Exception ex)
	 {
		logger.error("Exception while reading from orderMap");
	 }
	 long endTimeMs = System.currentTimeMillis();
         logger.info("GET exec time ms = " + Long.toString(endTimeMs - startTimeMs));
	 return(orderData);
    }

/*    
 * This method accepts order data in JSON format
 *
 */    
    @RequestMapping( method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value = "/hazelcast/order/put")
    public String putData(@RequestBody String inputData) 
    {
          long startTimeMs = System.currentTimeMillis();
	  String returnStatus = "SUCCESS";
	  try
	  {
          	JSONObject jsonObj = new JSONObject(inputData); 
	  	String orderId = (String) jsonObj.get("order_id");
	  	Connection conn = new Connection(serverIp);
	  	HazelcastInstance client = conn.getClient();
	  
	        IMap<String, String> mapOrders = client.getMap("ordersMap");
	      // PUT the data into the map
	        mapOrders.put(orderId, inputData);
	  }
	  catch (Exception ex)
	  {
		  returnStatus = "FAILURE";
		  logger.error("Exception in inserting data");
	  }
         long endTimeMs = System.currentTimeMillis();
         logger.info("POST exec time ms = " + Long.toString(endTimeMs - startTimeMs));
	 return (returnStatus);
    }
}
