

package com.batria;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
//import com.test.springboot.Sales;
//import com.test.springboot.SalesTrend;
import java.util.*;
import com.google.gson.JsonArray;
//import net.sf.json.JSONException;

import java.util.Map;
import java.util.List;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
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
import org.json.JSONException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import org.springframework.web.bind.annotation.PostMapping;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.annotation.JsonInclude;

//import com.fasterxml.jackson.annotation.jsoninclude.Include;
//import com.fasterxml.jackson.core.Include;

//@JsonInclude(Include.NON_EMPTY)
@RestController
public class RestAPIController {

    //private static final Logger logger = LogManager.getLogger("appLogger");

    private static String LOCAL_FOLDER = "";

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

//    @PostMapping("/upload")
    @RequestMapping( method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value = "/upload")
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
//	return "redirect:/uploadStatus";
    }
//    @GetMapping("/uploadStatus")
//    public String uploadStatus()
//    {
//	return "uploadStatus";	
//    }
}
