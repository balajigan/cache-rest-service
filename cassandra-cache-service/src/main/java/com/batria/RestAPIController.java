
/* This controller class is developed for conducting the performance tests on Datastax Enterprise Edition (DSE)
 * or Cassandra database.
 * 
 * 
 */

package com.batria;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.*;
import com.google.gson.JsonArray;

import java.util.Map;
import java.util.List;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
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

@RestController
public class RestAPIController {

    private static final Logger logger = LogManager.getLogger(RestAPIController.class);

    private static String LOCAL_FOLDER = "";

    // Configure this IP before starting the API.
    private static String serverIp = "10.128.0.3";

 //   @RequestMapping("/dashboard")
 //   public String index() {
 //       return "Home Page is here";
 //   }

    // Pass the orderId, for getting the order details.    
    @RequestMapping( method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/cassandra/order/get")
    public Object getData(@RequestParam(value="orderId", defaultValue="1") String orderId)
    {
         long startTimeMs = System.currentTimeMillis();	

	 String jsonString = null;
	 try{
	 	Connection conn = new Connection(serverIp);
	 	Session session = conn.getSession();
         	ResultSet resultSet = session.execute("SELECT JSON * FROM test.orders WHERE order_id="+ "'"+orderId+"'");

         	Row row = resultSet.one();
         	jsonString = row.getString(0);
	 }
	 catch(Exception ex)
	 {
		logger.error("Exception in getData method");
	 }
	 
	 long endTimeMs = System.currentTimeMillis();
	 //System.out.println("getData exec time = "+ Long.toString(endTimeMs - startTimeMs));
         logger.info("GET exec time ms = " + Long.toString(endTimeMs - startTimeMs));
         return(jsonString);
    }

/*    
 * This method accepts order data in JSON format and writes into database.
 * If the write fails, it will return FAILURE. Else, SUCCESS will be returned.
 */    
    @RequestMapping( method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value = "/cassandra/order/put")
    public String putData(@RequestBody String inputData) 
    {
          long startTimeMs = System.currentTimeMillis(); 
          String returnStatus = "SUCCESS";

	  try
	  {
	      Connection conn = new Connection(serverIp);
    	      Session session = conn.getSession();	      
	      session.execute("INSERT INTO test.orders JSON " + "'" + inputData + "'");
	  }
	  catch (Exception ex)
	  {
		  returnStatus = "FAILURE";
		  logger.error("Exception in inserting data");
	  }
         long endTimeMs = System.currentTimeMillis();
	 //System.out.println("putData exec time = "+ Long.toString(endTimeMs - startTimeMs));
         logger.info("POST exec time ms = " + Long.toString(endTimeMs - startTimeMs));
	  return (returnStatus);
    }

    /* Call this API, for uploading a file into Cassandra database. 
     * The file name should be in a specific format (tableName_seqNumber.csv)
     * Example: CUST_ORDER_0001.csv and CUST_ORDER_0002.csv for uploading data into CUST_ORDER table.
     *
     * Appropriate table needs to be created in the database, before this API is called.
     */
    @RequestMapping( method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value = "cassandra/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes)
    {
	String returnStatus = "SUCCESS";    
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
		
		// Ignore the attributes in JSON, if the value is null OR the filed is empty.

		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		mapper.writerWithDefaultPrettyPrinter().writeValue(output,data);
		Connection conn = new Connection();
		Session session = conn.getSession();

		// Get the records one by one and insert into the DB.
                for(int index=0; index < data.size(); index++)
		{
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data.get(index));
			//System.out.println("Value = " + jsonStr);
			session.execute("INSERT INTO test."+ tableName +" JSON " + "'"+ jsonStr + "'");	
		}

		 redirectAttributes.addFlashAttribute("message","successfully uploaded..");

	}
	catch (Exception ex)
	{
		logger.error("Issues in uploading file");
		returnStatus = "FAILURE";
	}
	return(returnStatus);
    }
}
