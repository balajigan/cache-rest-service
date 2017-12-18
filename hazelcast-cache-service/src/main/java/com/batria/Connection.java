
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
	private String serverIp = "127.0.0.1:5701";
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
			clientConfig.getGroupConfig().setName("HzCluster").setPassword("HzCluster");
			clientConfig.getNetworkConfig().addAddress(serverIp);

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
