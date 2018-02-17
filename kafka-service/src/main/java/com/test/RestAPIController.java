
/* This controller class is developed for conducting the performance tests on Datastax Enterprise Edition (DSE)
 * or Cassandra database.
 * 
 * 
 */

package com.test;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.*;
import com.google.gson.JsonArray;

import java.util.Map;
import java.util.List;

import java.io.IOException;
import com.google.gson.Gson;
import com.test.Connection;

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
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import java.io.InputStream;
import java.io.FileInputStream;



@RestController
public class RestAPIController {

    private static final Logger logger = LogManager.getLogger(RestAPIController.class);

    private static String LOCAL_FOLDER = "";

    // Configure this IP before starting the API.
    private static String serverIp = "10.128.0.3";

    @RequestMapping( method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/kafka/topic/create")
    public Object createTopic(@RequestParam(value="topic", defaultValue="test-topic") String topic)
    {
	return("SUCCESS");
    }
/*    
 * This method accepts order data in JSON format and writes into database.
 * If the write fails, it will return FAILURE. Else, SUCCESS will be returned.
 */    
    @RequestMapping( method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value = "/kafka/produce")
    public String putData(@RequestBody String inputData) 
    {
          long startTimeMs = System.currentTimeMillis(); 
          String returnStatus = "SUCCESS";

	  try
	  {
	  }
	  catch (Exception ex)
	  {
		  returnStatus = "FAILURE";
		  logger.error("Exception in producing the  message");
		  throw new RuntimeException("Error in producing message to kafka");
	  }
         long endTimeMs = System.currentTimeMillis();
	 //System.out.println("putData exec time = "+ Long.toString(endTimeMs - startTimeMs));
         logger.info("POST exec time ms = " + Long.toString(endTimeMs - startTimeMs));
	  return (returnStatus);
    }
}
