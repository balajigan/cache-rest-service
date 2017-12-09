

package com.batria;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
//import com.test.springboot.Sales;
//import com.test.springboot.SalesTrend;
import java.util.*;
import com.google.gson.JsonArray;
//import net.sf.json.JSONException;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import org.codehaus.jackson.map.ObjectMapper;
import java.io.IOException;
import com.google.gson.Gson;
import com.batria.Connection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;
import org.json.JSONException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;



@RestController
public class RestAPIController {

    //private static final Logger logger = LogManager.getLogger("appLogger");

    @RequestMapping("/dashboard")
    public String index() {
        return "Home Page is here";
    }

    @RequestMapping( method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/cassandra/order/get")
    public Object getData(@RequestParam(value="orderId", defaultValue="1") String orderId)
    {
//	 logger.info("getData is called");
         long startTimeMs = System.currentTimeMillis();	

	 Connection conn = new Connection();
	 Session session = conn.getSession();
         ResultSet resultSet = session.execute("SELECT JSON * FROM test.orders WHERE order_id="+ "'"+orderId+"'");

         Row row = resultSet.one();
         String jsonString = row.getString(0);
	 
	 long endTimeMs = System.currentTimeMillis();
	 System.out.println("getData exec time = "+ Long.toString(endTimeMs - startTimeMs));

         return(jsonString);
    }

/*    
 * This method accepts order data in JSON format
 *
 */    
    @RequestMapping( method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value = "/cassandra/order/put")
    public String putData(@RequestBody String inputData) 
    {
          long startTimeMs = System.currentTimeMillis(); 

          System.out.println("Received String = " + inputData);
	  Connection conn = new Connection();
	  Session session = conn.getSession();
	  try
	  {
	      session.execute("INSERT INTO test.orders JSON " + "'" + inputData + "'");
	  }
	  catch (Exception ex)
	  {
		  System.out.println("Exception");
	  }
         long endTimeMs = System.currentTimeMillis();
	 System.out.println("putData exec time = "+ Long.toString(endTimeMs - startTimeMs));

	  return (inputData);
    }
}
