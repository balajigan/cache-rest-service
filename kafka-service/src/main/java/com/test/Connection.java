
/* Class for creating connection with DSE or Cassandra.
 * Default 128 connection pool is used.
 * Don't shut down the cluster. Opening the new connections will take sometime.
 */
package com.test;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.Properties;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class Connection
{
	private static Logger logger = LogManager.getLogger("Connection");
	//public static Cluster cluster;
	public static Producer<String, String> producer = null;
	private String serverIp = "127.0.0.1:9092";
	public Connection()
	{

	}
	public Connection(String serverIp)
	{
		this.serverIp = serverIp;
	}

	public Producer getProducer()
	{
		if(producer == null)
		{
			Properties props = new Properties();
			props.put("bootstrap.servers", serverIp);
			props.put("acks", "all");
			props.put("retries", 0);
			props.put("batch.size", 16384);
			props.put("linger.ms", 0);
			props.put("buffer.memory", 33554432);
			props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
			props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
			
			try{
				producer = new KafkaProducer<String, String>(props);
				logger.info("getProducer method is called");
			}
			catch(Exception ex)
			{
				logger.error("Issues in opening connection with Cassandra");
				ex.printStackTrace();
			}
		}
		return producer;
	}
	public boolean produceMessage(Producer producer, String topic, String key, String message)
	{
		boolean status = true;
		try{
			ProducerRecord record = new ProducerRecord<String, String>(topic, key, message);
			producer.send(record);
		}
		catch(Exception ex)
		{
			status = false;
		}
	return (status);	
	}

	public boolean close()
	{
		if(producer != null)
		{
			producer.close();
		}
	   return true;		
	}

}
