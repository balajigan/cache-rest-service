

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



@RestController
public class RestAPIController {


    @RequestMapping("/dashboard")
    public String index() {
        return "Home Page is here";
    }

    @RequestMapping( method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/cassandra/order/get")
    public Object getData(@RequestParam(value="orderId", defaultValue="1") String orderId)
    {
	 Connection conn = new Connection();
	 Session session = conn.getSession();
         ResultSet resultSet = session.execute("SELECT JSON * FROM test.orders WHERE order_id="+ "'"+orderId+"'");
         Row row = resultSet.one();
         String jsonString = row.getString(0);
         return(jsonString);
    }

/*    
 * This method accepts order data in JSON format
 *
 */    
    @RequestMapping( method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value = "/cassandra/order/put")
    public String putData(@RequestBody String inputData) 
    {

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

	  return (inputData);
    }
}
