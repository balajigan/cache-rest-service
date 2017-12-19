

package com.batria;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.*;
import com.google.gson.JsonArray;
//import net.sf.json.JSONException;

import java.util.Map;
import java.util.List;

//import org.codehaus.jackson.map.ObjectMapper;
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
//import org.json.JSONParser;
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


//import com.fasterxml.jackson.annotation.jsoninclude.Include;
//import com.fasterxml.jackson.core.Include;

//@JsonInclude(Include.NON_EMPTY)
@RestController
public class RestAPIController {

    private static final Logger logger = LogManager.getLogger(RestAPIController.class);

    private static String LOCAL_FOLDER = "";
    private static String serverIp = "127.0.0.1:5701";
//    @RequestMapping("/dashboard")
//    public String index() {
//        return "Home Page is here";
//    }


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
		//client.shutdown();
		//System.out.println("order from Hazelcast = " + orderData);

	 }
	 catch(Exception ex)
	 {
		logger.error("Exception while reading from orderMap");
		ex.printStackTrace();
	 }
	 finally
	 {	// TODO - enable the connection pooling and don't shutdown the client.
		client.shutdown();
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
          JSONObject jsonObj = new JSONObject(inputData); 
          //System.out.println("inputData = "+ inputData);
	  String orderId = (String) jsonObj.get("orderId");
  	  //System.out.println("orderId = " + orderId);	  
	  Connection conn = new Connection(serverIp);
	  HazelcastInstance client = conn.getClient();
	  try
	  {
	      IMap<String, String> mapOrders = client.getMap("ordersMap");
	      // PUT the data into the map
	      mapOrders.put(orderId, inputData);
	  }
	  catch (Exception ex)
	  {
		  returnStatus = "FAILURE";
		  //System.out.println("Exception");
		  ex.printStackTrace();
		  logger.error("Exception in inserting data");
	  }
	  finally
	  {	// Enable the connection pooling and don't shutdown the client here.
		client.shutdown();
	  }
         long endTimeMs = System.currentTimeMillis();
         logger.info("POST exec time ms = " + Long.toString(endTimeMs - startTimeMs));
	 return (returnStatus);
    }

/*    
//    @PostMapping("/upload")
    @RequestMapping( method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value = "hazelcast/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes)
    {
	if(file.isEmpty())
	{
		redirectAttributes.addFlashAttribute("message", "Upload a file");
		return "redirect:uploadStatus";
	}
	try{
		byte[] bytes = file.getBytes();
		Path path = Paths.get(LOCAL_FOLDER + file.getOriginalFilename());
		Files.write(path, bytes);
                String inputFileName = file.getOriginalFilename();
		System.out.println("File Name = " + inputFileName);
                String tableName = inputFileName.substring(0, inputFileName.lastIndexOf("_")).toLowerCase();
		System.out.println("TableName = " + tableName);
		File input = new File(LOCAL_FOLDER + file.getOriginalFilename());
		File output = new File(LOCAL_FOLDER + "output.json");
		CsvSchema bootstrap = CsvSchema.emptySchema().withHeader();
		CsvMapper csvMapper = new CsvMapper();
		MappingIterator<Map<?, ?>> mappingIterator = null;
		mappingIterator = csvMapper.reader(Map.class).with(bootstrap).readValues(input);

		List<Map<?, ?>> data = mappingIterator.readAll();
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		mapper.writerWithDefaultPrettyPrinter().writeValue(output,data);
		Connection conn = new Connection();
		Session session = conn.getSession();
                for(int index=0; index < data.size(); index++)
		{
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data.get(index));
			System.out.println("Value = " + jsonStr);
			session.execute("INSERT INTO test."+ tableName +" JSON " + "'"+ jsonStr + "'");	
		}

		 redirectAttributes.addFlashAttribute("message","successfully uploaded..");

	}
	catch (IOException e)
	{
		e.printStackTrace();
	}
	return("SUCCESS");
    }
    */
}
