
/* Class for creating connection with Hazelcast.
 * Hazelcast creates default (based on the available CPU cores) connection pool and keep them warm.
 * Don't shut down the client. Opening the new connections will take sometime.
 */

package com.batria;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;


public class Connection
{
	private static Logger logger = LogManager.getLogger("Connection");
	public static HazelcastInstance client;
	private String serverIp = "10.128.0.4:5701";
	public Connection()
	{

	}
	public Connection(String serverIp)
	{
		this.serverIp = serverIp;
	}
	public HazelcastInstance getClient()
	{
		if(client == null)
		{
			try{

			ClientConfig clientConfig = new ClientConfig();
			clientConfig.getGroupConfig().setName("HzCluster1").setPassword("HzCluster1+");
			clientConfig.getNetworkConfig().addAddress(serverIp).setRedoOperation(true);

		        client = HazelcastClient.newHazelcastClient(clientConfig);

			logger.info("getClient method is called");
			}
			catch(Exception ex)
			{
				logger.error("Issues in opening connection with Hazelcast");
			}
		}
		return client;
	}
       
}
